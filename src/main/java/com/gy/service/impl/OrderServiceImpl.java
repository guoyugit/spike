package com.gy.service.impl;

import com.gy.dao.ItemStockDOMapper;
import com.gy.dao.OrderDOMapper;
import com.gy.dao.SequenceDOMapper;
import com.gy.dataobject.OrderDO;
import com.gy.dataobject.SequenceDO;
import com.gy.error.BusinessException;
import com.gy.error.EmBusinessError;
import com.gy.service.ItemService;
import com.gy.service.OrderService;
import com.gy.service.UserService;
import com.gy.service.model.ItemModel;
import com.gy.service.model.OrderModel;
import com.gy.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Administrator on 2019/3/9.
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderDOMapper orderDOMapper;
    @Autowired
    private SequenceDOMapper sequenceDOMapper;

    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer amount, Integer promoId) throws BusinessException {
        //1.校验下单状态，下单商品是否存在，用户是否合法，购买数量是否正确
        ItemModel itemModel = itemService.getItemById(itemId);
        if (itemModel == null) throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "商品信息不存在！");
        UserModel userModel = userService.getUserById(userId);
        if (userModel == null) throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户不存在！");
        if (amount > 99 || amount <= 0)
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "数量信息不正确！");
        //校验活动信息
        if (promoId != null) {
            //(1)校验对应活动是否存在这个适用商品
            if (promoId.intValue() != itemModel.getPromoModel().getId()) {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "活动信息不正确");
            } else if (itemModel.getPromoModel().getStatus() != 2) {//(2)校验活动是否正在进行中
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "活动还未开始");
            }
        }
        //2.落单减库存
        boolean result = itemService.decreaseStock(itemId, amount);
        if (!result) new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        //3.订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setAmount(amount);
        if (promoId != null)
            orderModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
        else
            orderModel.setItemPrice(itemModel.getPrice());
        orderModel.setOrderPrice(orderModel.getItemPrice().multiply(new BigDecimal(amount)));
        orderModel.setPromoId(promoId);
        orderModel.setId(generateOrderNo());

        OrderDO orderDO = convertFromOrderModel(orderModel);
        orderDOMapper.insertSelective(orderDO);
        //增加商品销量
        itemService.increaseSales(itemId, amount);
        //4.返回前端
        return orderModel;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private String generateOrderNo() {
        //订单号有16位置
        StringBuilder builder = new StringBuilder();
        //前八位为时间格式 年月日
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-", "");
        builder.append(nowDate);
        //中间六位为自增序列
        SequenceDO sequenceDo = sequenceDOMapper.getSequenceByName("order_info");
        int sequence = sequenceDo.getCurrentValue();
        sequenceDo.setCurrentValue(sequenceDo.getCurrentValue() + sequenceDo.getStep());
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDo);
        String sequenceStr = String.valueOf(sequence);
        for (int i = 0; i < 6 - sequenceStr.length(); i++) {
            builder.append(0);
        }
        builder.append(sequenceStr);
        //最后两位为分库分表位 暂时写死
        builder.append("00");
        return builder.toString();
    }

    private OrderDO convertFromOrderModel(OrderModel orderModel) {
        if (orderModel == null) return null;
        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(orderModel, orderDO);
        orderDO.setItemPrice(orderModel.getItemPrice().doubleValue());
        orderDO.setOrderPrice(orderModel.getOrderPrice().doubleValue());
        return orderDO;
    }
}
