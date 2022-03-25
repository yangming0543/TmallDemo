package com.xq.tmall.entity;

import lombok.Data;

import java.util.List;

/**
 * 类别
 */
@Data
public class Category {
    private Integer category_id;
    private String category_name;
    private String category_image_src;
    //产品列表
    private List<Product> productList;
    //产品二维集合
    private List<List<Product>> complexProductList;
    //属性列表
    private List<Property> propertyList;
}
