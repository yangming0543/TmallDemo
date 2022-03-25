package com.xq.tmall.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 商品订单
 */
@Data
public class ProductOrder {
    private Integer productOrder_id;
    private String productOrder_code;
    private Address productOrder_address;
    private String productOrder_detail_address;
    private String productOrder_post;
    private String productOrder_receiver;
    private String productOrder_mobile;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date productOrder_pay_date;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date productOrder_delivery_date;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date productOrder_confirm_date;
    private Byte productOrder_status;
    private User productOrder_user;
    private List<ProductOrderItem> productOrderItemList;
}
