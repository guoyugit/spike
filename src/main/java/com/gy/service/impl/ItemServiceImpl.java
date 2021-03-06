package com.gy.service.impl;

import com.gy.dao.ItemDOMapper;
import com.gy.dao.ItemStockDOMapper;
import com.gy.dataobject.ItemDO;
import com.gy.dataobject.ItemStockDO;
import com.gy.error.BusinessException;
import com.gy.error.EmBusinessError;
import com.gy.service.ItemService;
import com.gy.service.PromoService;
import com.gy.service.model.ItemModel;
import com.gy.service.model.PromoModel;
import com.gy.validator.ValidationResult;
import com.gy.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2019/3/9.
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ValidatorImpl validator;
    @Autowired
    private ItemDOMapper itemDoMapper;
    @Autowired
    private ItemStockDOMapper itemStockDOMapper;

    @Autowired
    private PromoService promoService;

    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {
        //校验入参
        ValidationResult result = validator.validate(itemModel);
        if (result.isHasError()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
        }
        //转换itemModel->dataObject
        ItemDO itemDO = convertItemDOFromItemModel(itemModel);
        //写入数据库
        itemDoMapper.insertSelective(itemDO);
        itemModel.setId(itemDO.getId());
        ItemStockDO itemStockDO = convertItemStockDOFromItemModel(itemModel);
        itemStockDOMapper.insertSelective(itemStockDO);
        //返回创建完成的对象
        return this.getItemById(itemModel.getId());
    }

    private ItemDO convertItemDOFromItemModel(ItemModel itemModel) {
        if (null == itemModel) return null;
        ItemDO itemDo = new ItemDO();
        BeanUtils.copyProperties(itemModel, itemDo);
        //转换领域模型对象中的price值 有bigdecimal转换为double
        itemDo.setPrice(itemModel.getPrice().doubleValue());
        return itemDo;
    }

    private ItemStockDO convertItemStockDOFromItemModel(ItemModel itemModel) {
        if (null == itemModel) return null;
        ItemStockDO itemStockDo = new ItemStockDO();
        itemStockDo.setStock(itemModel.getStock());
        itemStockDo.setItemId(itemModel.getId());
        return itemStockDo;
    }

    @Override
    public List<ItemModel> listItem() {
        List<ItemDO> items = itemDoMapper.listItem();
        List<ItemModel> itemModelList = items.stream().map(itemDO -> {
            ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
            ItemModel itemModel = converItemModelFromDataObject(itemDO, itemStockDO);
            return itemModel;
        }).collect(Collectors.toList());
        return itemModelList;
    }

    @Override
    public ItemModel getItemById(Integer id) {
        ItemDO itemDO = itemDoMapper.selectByPrimaryKey(id);
        if (itemDO == null) return null;
        //操作获得库存信息
        ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
        ItemModel itemModel = converItemModelFromDataObject(itemDO, itemStockDO);
        //获取活动商品信息
        PromoModel promoModel = promoService.getPromoByItemId(itemModel.getId());
        if (null != promoModel && promoModel.getStatus() != 3) itemModel.setPromoModel(promoModel);
        return itemModel;
    }

    private ItemModel converItemModelFromDataObject(ItemDO itemDO, ItemStockDO itemStockDO) {
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDO, itemModel);
        itemModel.setPrice(new BigDecimal(itemDO.getPrice() + ""));
        itemModel.setStock(itemStockDO.getStock());
        return itemModel;
    }

    @Override
    @Transactional//保证事务一致性
    public boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException {
        int affactedRow = itemStockDOMapper.decreaseStock(itemId, amount);
        if (affactedRow > 0) return true;//更新库存成功
        return false;//更新库存失败
    }

    @Override
    @Transactional
    public void increaseSales(Integer itemId, Integer amount) {
        itemDoMapper.increaseSales(itemId, amount);
    }
}
