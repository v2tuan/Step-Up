package com.stepup.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyAccountDTO {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String verificationCode;
}
