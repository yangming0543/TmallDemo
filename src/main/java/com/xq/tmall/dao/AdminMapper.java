package com.xq.tmall.dao;

import com.xq.tmall.entity.Admin;
import com.xq.tmall.util.PageUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AdminMapper {
    /**
     * 新增管理员
     * @param admin
     * @return
     */
    Integer insertOne(@Param("admin") Admin admin);

    /**
     * 修改管理员
     * @param admin
     * @return
     */
    Integer updateOne(@Param("admin") Admin admin);

    /**
     * 查询管理员列表
     * @param admin_name
     * @param pageUtil
     * @return
     */
    List<Admin> select(@Param("admin_name") String admin_name, @Param("pageUtil") PageUtil pageUtil);

    /**
     * 查询管理员
     * @param admin_name
     * @param admin_id
     * @return
     */
    Admin selectOne(@Param("admin_name") String admin_name, @Param("admin_id") Integer admin_id);

    /**
     * 查询管理员是否存在
     * @param admin_name
     * @param admin_password
     * @return
     */
    Integer selectByLogin(@Param("admin_name") String admin_name, @Param("admin_password") String admin_password);

    /**
     * 查询管理员总数
     * @param admin_name
     * @return
     */
    Integer selectTotal(@Param("admin_name") String admin_name);
}