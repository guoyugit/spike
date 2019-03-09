package com.gy.service.model;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2019/3/9.
 */
//用户下单的交易模型
public class OrderModel {
    //交易单号有一定的含义使用string
    private String id;
    //购买的用户id
    private Integer userId;
    //购买的商品id
    private Integer itemId;
    //购买商品的数量
    private Integer amount;
    //购买商品的金额 若promoId非空，则表示是以秒杀商品方式下单
    private BigDecimal orderPrice;

    //商品的单价是不断变化的 存储购买商品时的单价 和购买的数量构成商品的总价，不会发生变化。若promoId非空，则表示是以秒杀商品方式下单
    private BigDecimal itemPrice;

    //若非空，则表示是以秒杀商品方式下单
    private Integer promoId;

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }
}
