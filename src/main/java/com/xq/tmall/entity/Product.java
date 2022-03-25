package com.xq.tmall.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;
/**
 * 商品
 */
@Data
public class Product {
    private Integer product_id;
    private String product_name;
    private String product_title;
    private Double product_price;
    private Double product_sale_price;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date product_create_date;
    private Category product_category;
    private Byte product_isEnabled;
    private List<PropertyValue> propertyValueList;
    private List<ProductImage> singleProductImageList;
    private List<ProductImage> detailProductImageList;
    private List<Review> reviewList;
    private List<ProductOrderItem> productOrderItemList;
    //销量数
    private Integer product_sale_count;
    //评价数
    private Integer product_review_count;
    public Object setProduct_sale_co;

}
