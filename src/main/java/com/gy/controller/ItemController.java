package com.gy.controller;

import com.gy.controller.viewObject.ItemVO;
import com.gy.dataobject.ItemDO;
import com.gy.error.BusinessException;
import com.gy.response.CommonReturnType;
import com.gy.service.ItemService;
import com.gy.service.model.ItemModel;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2019/3/9.
 */
@Controller
@RequestMapping("/item")
@CrossOrigin(allowedHeaders = "*", allowCredentials = "true")
public class ItemController extends BaseController {

    @Autowired
    private ItemService itemService;

    //创建商品controller
    @RequestMapping(value = "/create", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createItem(@RequestParam(name = "title") String title,
                                       @RequestParam(name = "descp") String descp,
                                       @RequestParam(name = "price") BigDecimal price,
                                       @RequestParam(name = "stock") Integer stock,
                                       @RequestParam(name = "imgUrl") String imgUrl) throws BusinessException {
        //封装sercie请求需要的商品信息
        ItemModel itemModel = new ItemModel();
        itemModel.setTitle(title);
        itemModel.setDescp(descp);
        itemModel.setPrice(price);
        itemModel.setStock(stock);
        itemModel.setImgUrl(imgUrl);
        ItemModel itemMD = itemService.createItem(itemModel);
        ItemVO itemVO = convertItemVOFromModel(itemMD);
        return CommonReturnType.create(itemVO);
    }

    //查询商品list
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType list() {
        List<ItemModel> itemModels = itemService.listItem();
        List<ItemVO> itemDOs = itemModels.stream().map(itemModel -> {
            return convertItemVOFromModel(itemModel);
        }).collect(Collectors.toList());
        return CommonReturnType.create(itemDOs);
    }

    //查询单个商品
    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType get(@RequestParam(value = "id") Integer id) {
        ItemModel itemModel = itemService.getItemById(id);
        ItemVO itemVO = convertItemVOFromModel(itemModel);
        return CommonReturnType.create(itemVO);
    }

    private ItemVO convertItemVOFromModel(ItemModel itemModel) {
        if (itemModel == null) return null;
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel, itemVO);
        if (itemModel.getPromoModel() != null) {
            //有即将或正在进行的秒杀活动
            itemVO.setPromoStatus(itemModel.getPromoModel().getStatus());
            itemVO.setPromoId(itemModel.getPromoModel().getId());
            itemVO.setStartDate(itemModel.getPromoModel().getStartDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
            itemVO.setPromoPrice(itemModel.getPromoModel().getPromoItemPrice());
        } else {
            itemVO.setPromoStatus(0);
        }
        return itemVO;
    }

}
