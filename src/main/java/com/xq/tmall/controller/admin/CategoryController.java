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
 * 后台管理-分类页
 */
@Api(tags = "后台管理-分类页")
@Controller
@RequiredArgsConstructor
public class CategoryController extends BaseController {
    private final CategoryService categoryService;
    private final LastIDService lastIDService;
    private final PropertyService propertyService;

    // 转到后台管理-分类页-ajax
    @ApiOperation(value = "转到后台管理-分类页", notes = "转到后台管理-分类页")
    @GetMapping(value = "admin/category")
    public String goToPage(HttpSession session, Map<String, Object> map) {
        // 检查管理员权限
        Object adminId = checkAdmin(session);
        if (adminId == null) {
            return "admin/include/loginMessage";
        }
        // 获取前10条分类列表
        PageUtil pageUtil = new PageUtil(0, 10);
        List<Category> categoryList = categoryService.getList(null, pageUtil);
        map.put("categoryList", categoryList);
        // 获取分类总数量
        Integer categoryCount = categoryService.getTotal(null);
        map.put("categoryCount", categoryCount);
        // 获取分页信息
        pageUtil.setTotal(categoryCount);
        map.put("pageUtil", pageUtil);
        // 转到后台管理-分类页-ajax方式
        return "admin/categoryManagePage";
    }

    // 转到后台管理-分类详情页-ajax
    @ApiOperation(value = "转到后台管理-分类详情页", notes = "转到后台管理-分类详情页")
    @GetMapping(value = "admin/category/{cid}")
    public String goToDetailsPage(HttpSession session, Map<String, Object> map, @PathVariable Integer cid/* 分类ID */) {
        // 检查管理员权限
        Object adminId = checkAdmin(session);
        if (adminId == null) {
            return "admin/include/loginMessage";
        }
        // 获取category_id为{}的分类信息, cid
        Category category = categoryService.get(cid);
        // 获取分类子信息-属性列表
        Property property = new Property();
        property.setProperty_category(category);
        category.setPropertyList(propertyService.getList(property, null));
        map.put("category", category);
        // 转到后台管理-分类详情页-ajax方式
        return "admin/include/categoryDetails";
    }

    // 转到后台管理-分类添加页-ajax
    @ApiOperation(value = "转到后台管理-分类添加页", notes = "转到后台管理-分类添加页")
    @GetMapping(value = "admin/category/new")
    public String goToAddPage(HttpSession session, Map<String, Object> map) {
        // 检查管理员权限
        Object adminId = checkAdmin(session);
        if (adminId == null) {
            return "admin/include/loginMessage";
        }
        // 转到后台管理-分类添加页-ajax方式
        return "admin/include/categoryDetails";
    }

    // 添加分类信息-ajax
    @ApiOperation(value = "添加分类信息", notes = "添加分类信息")
    @ResponseBody
    @PostMapping(value = "admin/category", produces = "application/json;charset=utf-8")
    public String addCategory(@RequestParam String category_name/* 分类名称 */,
                              @RequestParam String category_image_src/* 分类图片路径 */) {
        JSONObject jsonObject = new JSONObject();
        // 整合分类信息
        Category category = new Category();
        category.setCategory_name(category_name);
        category.setCategory_image_src(category_image_src.substring(category_image_src.lastIndexOf("/") + 1));
        // 添加分类信息
        boolean yn = categoryService.add(category);
        if (yn) {
            int category_id = lastIDService.selectLastID();
            // 添加成功！,新增分类的ID值为：{}, category_id
            jsonObject.put(Constants.SUCCESS, true);
            jsonObject.put("category_id", category_id);
        } else {
            jsonObject.put(Constants.SUCCESS, false);
            // 添加失败！事务回滚
            throw new RuntimeException();
        }
        return String.valueOf(jsonObject);
    }

