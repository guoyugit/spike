package com.gy.service;

import com.gy.dataobject.UserDO;
import com.gy.error.BusinessException;
import com.gy.service.model.UserModel;

/**
 * Created by Administrator on 2019/3/5.
 */
public interface UserService {
    UserModel getUserById(Integer id);

    void register(UserModel userModel) throws BusinessException;

    /*
        telphone:用户注册手机
        encrptPassword:用户加密后的密码
     */
    UserModel validateLogin(String telphone,String encrptPassword) throws BusinessException;
}
