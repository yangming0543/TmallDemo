package com.xq.tmall.entity;

import lombok.Data;

/**
 * 商品图片
 */
@Data
public class ProductImage {
    private Integer productImage_id;
    private Byte productImage_type;
    private String productImage_src;
    private Product productImage_product;

}
