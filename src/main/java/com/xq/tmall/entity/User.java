package com.xq.tmall.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 用户
 */
@Data
public class User {
    private Integer user_id;
    private String user_name;
    private String user_nickname;
    private String user_password;
    private String user_realname;
    private Byte user_gender;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date user_birthday;
    private Address user_address;
    private Address user_homeplace;
    private String user_profile_picture_src;
    private List<Review> reviewList;
    private List<ProductOrderItem> productOrderItemList;
    private List<ProductOrder> productOrderList;
}
