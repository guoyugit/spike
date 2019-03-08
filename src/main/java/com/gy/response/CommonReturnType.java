package com.gy.response;

/**
 * Created by Administrator on 2019/3/6.
 */
public class CommonReturnType {
    //表明对应请求的返回处理结果 success代表成功，fail代表失败
    private String status;

    //success对应正确数据的json格式
    //fail对应通用的错误码格式
    private Object data;

    //定义一个通用的创建方法
    public static CommonReturnType create(Object result){
        return CommonReturnType.create(result,"success");
    }

    public static CommonReturnType create(Object result, String status) {
        CommonReturnType type = new CommonReturnType();
        type.setStatus(status);
        type.setData(result);
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
