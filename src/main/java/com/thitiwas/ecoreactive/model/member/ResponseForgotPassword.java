package com.thitiwas.ecoreactive.model.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseForgotPassword {
    private String ref;
    private String expiredSecond;
}
