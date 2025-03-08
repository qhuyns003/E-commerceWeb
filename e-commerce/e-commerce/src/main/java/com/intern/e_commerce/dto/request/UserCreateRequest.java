package com.intern.e_commerce.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.intern.e_commerce.validator.DobConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateRequest {

    @Size(min = 5, message = "USERNAME_INVALID")
    String username;

    @Size(min = 5, message = "PASSWORD_INVALID")
    String password;

    String firstName;
    String lastName;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "MM/dd/yyyy")
    @DobConstraint(min = 18, message = "DOB_INVALID")
    LocalDate dob;
}
