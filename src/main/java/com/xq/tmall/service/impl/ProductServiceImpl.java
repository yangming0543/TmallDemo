package com.xq.tmall.service.impl;

import com.xq.tmall.dao.ProductMapper;
import com.xq.tmall.entity.Product;
import com.xq.tmall.service.ProductService;
import com.xq.tmall.util.OrderUtil;
import com.xq.tmall.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductMapper productMapper;

    @Override
    public boolean add(Product product) {
        return productMapper.insertOne(product) > 0;
    }

    @Override
    public boolean update(Product product) {
        return productMapper.updateOne(product) > 0;
    }

    @Override
    public boolean delete(Integer id) {
        return productMapper.deleteOne(id) > 0;
    }

    @Override
    public List<Product> getList(Product product, Byte[] product_isEnabled_array, OrderUtil orderUtil, PageUtil pageUtil) {
        return productMapper.selectProductList(product, product_isEnabled_array, orderUtil, pageUtil);
    }

    @Override
    public List<Product> getTitle(Product product, PageUtil pageUtil) {
        return productMapper.selectTitle(product, pageUtil);
    }

    @Override
    public Product get(Integer product_Id) {
        return productMapper.selectOne(product_Id);
    }

    @Override
    public Integer getTotal(Product product, Byte[] product_isEnabled_array) {
        return productMapper.selectTotal(product, product_isEnabled_array);
    }

    @Override
    public List<Product> getMoreList(Product product, Byte[] bytes, OrderUtil orderUtil, PageUtil pageUtil, String[] product_name_split) {
        return productMapper.selectMoreList(product, bytes, orderUtil, pageUtil, product_name_split);
    }

    @Override
    public Integer getMoreListTotal(Product product, Byte[] bytes, String[] product_name_split) {
        return productMapper.selectMoreListTotal(product, bytes, product_name_split);
    }

    @Override
    public int saveScheme(List<Product> infos) {
        return productMapper.saveScheme(infos);
    }
}
