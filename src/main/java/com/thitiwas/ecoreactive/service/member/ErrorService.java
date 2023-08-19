package com.thitiwas.ecoreactive.service.member;


import com.thitiwas.ecoreactive.exception.CustomErrorException;
import com.thitiwas.ecoreactive.model.CommonConstant;
import com.thitiwas.ecoreactive.model.Constant;
import com.thitiwas.ecoreactive.model.ErrorObj;
import com.thitiwas.ecoreactive.model.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ErrorService {
    public CustomErrorException createUserNotFound() {

        ErrorObj errorObj = ErrorObj.builder()
                .errorCode("400")
                .topicMessage("เกิดข้อผิดพลาด")
                .detailMessage("ไม่พบผู้ใช้งานในระบบ")
                .msg("user not found")
                .msgType(Constant.ERROR_MSG_TYPE_POPUP)
                .build();
        return new CustomErrorException(HttpStatus.BAD_REQUEST,
                errorObj,
                CommonConstant.STATUS_FAIL_CODE,
                CommonConstant.STATUS_FAIL);
    }

    public CustomErrorException emailNotValid() {
        // return 400
        ErrorObj errorObj = ErrorObj.builder()
                .errorCode("400")
                .topicMessage("รูปแบบ Email ไม่ถูกต้อง")
                .detailMessage("Email is not valid")
                .msg("Email is not valid")
                .msgType(Constant.ERROR_MSG_TYPE_POPUP)
                .build();
        return new CustomErrorException(HttpStatus.BAD_REQUEST,
                errorObj,
                CommonConstant.STATUS_FAIL_CODE,
                CommonConstant.STATUS_FAIL);
    }

    public CustomErrorException telnoNotValid() {
        ErrorObj errorObj = ErrorObj.builder()
                .errorCode("400")
                .topicMessage("เบอร์โทรไม่ถูกต้อง")
                .detailMessage("รูปแบบ เบอร์โทร ไม่ถูกต้อง")
                .msg("Tel number is not valid")
                .msgType(Constant.ERROR_MSG_TYPE_POPUP)
                .build();
        return new CustomErrorException(HttpStatus.BAD_REQUEST,
                errorObj,
                CommonConstant.STATUS_FAIL_CODE,
                CommonConstant.STATUS_FAIL);
    }

    public CustomErrorException emailIsAlreadyExist() {

        ErrorObj errorObj = ErrorObj.builder()
                .msg("Email is already exist")
                .errorCode("400")
                .topicMessage("ขออภัยคุณไม่สามารถใช้อีเมลนี้ได้")
                .detailMessage("เนื่องจากมีผู้ใช้อีเมลนี้ในระบบแล้ว")
                .msgType(Constant.ERROR_MSG_TYPE_POPUP).build();

        return new CustomErrorException(HttpStatus.OK,
                errorObj,
                CommonConstant.STATUS_FAIL_CODE,
                CommonConstant.STATUS_FAIL);
    }

    public CustomErrorException telNoIsAlreadyExist() {

        ErrorObj errorObj = ErrorObj.builder()
                .msg("Telno has already exist")
                .errorCode("400")
                .topicMessage("ขออภัย\\nคุณไม่สามารถใช้เบอร์นี้ได้")
                .detailMessage("เนื่องจากมีผู้ใช้เบอร์ในระบบแล้ว")
                .msgType(Constant.ERROR_MSG_TYPE_POPUP).build();

        return new CustomErrorException(HttpStatus.OK,
                errorObj,
                CommonConstant.STATUS_FAIL_CODE,
                CommonConstant.STATUS_FAIL);
    }

    public CustomErrorException otpIsWrong() {
        ErrorObj errorObj = ErrorObj.builder()
                .msgType(Constant.ERROR_MSG_TYPE_POPUP)
                .detailMessage("รหัส OTP ผิด")
                .topicMessage("")
                .errorCode("400")
                .msg("OTP is wrong")
                .build();
        return new CustomErrorException(HttpStatus.OK, errorObj,
                CommonConstant.STATUS_FAIL_CODE, CommonConstant.STATUS_FAIL);
    }

    public CustomErrorException otpExpired() {

        ErrorObj errorObj = ErrorObj.builder()
                .msgType(Constant.ERROR_MSG_TYPE_POPUP)
                .detailMessage("OTP หมดอายุ")
                .topicMessage("")
                .errorCode("400")
                .msg("OTP expired")
                .build();
        return new CustomErrorException(HttpStatus.OK, errorObj,
                CommonConstant.STATUS_FAIL_CODE, CommonConstant.STATUS_FAIL);
    }


    public CustomErrorException unAuthorized() {
        ErrorObj build = ErrorObj.builder()
                .errorCode(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
                .msgType(Constant.ERROR_MSG_TYPE_POPUP)
                .topicMessage("")
                .detailMessage("")
                .msg("")
                .build();
        return new CustomErrorException(HttpStatus.UNAUTHORIZED, build, CommonConstant.STATUS_FAIL_CODE,
                CommonConstant.STATUS_FAIL);
    }

    public CustomErrorException feignClientError(ResponseEntity<ResponseWrapper> response) {
        return new CustomErrorException(response.getStatusCode(),
                response.getBody().getObjectValue(),
                response.getBody().getCode(),
                response.getBody().getStatus());
    }

    public CustomErrorException invalidEmailOrPassword() {
        ErrorObj build = ErrorObj.builder()
                .errorCode(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
                .msgType(Constant.ERROR_MSG_TYPE_POPUP)
                .topicMessage("email or password are wrong")
                .detailMessage("อีเมล หรือ รหัสผ่านผิด")
                .msg("invalid email or password")
                .build();
        return new CustomErrorException(HttpStatus.UNAUTHORIZED, build, CommonConstant.STATUS_FAIL_CODE,
                CommonConstant.STATUS_FAIL);
    }

    public CustomErrorException notificationNotFound() {
        ErrorObj errorObj = new ErrorObj("notification not found",
                "400",
                "",
                "",
                Constant.ERROR_MSG_TYPE_POPUP);
        return new CustomErrorException(HttpStatus.BAD_REQUEST, errorObj, CommonConstant.STATUS_FAIL_CODE, CommonConstant.STATUS_FAIL);

    }

    public CustomErrorException notFound() {
        ErrorObj errorObj = new ErrorObj("page not found",
                "404",
                "",
                "",
                "");
        return new CustomErrorException(HttpStatus.NOT_FOUND, errorObj,
                CommonConstant.STATUS_ERROR_CODE, CommonConstant.STATUS_ERROR);
    }

    public CustomErrorException promotionNotFound() {
        ErrorObj errorObj = new ErrorObj("promotion not found",
                "400",
                "",
                "",
                Constant.ERROR_MSG_TYPE_POPUP);
        return new CustomErrorException(HttpStatus.BAD_REQUEST, errorObj,
                CommonConstant.STATUS_FAIL_CODE, CommonConstant.STATUS_FAIL);
    }

    public CustomErrorException outOfPromotion() {
        ErrorObj errorObj = new ErrorObj("out of promotion",
                "200",
                "out of promotion",
                "out of promotion",
                Constant.ERROR_MSG_TYPE_POPUP);
        return new CustomErrorException(HttpStatus.OK, errorObj,
                CommonConstant.STATUS_FAIL_CODE, CommonConstant.STATUS_FAIL);
    }
}
