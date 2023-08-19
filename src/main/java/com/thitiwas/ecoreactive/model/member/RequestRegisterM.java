package com.thitiwas.ecoreactive.model.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestRegisterM {
    private String email;
    private String password;
    private String birthDay;
    private String firstName;
    private String lastName;
    private String telno;
    private String gender;
    private String deviceOS;
    private String clientVersion;
}
