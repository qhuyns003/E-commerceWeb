package com.intern.e_commerce.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailStructure {
    String subject;
    String message;
}
