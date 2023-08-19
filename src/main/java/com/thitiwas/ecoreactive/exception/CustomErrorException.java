package com.thitiwas.ecoreactive.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class CustomErrorException extends RuntimeException {
    private HttpStatus httpStatus;
    private Object objectValue;
    private String code;
    private String status;

    public CustomErrorException(HttpStatus httpStatus, Object objectValue, String code, String status) {
        super(objectValue.toString());
        this.httpStatus = httpStatus;
        this.objectValue = objectValue;
        this.code = code;
        this.status = status;
    }

    @Override
    public String toString() {
        return "CustomErrorException{" +
                "httpStatus=" + httpStatus +
                ", objectValue=" + objectValue +
                ", code='" + code + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
