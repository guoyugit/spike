package com.gy.error;

/**
 * Created by Administrator on 2019/3/6.
 */
public enum EmBusinessError implements CommonError {
    //定义通用错误类型10000开头
    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),//很多程序需要进行参数校验，输入是否有效等等。通用的错误码使得我们无需定义其他错误信息，如姓名没有填写等，这个时候需要一个方法来改动这个错误信息具体事什么，setErrMsg的作用就是这个。
    UNKNOWN_ERROR(10002,"未知错误"),
    //20000开头为用户信息相关错误递定义
    USER_NOT_EXIST(20001,"用户不存在"),
    USER_LOGIN_FAIL(20002,"用户手机号或密码不正确"),//不能写成不存在手机号，否则容易被人攻击
    USER_NOT_LOGIN(20003,"用户尚未登陆"),
    //通过exception的方式使用该枚举定义类。exception会被controller层的springboot的handler捕获并且做一些处理
    //30000开头为交易信息错误定义
    STOCK_NOT_ENOUGH(30001,"库存不足"),

    ;

    EmBusinessError(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    private int errCode;
    private String errMsg;

    @Override
    public int getErrorCode() {
        return errCode;
    }

    @Override
    public String getErrMsg() {
        return errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
    }
