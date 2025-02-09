package com.xq.tmall.service.impl;

import com.xq.tmall.dao.AdminMapper;
import com.xq.tmall.entity.Admin;
import com.xq.tmall.service.AdminService;
import com.xq.tmall.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminMapper adminMapper;

    @Override
    public boolean add(Admin admin) {
        return adminMapper.insertOne(admin) > 0;
    }

    @Override
    public boolean update(Admin admin) {
        return adminMapper.updateOne(admin) > 0;
    }

    @Override
    public List<Admin> getList(String admin_name, PageUtil pageUtil) {
        return adminMapper.selectAdminList(admin_name, pageUtil);
    }

    @Override
    public Admin get(String admin_name, Integer admin_id) {
        return adminMapper.selectOne(admin_name, admin_id);
    }

    @Override
    public Integer login(String admin_name, String admin_password) {
        return adminMapper.selectByLogin(admin_name, admin_password);
    }

    @Override
    public Integer getTotal(String admin_name) {
        return adminMapper.selectTotal(admin_name);
    }

    @Override
    public Admin getAdmin(String userName, String password) {
        return adminMapper.getAdmin(userName, password);
    }
}
