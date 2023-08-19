package com.thitiwas.ecoreactive.exception;

import com.google.gson.Gson;
import com.thitiwas.ecoreactive.model.CommonConstant;
import com.thitiwas.ecoreactive.model.ErrorObj;
import com.thitiwas.ecoreactive.model.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

//    @ExceptionHandler(UnauthorizedExceptionCustom.class)
//    public ResponseEntity<ResponseObj<ErrorObj>> unauthorized(UnauthorizedExceptionCustom exec) {
//        ResponseObj<ErrorObj> ret = ResponseObj.<ErrorObj>builder()
//                .code(CommonConstant.STATUS_ERROR_CODE)
//                .status(CommonConstant.STATUS_ERROR)
//                .objectValue(ErrorObj.builder()
//                        .msg(exec.getMsg())
//                        .errorCode(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
//                        .topicMessage("")
//                        .detailMessage("")
//                        .msgType("")
//                        .build()
//                ).build();
//        return new ResponseEntity<>(ret, HttpStatus.UNAUTHORIZED);
//    }

//    @ExceptionHandler(TokenExpiredException.class)
//    public ResponseEntity<ResponseObj<ErrorObj>> methodNotAllow(TokenExpiredException exec) {
//        ResponseObj<ErrorObj> ret = ResponseObj.<ErrorObj>builder()
//                .code(CommonConstant.STATUS_ERROR_CODE)
//                .status(CommonConstant.STATUS_ERROR)
//                .objectValue(ErrorObj.builder()
//                        .msg(exec.toString())
//                        .errorCode(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
//                        .topicMessage("")
//                        .detailMessage("")
//                        .msgType("")
//                        .build()
//                ).build();
//        return new ResponseEntity<>(ret, HttpStatus.UNAUTHORIZED);
//    }

    /*@ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseWrapper<ErrorObj>> methodNotAllow() {
        ResponseWrapper<ErrorObj> ret = ResponseWrapper.<ErrorObj>builder()
                .code(CommonConstant.STATUS_ERROR_CODE)
                .status(CommonConstant.STATUS_ERROR)
                .objectValue(ErrorObj.builder()
                        .msg("")
                        .errorCode(String.valueOf(HttpStatus.METHOD_NOT_ALLOWED.value()))
                        .topicMessage("")
                        .detailMessage("")
                        .msgType("")
                        .build()
                ).build();
        return new ResponseEntity<>(ret, HttpStatus.METHOD_NOT_ALLOWED);
    }*/

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({SQLException.class})
    public ResponseEntity<ResponseWrapper<ErrorObj>> sqlException() {
        ResponseWrapper<ErrorObj> ret = ResponseWrapper.<ErrorObj>builder()
                .code(CommonConstant.STATUS_ERROR_CODE)
                .status(CommonConstant.STATUS_ERROR)
                .objectValue(ErrorObj.builder()
                        .msg("")
                        .errorCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .topicMessage("")
                        .detailMessage("")
                        .msgType("")
                        .build()
                ).build();
        return new ResponseEntity<>(ret, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /*@ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ResponseWrapper<ErrorObj>> badRequest(MissingServletRequestParameterException exec) {
        exec.printStackTrace();
        ResponseWrapper<ErrorObj> ret = ResponseWrapper.<ErrorObj>builder()
                .code(CommonConstant.STATUS_ERROR_CODE)
                .status(CommonConstant.STATUS_ERROR)
                .objectValue(ErrorObj.builder()
                        .msg(exec.getMessage())
                        .errorCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                        .topicMessage("")
                        .detailMessage("")
                        .msgType("")
                        .build()
                ).build();
        return new ResponseEntity<>(ret, HttpStatus.BAD_REQUEST);
    }*/

    /*@ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ServletRequestBindingException.class})
    public ResponseEntity<ResponseObj<ErrorObj>> badRequest(ServletRequestBindingException exec) {
        log.debug("ServletRequestBindingException::here");
        log.debug("ServletRequestBindingException::exec :{}", exec.toString());
        exec.printStackTrace();
        ResponseObj<ErrorObj> ret = ResponseObj.<ErrorObj>builder()
                .code(CommonConstant.STATUS_ERROR_CODE)
                .status(CommonConstant.STATUS_ERROR)
                .objectValue(ErrorObj.builder()
                        .msg(exec.getMessage())
                        .errorCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                        .topicMessage("")
                        .detailMessage("")
                        .msgType("")
                        .build()
                ).build();
        return new ResponseEntity<>(ret, HttpStatus.BAD_REQUEST);
    }*/

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseWrapper<ErrorObj>> internalServerError() {
        ResponseWrapper<ErrorObj> ret = ResponseWrapper.<ErrorObj>builder()
                .code(CommonConstant.STATUS_ERROR_CODE)
                .status(CommonConstant.STATUS_ERROR)
                .objectValue(ErrorObj.builder()
                        .msg("")
                        .errorCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .topicMessage("")
                        .detailMessage("")
                        .msgType("")
                        .build()
                ).build();
        return new ResponseEntity<>(ret, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /*@ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseObj<ErrorObj>> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exec) {
        log.debug("methodArgumentTypeMismatchException::here");
        log.error("methodArgumentTypeMismatchException::exec :{}", exec.getMessage());
        exec.printStackTrace();
        ResponseObj<ErrorObj> ret = ResponseObj.<ErrorObj>builder()
                .code(CommonConstant.STATUS_ERROR_CODE)
                .status(CommonConstant.STATUS_ERROR)
                .objectValue(ErrorObj.builder()
                        .msg(exec.getMessage())
                        .errorCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                        .topicMessage("")
                        .detailMessage("")
                        .msgType("")
                        .build()
                ).build();
        return new ResponseEntity<>(ret, HttpStatus.BAD_REQUEST);
    }*/

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public ResponseEntity<ResponseWrapper<ErrorObj>> notFound(ChangeSetPersister.NotFoundException exception) {
        ResponseWrapper<ErrorObj> ret = ResponseWrapper.<ErrorObj>builder()
                .code(CommonConstant.STATUS_ERROR_CODE)
                .status(CommonConstant.STATUS_ERROR)
                .objectValue(ErrorObj.builder()
                        .msg("")
                        .errorCode(String.valueOf(HttpStatus.NOT_FOUND.value()))
                        .topicMessage("")
                        .detailMessage("")
                        .msgType("")
                        .build()
                ).build();
        return new ResponseEntity<>(ret, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({IllegalStateException.class})
    public ResponseEntity<ResponseWrapper<ErrorObj>> handleException(IllegalStateException exc) {
        log.debug("IllegalStateException::error here");
        ResponseWrapper<ErrorObj> ret = ResponseWrapper.<ErrorObj>builder()
                .code(CommonConstant.STATUS_ERROR_CODE)
                .status(CommonConstant.STATUS_ERROR)
                .objectValue(ErrorObj.builder()
                        .msg(exc.getMessage())
                        .errorCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .topicMessage("เกิดข้อผิดพลาด")
                        .detailMessage("")
                        .msgType("")
                        .build()
                ).build();
        return new ResponseEntity<>(ret, HttpStatus.BAD_REQUEST);
    }


    /*@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({FeignException.class})
    public ResponseEntity<String> handleException(FeignException exc) {
        log.debug("FeignException::error here");
        log.debug("FeignException::exc.contentUTF8() :{}", exc.contentUTF8());
        log.debug("FeignException::exc.status() :{}", exc.status());
        log.debug("FeignException::exc :{}", exc.toString());
        Gson gson = new Gson();
        ResponseObj<ErrorObj> ret;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (exc.contentUTF8() == null || exc.contentUTF8().equals("") || exc.contentUTF8().trim().equals("")) {
            ret = ResponseObj.<ErrorObj>builder()
                    .status(CommonConstant.STATUS_ERROR)
                    .code(CommonConstant.STATUS_ERROR_CODE)
                    .objectValue(ErrorObj.builder()
                            .detailMessage("")
                            .errorCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                            .msg("")
                            .msgType("")
                            .topicMessage("")
                            .build())
                    .build();
            return new ResponseEntity<>(gson.toJson(ret), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(exc.contentUTF8(), headers, HttpStatus.valueOf(exc.status()));
        }
    }*/

    @ExceptionHandler({CustomErrorException.class})
    public ResponseEntity<String> customErrorExceptionV2(CustomErrorException exc) {
        log.debug("CustomErrorExceptionV2::error here");
        log.debug("CustomErrorExceptionV2::error string :{}", exc.toString());
        Gson gson = new Gson();
        ResponseWrapper<Object> responseObj = ResponseWrapper.builder()
                .status(exc.getStatus())
                .code(exc.getCode())
                .build();
        responseObj.setObjectValue(exc.getObjectValue());
        String ret = gson.toJson(responseObj);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (exc.getHttpStatus() != null) {
            return new ResponseEntity<>(ret, headers, exc.getHttpStatus());
        } else {
            return new ResponseEntity<>(ret, headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ResponseWrapper<ErrorObj>> handleException(Exception exc) {
        log.debug("handleException::error here");
        log.error("Exception::exc.getMessage() :{}", exc.getMessage());
        exc.printStackTrace();
        ResponseWrapper<ErrorObj> ret = ResponseWrapper.<ErrorObj>builder()
                .code(CommonConstant.STATUS_ERROR_CODE)
                .status(CommonConstant.STATUS_ERROR)
                .objectValue(ErrorObj.builder()
                        .msg(exc.getMessage()) // or not send
                        .errorCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .topicMessage("")
                        .detailMessage("")
                        .msgType("")
                        .build()
                ).build();
        return new ResponseEntity<>(ret, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
