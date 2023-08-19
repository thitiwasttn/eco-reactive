package com.thitiwas.ecoreactive.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ErrorObj {
    private String msg;
    private String errorCode;
    private String topicMessage;
    private String detailMessage;
    private String msgType;
}
