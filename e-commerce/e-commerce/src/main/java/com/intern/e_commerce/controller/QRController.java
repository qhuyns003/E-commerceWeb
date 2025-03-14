package com.intern.e_commerce.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.WriterException;
import com.intern.e_commerce.configuration.QRGenerator;
import com.intern.e_commerce.service.QRService;

@RestController
public class QRController {
    @Autowired
    private QRService qrService;

    @GetMapping("/generate-qr")
    public ResponseEntity<byte[]> generateQR(
            @RequestParam Long orderId,
            @RequestParam String accountNumber,
            @RequestParam(required = false) String description)
            throws WriterException, IOException {

        String vietQRContent = qrService.generateQR(orderId, accountNumber, description);
        byte[] qrImage = QRGenerator.generateQRCode(vietQRContent, 300, 300);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return ResponseEntity.ok().headers(headers).body(qrImage);
    }
}
