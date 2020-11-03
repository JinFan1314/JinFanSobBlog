package net.sunofbeach.blog.response;

import org.aspectj.bridge.IMessage;

/**
 * @program: SobBlogSystem
 * @description: 返回结果的状态
 * @author: JinFan
 * @create: 2020-09-15 14:23
 **/
public enum  ResponseState {

    SUCCESS(true,20000,"操作成功"),
    LOGIN_SUCCESS(true,20001,"登录成功"),
    FILE_UPLOAD_SUCCESS(true,50000,"上传成功"),
    LOGIN_FAILED(false,49999,"登录失败"),
    FILE_UPLOAD_FAILED(false,50001,"上传失败!请重试!"),
    PERMISSION_FORBID(false,40002,"没有权限操作"),
    ACCOUNT_NOT_LOGIN(false,40002,"账号未登录"),
    GET_RESOURCE_FAILED(false,40001,"没有权限操作"),
    JOIN_IN_SUCCESS(true,20004,"注册成功"),
    ACCOUNT_DENIED(false,40003,"账号被禁止"),
    ERROR_403(false,40004,"没有权限操作"),
    ERROR_404(false,20005,"页面丢失"),
    ERROR_405(false,20006,"注册成功"),
    ERROR_504(false,20007,"系统繁忙!"),
    ERROR_505(false,20008,"请求错误"),
    OPERATION_IS_NOT_SUPPORTED(false,40004,"不支持该操作"),
    FAILED(false,40000,"操作失败");

    private int code;

    private String message;

    private boolean success;

    ResponseState(boolean success,int code, String message){
        this.code=code;
        this.message=message;
        this.success=success;
    }



    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
