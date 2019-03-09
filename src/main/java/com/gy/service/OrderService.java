package com.gy.service;

import com.gy.error.BusinessException;
import com.gy.service.model.OrderModel;

/**
 * Created by Administrator on 2019/3/9.
 */
public interface OrderService {
    //1.通过前端url上传递秒杀活动的id，然后下单接口内校验对应id是否属于对应商品且活动已开始
    //2.直接在下单接口内判断对应商品是否存在秒杀活动，若存在进行中的则以秒杀价格下单
    OrderModel createOrder(Integer userId,Integer itemId,Integer amount,Integer promoId) throws BusinessException;
}
