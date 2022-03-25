package com.xq.tmall.entity;


import lombok.Data;

/**
 * 属性值
 */
@Data
public class PropertyValue {
    private Integer propertyValue_id;
    private String propertyValue_value;
    private Property propertyValue_property;
    private Product propertyValue_product;
}