    // 更新分类信息-ajax
    @ApiOperation(value = "更新分类信息", notes = "更新分类信息")
    @ResponseBody
    @PutMapping(value = "admin/category/{category_id}", produces = "application/json;charset=utf-8")
    public String updateCategory(@RequestParam String category_name/* 分类名称 */,
                                 @RequestParam String category_image_src/* 分类图片路径 */,
                                 @PathVariable("category_id") Integer category_id/* 分类ID */) {
        JSONObject jsonObject = new JSONObject();
        // 整合分类信息
        Category category = new Category();
        category.setCategory_id(category_id);
        category.setCategory_name(category_name);
        category.setCategory_image_src(category_image_src.substring(category_image_src.lastIndexOf("/") + 1));
        // 更新分类信息，分类ID值为：{}, category_id
        boolean yn = categoryService.update(category);
        if (yn) {
            // 更新成功！
            jsonObject.put(Constants.SUCCESS, true);
            jsonObject.put("category_id", category_id);
        } else {
            jsonObject.put(Constants.SUCCESS, false);
            // 更新失败！事务回滚
            throw new RuntimeException();
        }

        return String.valueOf(jsonObject);
    }

    // 按条件查询分类-ajax
    @ApiOperation(value = "按条件查询分类", notes = "按条件查询分类")
    @ResponseBody
    @GetMapping(value = "admin/category/{index}/{count}", produces = "application/json;charset=utf-8")
    public String getCategoryBySearch(@RequestParam(required = false) String category_name/* 分类名称 */,
                                      @PathVariable Integer index/* 页数 */,
                                      @PathVariable Integer count/* 行数 */) throws UnsupportedEncodingException {
        // 移除不必要条件
        if (category_name != null) {
            // 如果为非空字符串则解决中文乱码：URLDecoder.decode(String,"UTF-8");
            category_name = "".equals(category_name) ? null : URLDecoder.decode(category_name, "UTF-8");
        }
        JSONObject object = new JSONObject();
        // 按条件获取第{}页的{}条分类, index + 1, count
        PageUtil pageUtil = new PageUtil(index, count);
        List<Category> categoryList = categoryService.getList(category_name, pageUtil);
        object.put("categoryList", JSONArray.parseArray(JSON.toJSONString(categoryList)));
        // 按条件获取分类总数量
        Integer categoryCount = categoryService.getTotal(category_name);
        object.put("categoryCount", categoryCount);
        // 获取分页信息
        pageUtil.setTotal(categoryCount);
        object.put("totalPage", pageUtil.getTotalPage());
        object.put("pageUtil", pageUtil);
        return String.valueOf(object);
    }

    // 上传分类图片-ajax
    @ApiOperation(value = "上传分类图片", notes = "上传分类图片")
    @ResponseBody
    @PostMapping(value = "admin/uploadCategoryImage", produces = "application/json;charset=utf-8")
    public String uploadCategoryImage(@RequestParam MultipartFile file, HttpSession session) {
        String originalFileName = file.getOriginalFilename();
        // 获取图片原始文件名:  {}, originalFileName
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        // 允许的文件类型字符串
        String allowedExtensions = ".jpg,.jpeg,.png,.gif";
        
        // 如果文件类型不在允许范围内，修改为 .jpg 扩展名
        if (!allowedExtensions.contains(extension)) {
            extension = ".jpg";
        }
        String fileName = UUID.randomUUID() + extension;
        String filePath = session.getServletContext().getRealPath("/") + "res/images/item/categoryPicture/" + fileName;

        // 文件上传路径：{}, filePath
        JSONObject object = new JSONObject();
        try {
            // 文件上传中...
            file.transferTo(new File(filePath));
            logger.info("文件上传完成");
            object.put(Constants.SUCCESS, true);
            object.put("fileName", fileName);
        } catch (IOException e) {
            logger.error("文件上传失败!");
        }
        return String.valueOf(object);
    }

    // 按ID删除分类并返回最新结果-ajax
    @ApiOperation(value = "按ID删除分类并返回最新结果", notes = "按ID删除分类并返回最新结果")
    @ResponseBody
    @GetMapping(value = "admin/category/del/{id}", produces = "application/json;charset=utf-8")
    public String deleteProductById(@PathVariable Integer id) {
        JSONObject object = new JSONObject();
        Category category = categoryService.get(id);
        category.setDel_flag(1);
        boolean yn = categoryService.update(category);
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