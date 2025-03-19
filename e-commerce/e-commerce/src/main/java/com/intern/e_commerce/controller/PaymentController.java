package com.intern.e_commerce.controller;

import com.intern.e_commerce.dto.response.ApiResponse;
import com.intern.e_commerce.dto.response.VnpayResponse;
import com.intern.e_commerce.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PaymentController {
    PaymentService paymentService;
    @GetMapping("/vn-pay")
    public ApiResponse<VnpayResponse> pay(HttpServletRequest request) {
        return ApiResponse.<VnpayResponse>builder()
                .result(paymentService.createVnPayPayment(request))
                .build();
    }
    @GetMapping("/vn-pay-callback")
    public ApiResponse<String> payCallbackHandler(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        if (status.equals("00")) {
            return ApiResponse.<String>builder()
                    .result("Success")
                    .build();        }
        else {
            return ApiResponse.<String>builder()
                    .result("Fail")
                    .build();
        }
    }
}
