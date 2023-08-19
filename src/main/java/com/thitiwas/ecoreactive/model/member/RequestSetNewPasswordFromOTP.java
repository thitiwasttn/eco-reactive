package com.thitiwas.ecoreactive.model.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestSetNewPasswordFromOTP {
    private String ref;
    private String otp;
    private String newPassword;
    private String confirmPassword;
}
