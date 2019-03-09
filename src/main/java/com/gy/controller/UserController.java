package com.gy.controller;

import com.alibaba.druid.util.StringUtils;
import com.gy.controller.viewObject.UserVO;
import com.gy.error.BusinessException;
import com.gy.error.EmBusinessError;
import com.gy.response.CommonReturnType;
import com.gy.service.UserService;
import com.gy.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Created by Administrator on 2019/3/5.
 */
@Controller
@RequestMapping("/user")
@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    //bean的方式注入说明httpServletRequest是一个单例的模式，单例模式如何支持多个用户并发访问呢？
    //通过spirng包装的httpServletRequest本质是一个proxy，内部有ThreadLocal方式的map使得用户在线程中处理他们自己对应的map，并且有ThreadLocal清除的机制
    @Autowired
    private HttpServletRequest httpServletRequest;

    //用户登陆接口
    @RequestMapping(value = "/login", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name = "telphone") String telphone,
                                     @RequestParam(name = "password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //入参校验
        if (StringUtils.isEmpty(telphone) || StringUtils.isEmpty(password)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //用户登陆服务用来校验登陆用户是否合法
        UserModel userModel = userService.validateLogin(telphone, this.EncodeByMd5(password));

        //将登陆凭证加入到用户登陆成功的session内
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);
        return CommonReturnType.create(null);
    }


    //用户注册接口
    @RequestMapping(value = "/register", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name = "telphone") String telphone,
                                     @RequestParam(name = "otpCode") String otpCode,
                                     @RequestParam(name = "name") String name,
                                     @RequestParam(name = "gender") Integer gender,
                                     @RequestParam(name = "age") Integer age,
                                     @RequestParam(name = "password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //验证手机号与对应的otp码值相符合
        String inSessionOtpCode = (String) this.httpServletRequest.getSession().getAttribute(telphone);
        if (!StringUtils.equals(inSessionOtpCode, otpCode)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "短信验证码不符合！");
        }
        //用户注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setAge(age);
        if(gender != null){
            userModel.setGender(new Byte(String.valueOf(gender)));
        }
        userModel.setTelphone(telphone);
        userModel.setThirdPartyId("byphone");
        if(org.apache.commons.lang3.StringUtils.isNotBlank(password))
        userModel.setEncrptPassword(this.EncodeByMd5(password));
        this.userService.register(userModel);
        return CommonReturnType.create(null);
    }

    //base64加密
    public String EncodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        //加密字符串
        String newStr = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return newStr;
    }

    //用户获取otp短信接口
    @RequestMapping(value = "/getOtp", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name = "telphone") String telphone) {
        //需要按照一定的规则生成otp验证码
        Random random = new Random();
        int randomNum = random.nextInt(99999);
        randomNum += 10000;
        String otpCode = String.valueOf(randomNum);
        //将otp验证码与用户手机号关联 key-value键值对形式，分布式应用系统下使用redis介入，将用户的telphone与otp验证码放在redis中。反复点击getotp时可以重复覆盖。因此telphone永远只有最新生成的otp密码有效 redis还支持过期时间 适合做这种应用。
        //暂时使用httpsession方式绑定手机号与otp码
        httpServletRequest.getSession().setAttribute(telphone, otpCode);
        //将otp验证码通过短信通道发送给用户 省略(正式系统中不能这样做，敏感信息不能打印或保存在日志中，容易造成信息泄露)
        System.out.println("telphone = " + telphone + "& otpcode = " + otpCode);
        return CommonReturnType.create(null);
    }

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BusinessException {
        UserModel userModel = userService.getUserById(id);
        if (null == userModel) {
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        //将核心领域对象转换为可供ui使用的viewObject对象。
        UserVO userVO = convertFromModel(userModel);
        return CommonReturnType.create(userVO);
    }

    public UserVO convertFromModel(UserModel userModel) {
        if (null == userModel) return null;
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }
}
