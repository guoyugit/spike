package com.gy;

import com.gy.dao.UserDOMapper;
import com.gy.dataobject.UserDO;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 */
@SpringBootApplication(scanBasePackages = {"com.gy"})//扫描service，component等注解修饰的bean
@RestController
@MapperScan("com.gy.dao")//扫描dao层 即扫描mapper
public class App {

    @Autowired
    private UserDOMapper userDOMapper;

    @RequestMapping("/")
    public String home() {
        UserDO userDO = userDOMapper.selectByPrimaryKey(1);
        if (null == userDO) {
            return "用户对象不存在！";
        } else {
            return userDO.getName();
        }
    }

    @RequestMapping("/insert")
    public String insert() {
        UserDO user = new UserDO();
        user.setAge(1);
        user.setGender((byte) 1);
        user.setName("测试");
        user.setTelphone("123");
        user.setRegisterMode("微信");
        user.setThirdPartyId("京东");
        int insert = userDOMapper.insert(user);
        if (insert > 0) {
            return "插入用户成功!";
        } else {
            return "插入用户失败。";
        }
    }

    public static void main(String[] args) {
        System.out.println("Hello World!");
        SpringApplication.run(App.class, args);
    }
}
