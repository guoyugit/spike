package com.gy.controller;

import com.alibaba.druid.util.StringUtils;
import com.gy.error.BusinessException;
import com.gy.error.EmBusinessError;
import com.gy.response.CommonReturnType;
import com.gy.service.OrderService;
import com.gy.service.model.OrderModel;
import com.gy.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2019/3/9.
 */
@Controller
@RequestMapping("/order")
@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
public class OrderController extends BaseController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    //封装下单请求
    @RequestMapping(value = "/create", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name = "itemId") Integer itemId,
                                     @RequestParam(name = "amount") Integer amount,
                                     @RequestParam(name = "promoId", required = false) Integer promoId) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
        if (isLogin == null || !isLogin.booleanValue())
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN, "用户还未登陆，不能下单！");
        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute("LOGIN_USER");
        //获取用户的登陆信息
        OrderModel orderModel = orderService.createOrder(userModel.getId(), itemId, amount, promoId);
        return CommonReturnType.create(null);
    }
}
