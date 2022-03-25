package com.xq.tmall.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 回复
 */
@Data
public class Review {
    private Integer review_id;
    private String review_content;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date review_createDate;
    private User review_user;
    private Product review_product;
    private ProductOrderItem review_orderItem;
}
