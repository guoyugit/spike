package com.gy.controller;

import com.gy.controller.viewObject.UserVO;
import com.gy.dao.UserDOMapper;
import com.gy.dataobject.UserDO;
import com.gy.service.UserService;
import com.gy.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2019/3/5.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("/get")
    @ResponseBody
    public Object getUser(@RequestParam(name = "id") Integer id) {
        UserModel userModel = userService.getUserById(id);
        //将核心领域对象转换为可供ui使用的viewObject对象。
        return convertFromModel(userModel);
    }

    public UserVO convertFromModel(UserModel userModel) {
        if (null == userModel) return null;
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }
}
