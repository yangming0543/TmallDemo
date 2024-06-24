package com.xq.tmall.service.impl;

import com.xq.tmall.dao.ProductOrderMapper;
import com.xq.tmall.entity.OrderGroup;
import com.xq.tmall.entity.ProductOrder;
import com.xq.tmall.service.ProductOrderService;
import com.xq.tmall.util.OrderUtil;
import com.xq.tmall.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductOrderServiceImpl implements ProductOrderService {
    private final ProductOrderMapper productOrderMapper;

    @Override
    public boolean add(ProductOrder productOrder) {
        return productOrderMapper.insertOne(productOrder) > 0;
    }

    @Override
    public boolean update(ProductOrder productOrder) {
        return productOrderMapper.updateOne(productOrder) > 0;
    }

    @Override
    public boolean deleteList(Integer[] productOrder_id_list) {
        return productOrderMapper.deleteList(productOrder_id_list) > 0;
    }

    @Override
    public List<ProductOrder> getList(ProductOrder productOrder, Byte[] productOrder_status_array, OrderUtil orderUtil, PageUtil pageUtil) {
        return productOrderMapper.selectProductOrderList(productOrder, productOrder_status_array, orderUtil, pageUtil);
    }

    @Override
    public List<OrderGroup> getTotalByDate(Date beginDate, Date endDate) {
        return productOrderMapper.getTotalByDate(beginDate, endDate);
    }

    @Override
    public ProductOrder get(Integer productOrder_id) {
        return productOrderMapper.selectOne(productOrder_id);
    }

    @Override
    public ProductOrder getByCode(String productOrder_code) {
        return productOrderMapper.selectByCode(productOrder_code);
    }

    @Override
    public Integer getTotal(ProductOrder productOrder, Byte[] productOrder_status_array) {
        return productOrderMapper.selectTotal(productOrder, productOrder_status_array);
    }
}
