package com.xq.tmall.entity;

import lombok.Data;

import java.util.List;

/**
 * 属性
 */
@Data
public class Property {
    private Integer property_id;
    private String property_name;
    private Category property_category;
    private List<PropertyValue> propertyValueList;
}
