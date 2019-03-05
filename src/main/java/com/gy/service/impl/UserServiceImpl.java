package com.gy.service.impl;

import com.gy.dao.UserDOMapper;
import com.gy.dao.UserPasswordDOMapper;
import com.gy.dataobject.UserDO;
import com.gy.dataobject.UserPasswordDO;
import com.gy.service.UserService;
import com.gy.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2019/3/5.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;

    @Override
    public UserModel getUserById(Integer id) {
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        if(null == userDO) return null;
        //通过用户id获得对应用户的加密密码信息。
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(id);
        return convertFromDataObject(userDO, userPasswordDO);
    }

    public UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO) {
        if (null == userDO) return null;
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO, userModel);
        //userPasswordDo不能再使用copy，因为id值是重复的。
        if (null != userPasswordDO)
            userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        return userModel;
    }
}
