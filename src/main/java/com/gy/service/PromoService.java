package com.gy.service;

import com.gy.service.model.PromoModel;

/**
 * Created by Administrator on 2019/3/9.
 */
public interface PromoService {
    //根据商品id获取即将或正在进行的秒杀活动
    PromoModel getPromoByItemId(Integer itemId);
}
