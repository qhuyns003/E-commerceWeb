package com.intern.e_commerce.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.intern.e_commerce.entity.Orders;
import com.intern.e_commerce.exception.AppException;
import com.intern.e_commerce.exception.ErrorCode;
import com.intern.e_commerce.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.WriterException;
import com.intern.e_commerce.configuration.QRGenerator;

@RestController
public class QRController {
    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/generate-qr")
    public ResponseEntity<byte[]> generateQR(
            @RequestParam Long orderId,
            @RequestParam String accountNumber,
//            @RequestParam(required = false) String amount,
            @RequestParam(required = false) String description)
            throws WriterException, IOException {

        String vietQRContent = generateVietQR(orderId,accountNumber, description);
        byte[] qrImage = QRGenerator.generateQRCode(vietQRContent, 300, 300);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return ResponseEntity.ok().headers(headers).body(qrImage);
    }

    private String generateVietQR(Long orderId, String accountNumber, String description) {
        StringBuilder qrData = new StringBuilder();

        // Phiên bản EMVCo & Loại giao dịch (QR IBFT - Chuyển khoản)
        qrData.append("000201");
        qrData.append("010212"); // 12 = QR IBFT, 11 = Thanh toán

        // Mã VietQR & Napas
        qrData.append("38560010A0000007270126");

        // Mã ngân hàng VietinBank
        qrData.append("0006970415");

        // Độ dài số tài khoản + số tài khoản
        qrData.append("01");
        qrData.append(String.format("%02d", accountNumber.length()));
        qrData.append(accountNumber);

        // Loại giao dịch cố định QR IBFT
        qrData.append("0208QRIBFTTA");

        // Loại tiền tệ (VND)
        qrData.append("5303704");

        // Số tiền (nếu có)
        List<Long> prices = new ArrayList<>();
        Orders orders = orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        orders.getOrderDetailList().stream().forEach(orderDetail -> prices.add(orderDetail.getPrice()*orderDetail.getQuantity()));
        String amount = prices.stream().mapToLong(Long::longValue).sum()+"";
        if (amount != null && !amount.isEmpty()) {
            // Định dạng số tiền với dấu phẩy mỗi 3 số từ cuối lên
            amount = formatAmountWithCommas(amount);

            qrData.append("54");
            qrData.append(String.format("%02d", amount.length())); // Độ dài mới sau khi thêm dấu phẩy
            qrData.append(amount);
        }

        // Mã quốc gia (VN)
        qrData.append("5802VN");

        // Nội dung giao dịch (nếu có)
        if (description != null && !description.isEmpty()) {
            qrData.append("62");

            String prefix = "08" + String.format("%02d", description.length()); // Tạo "08XX"
            int totalLength = prefix.length() + description.length(); // Tổng độ dài của trường 62

            qrData.append(String.format("%02d", totalLength)); // Ghi tổng độ dài của 62
            qrData.append(prefix); // Ghi "08XX"
            qrData.append(description); // Ghi nội dung mô tả
        }

        // Thêm mã kiểm tra CRC16
        String dataForCRC = qrData.toString() + "6304"; // Thêm "6304" trước khi tính CRC
        String crc16 = generateCRC16(dataForCRC);
        qrData.append("6304").append(crc16);

        return qrData.toString();
    }

    private String generateCRC16(String data) {
        int crc = 0xFFFF; // Khởi tạo CRC16-CCITT (False)
        for (byte b : data.getBytes(StandardCharsets.ISO_8859_1)) {
            crc ^= (b & 0xFF) << 8;
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x8000) != 0) {
                    crc = (crc << 1) ^ 0x1021;
                } else {
                    crc <<= 1;
                }
            }
        }
        return String.format("%04X", crc & 0xFFFF); // Trả về mã CRC16-CCITT (False)
    }

    private String formatAmountWithCommas(String amount) {
        StringBuilder formattedAmount = new StringBuilder();
        int count = 0;

        // Duyệt từ cuối chuỗi lên
        for (int i = amount.length() - 1; i >= 0; i--) {
            formattedAmount.insert(0, amount.charAt(i));
            count++;
            if (count % 3 == 0 && i > 0) {
                formattedAmount.insert(0, ","); // Chèn dấu phẩy
            }
        }
        return formattedAmount.toString();
    }
}
