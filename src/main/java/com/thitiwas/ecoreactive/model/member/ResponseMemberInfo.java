package com.thitiwas.ecoreactive.model.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMemberInfo {
    private String email;
    private String firstName;
    private String lastName;
    private String telno;
    private String birthDate;
    private String gender;
    private String coin;
}
