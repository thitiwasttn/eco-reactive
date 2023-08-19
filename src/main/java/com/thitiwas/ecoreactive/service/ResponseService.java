package com.thitiwas.ecoreactive.service;

import com.thitiwas.ecoreactive.model.CommonConstant;
import com.thitiwas.ecoreactive.model.ResponseWrapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ResponseService {
    public <T> Mono<ResponseWrapper<T>> createResponseSuccess(T cls) {
        return Mono.fromCallable(() -> ResponseWrapper.<T>builder()
                .code(CommonConstant.STATUS_SUCCESS_CODE)
                .status(CommonConstant.STATUS_SUCCESS)
                .objectValue(cls)
                .build());
    }
}
