package com.gy.service;

import com.gy.dataobject.UserDO;
import com.gy.service.model.UserModel;

/**
 * Created by Administrator on 2019/3/5.
 */
public interface UserService {
    UserModel getUserById(Integer id);
}
