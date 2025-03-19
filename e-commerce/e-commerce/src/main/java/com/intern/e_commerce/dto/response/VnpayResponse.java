package com.intern.e_commerce.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class VnpayResponse {
    String code;
    String message;
    String paymentUrl;
}
