package com.xq.tmall.entity;

import lombok.Data;

/**
 * 地址类
 */
@Data
public class Address {
    private String address_areaId;
    private String address_name;
    private Address address_regionId;
}
