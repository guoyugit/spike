package com.gy.error;

/**
 * Created by Administrator on 2019/3/6.
 */
public interface CommonError {
    public int getErrorCode();
    public String getErrMsg();
    public CommonError setErrMsg(String errMsg);
}
