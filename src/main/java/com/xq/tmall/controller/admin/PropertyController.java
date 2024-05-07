package com.xq.tmall.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xq.tmall.controller.BaseController;
import com.xq.tmall.entity.Category;
import com.xq.tmall.entity.Property;
import com.xq.tmall.service.CategoryService;
import com.xq.tmall.service.LastIDService;
import com.xq.tmall.service.PropertyService;
import com.xq.tmall.util.Constants;
import com.xq.tmall.util.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * 后台管理-属性页
 */
@Api(tags = "后台管理-属性页")
@Controller
@RequiredArgsConstructor
public class PropertyController extends BaseController {
    private final CategoryService categoryService;
    private final LastIDService lastIDService;
    private final PropertyService propertyService;

    // 转到后台管理-属性页-ajax
    @ApiOperation(value = "转到后台管理-属性页", notes = "转到后台管理-属性页")
    @GetMapping(value = "admin/property")
    public String goToPage(HttpSession session, Map<String, Object> map) {
        // 检查管理员权限
        Object adminId = checkAdmin(session);
        if (adminId == null) {
            return "admin/include/loginMessage";
        }
        // 获取前10条属性列表
        PageUtil pageUtil = new PageUtil(0, 10);
        List<Property> propertyList = propertyService.getList(null, pageUtil);
        map.put("propertyList", propertyList);
        // 获取属性总数量
        Integer propertyCount = propertyService.getTotal(null);
        map.put("propertyCount", propertyCount);
        // 获取分页信息
        pageUtil.setTotal(propertyCount);
        map.put("pageUtil", pageUtil);
        // 转到后台管理-属性页-ajax方式
        return "admin/propertyManagePage";
    }

    // 转到后台管理-属性详情页-ajax
    @ApiOperation(value = "转到后台管理-属性详情页", notes = "转到后台管理-属性详情页")
    @GetMapping(value = "admin/property/{cid}")
    public String goToDetailsPage(HttpSession session, Map<String, Object> map, @PathVariable Integer cid/* 属性ID */) {
        // 检查管理员权限
        Object adminId = checkAdmin(session);
        if (adminId == null) {
            return "admin/include/loginMessage";
        }
        // 获取属性子信息-属性列表
        map.put("property", propertyService.get(cid));
        // 转到后台管理-属性详情页-ajax方式
        return "admin/include/propertyDetails";
    }

    // 转到后台管理-属性添加页-ajax
    @ApiOperation(value = "转到后台管理-属性添加页", notes = "转到后台管理-属性添加页")
    @GetMapping(value = "admin/property/new")
    public String goToAddPage(HttpSession session, Map<String, Object> map) {
        // 检查管理员权限
        Object adminId = checkAdmin(session);
        if (adminId == null) {
            return "admin/include/loginMessage";
        }
        // 转到后台管理-属性添加页-ajax方式
        return "admin/include/propertyDetails";
    }

    // 添加属性信息-ajax
    @ApiOperation(value = "添加属性信息", notes = "添加属性信息")
    @ResponseBody
    @PostMapping(value = "admin/property/{category_id}", produces = "application/json;charset=utf-8")
    public String addCategory(@RequestParam String property_name/* 属性名称 */, @PathVariable("category_id") Integer category_id/* 分类ID */) {
        JSONObject jsonObject = new JSONObject();
        // 整合属性信息
        Property property = new Property();
        property.setProperty_name(property_name);
        Category category=new Category();
        category.setCategory_id(category_id);
        property.setProperty_category(category);
        // 添加属性信息
        boolean yn = propertyService.add(property);
        if (yn) {
            int property_id = lastIDService.selectLastID();
            // 添加成功！,新增属性的ID值为：{}, category_id
            jsonObject.put(Constants.SUCCESS, true);
            jsonObject.put("property_id", property_id);
        } else {
            jsonObject.put(Constants.SUCCESS, false);
            // 添加失败！事务回滚
            throw new RuntimeException();
        }
        return String.valueOf(jsonObject);
    }

    // 更新属性信息-ajax
    @ApiOperation(value = "更新属性信息", notes = "更新属性信息")
    @ResponseBody
    @PutMapping(value = "admin/property/{property_id}", produces = "application/json;charset=utf-8")
    public String updateCategory(@RequestParam String property_name/* 属性名称 */,
                                 @PathVariable("property_id") Integer property_id/* 属性ID */) {
        JSONObject jsonObject = new JSONObject();
        // 整合属性信息
        Property property = new Property();
        property.setProperty_id(property_id);
        property.setProperty_name(property_name);
        // 更新属性信息，属性ID值为：{}, category_id
        boolean yn = propertyService.update(property);
        if (yn) {
            // 更新成功！
            jsonObject.put(Constants.SUCCESS, true);
            jsonObject.put("property_id", property_id);
        } else {
            jsonObject.put(Constants.SUCCESS, false);
            // 更新失败！事务回滚
            throw new RuntimeException();
        }

        return String.valueOf(jsonObject);
    }

    // 按条件查询属性-ajax
    @ApiOperation(value = "按条件查询属性", notes = "按条件查询属性")
    @ResponseBody
    @GetMapping(value = "admin/property/{index}/{count}", produces = "application/json;charset=utf-8")
    public String getCategoryBySearch(@RequestParam(required = false) String property_name/* 属性名称 */,
                                      @PathVariable Integer index/* 页数 */,
                                      @PathVariable Integer count/* 行数 */) throws UnsupportedEncodingException {
        // 移除不必要条件
        if (property_name != null) {
            // 如果为非空字符串则解决中文乱码：URLDecoder.decode(String,"UTF-8");
            property_name = "".equals(property_name) ? null : URLDecoder.decode(property_name, "UTF-8");
        }
        JSONObject object = new JSONObject();
        // 按条件获取第{}页的{}条属性, index + 1, count
        PageUtil pageUtil = new PageUtil(index, count);
        Property property = new Property();
        property.setProperty_name(property_name);
        List<Property> propertyList = propertyService.getList(property, pageUtil);
        object.put("propertyList", JSONArray.parseArray(JSON.toJSONString(propertyList)));
        // 按条件获取属性总数量
        Integer propertyCount = propertyService.getTotal(property);
        object.put("propertyCount", propertyCount);
        // 获取分页信息
        pageUtil.setTotal(propertyCount);
        object.put("totalPage", pageUtil.getTotalPage());
        object.put("pageUtil", pageUtil);
        return String.valueOf(object);
    }

    // 按ID删除属性并返回最新结果-ajax
    @ApiOperation(value = "按ID删除属性并返回最新结果", notes = "按ID删除属性并返回最新结果")
    @ResponseBody
    @GetMapping(value = "admin/property/del/{id}", produces = "application/json;charset=utf-8")
    public String deleteProductById(@PathVariable Integer id) {
        JSONObject object = new JSONObject();
        boolean yn = propertyService.delete(id);
        if (yn) {
            logger.info("删除成功！");
            object.put(Constants.SUCCESS, true);
        } else {
            logger.warn("删除失败！事务回滚");
            object.put(Constants.SUCCESS, false);
            throw new RuntimeException();
        }
        return String.valueOf(object);
    }
}