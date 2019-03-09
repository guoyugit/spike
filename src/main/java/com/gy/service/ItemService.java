package com.gy.service;

import com.gy.error.BusinessException;
import com.gy.service.model.ItemModel;

import java.util.List;

/**
 * Created by Administrator on 2019/3/9.
 */
public interface ItemService {
    //创建商品
    ItemModel createItem(ItemModel itemModel) throws BusinessException;
    //商品列表浏览
    List<ItemModel> listItem();
    //商品详情浏览
    ItemModel getItemById(Integer id);
}
