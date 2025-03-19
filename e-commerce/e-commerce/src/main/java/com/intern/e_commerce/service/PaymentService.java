package com.intern.e_commerce.service;

import com.intern.e_commerce.Util.VnpayUtil;
import com.intern.e_commerce.configuration.VnpayConfig;
import com.intern.e_commerce.dto.response.VnpayResponse;
import com.intern.e_commerce.entity.Orders;
import com.intern.e_commerce.exception.AppException;
import com.intern.e_commerce.exception.ErrorCode;
import com.intern.e_commerce.repository.OrderRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final VnpayConfig vnPayConfig;
    private final OrderRepository orderRepository;
    public VnpayResponse createVnPayPayment(HttpServletRequest request) {

        List<Long> prices = new ArrayList<>();
        Long orderId= Long.parseLong(request.getParameter("orderId"));
        Orders orders =
                orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        orders.getOrderDetailList().stream()
                .forEach(orderDetail -> prices.add(orderDetail.getPrice() * orderDetail.getQuantity()));
        Long amount = prices.stream().mapToLong(Long::longValue).sum() *100L;

        String bankCode = request.getParameter("bankCode");
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", VnpayUtil.getIpAddress(request));
        //build query url

        String queryUrl = VnpayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VnpayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VnpayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_Payurl() + "?" + queryUrl;
        return  VnpayResponse.builder()
                .code("ok")
                .message("success")
                .paymentUrl(paymentUrl).build();
    }



}
