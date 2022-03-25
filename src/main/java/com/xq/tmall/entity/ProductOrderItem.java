package com.xq.tmall.entity;

import lombok.Data;

/**
 * 商品订单子类
 */
@Data
public class ProductOrderItem {
    private Integer productOrderItem_id;
    private Short productOrderItem_number;
    private Double productOrderItem_price;
    private Product productOrderItem_product;
    private ProductOrder productOrderItem_order;
    private User productOrderItem_user;
    private String productOrderItem_userMessage;
    //订单产品是否已经评价
    private Boolean isReview;
}
