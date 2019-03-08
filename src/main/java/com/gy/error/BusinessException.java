package com.gy.error;

/**
 * Created by Administrator on 2019/3/6.
 */
//包装器业务异常类实现
public class BusinessException extends Exception implements CommonError{

    private CommonError commonError;

    //直接接收EmBunissesError的传参用于构造业务异常
    public BusinessException(CommonError commonError) {
        this.commonError = commonError;
    }
    //接收自定义erMsg的方式构造业务异常
    public BusinessException(CommonError commonError,String errMsg) {
        this.commonError = commonError;
        this.commonError.setErrMsg(errMsg);
    }

    @Override
    public int getErrorCode() {
        return this.commonError.getErrorCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this;
    }
}
