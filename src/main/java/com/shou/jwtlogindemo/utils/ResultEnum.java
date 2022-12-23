package com.shou.jwtlogindemo.utils;

public enum ResultEnum {
    SUCCESS(200,"成功"),
    FAILURE(500,"失败"),
    USER_NEED_AUTHORITIES(203,"用户未登录"),
    USER_LOGIN_FAILED(401,"用户账号或密码错误"),
    USER_LOGIN_SUCCESS(200,"用户登录成功"),
    USER_NO_ACCESS(403,"用户无权访问"),
    USER_LOGOUT_SUCCESS(200,"用户登出成功"),
    TOKEN_IS_BLACKLIST(401,"此token为黑名单"),
    LOGIN_IS_OVERDUE(401,"登录已失效"),

    ;

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResultEnum parse(int code){
        ResultEnum[] values = values();
        for (ResultEnum value : values) {
            if(value.getCode() == code){
                return value;
            }
        }
        throw  new RuntimeException("Unknown code of ResultEnum");
    }

    ResultEnum() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}