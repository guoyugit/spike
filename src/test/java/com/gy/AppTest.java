package com.gy;

import static org.junit.Assert.assertTrue;

import com.gy.dao.ItemDOMapper;
import com.gy.dao.UserDOMapper;
import com.gy.dataobject.ItemDO;
import com.gy.dataobject.UserDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Unit test for simple App.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class AppTest 
{

    @Autowired
    private ItemDOMapper itemDOMapper;
    @Autowired
    private UserDOMapper userDOMapper;

    @Test
    public void testInsertItem(){
        ItemDO itemDO = new ItemDO();
//        itemDO.setTitle("ceshi");
//        itemDO.setPrice(1200d);
//        itemDO.setDesc("ceshi desc");
//        itemDO.setSales(10);
//        itemDO.setImgUrl("url");
        ItemDO itemDO1 = itemDOMapper.selectByPrimaryKey(1);
        System.out.println();
    }
    @Test
    public void testInsertUser(){
        UserDO userDO = new UserDO();
        userDO.setName("测试");
        userDO.setTelphone("1234567");
        userDOMapper.insertSelective(userDO);
    }

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }
}
