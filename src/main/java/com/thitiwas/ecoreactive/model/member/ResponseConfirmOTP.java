package com.thitiwas.ecoreactive.model.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseConfirmOTP {
    private String memberId;
    private String email;
    private String accessToken;
    private String firstName;
    private String lastName;
    private String telno;
}
