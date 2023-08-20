package com.thitiwas.ecoreactive.model.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserM {
    private String email;
    private String password;
    private String type;
    private String telno;
}
