package com.xq.tmall.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xq.tmall.controller.BaseController;
import com.xq.tmall.entity.Admin;
import com.xq.tmall.entity.OrderGroup;
import com.xq.tmall.service.AdminService;
import com.xq.tmall.service.ProductOrderService;
import com.xq.tmall.service.ProductService;
import com.xq.tmall.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 后台管理-主页
 */
@Api(tags = "后台管理-主页")
@Controller
public class AdminHomeController extends BaseController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private ProductOrderService productOrderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    // 转到后台管理-主页
    @ApiOperation(value = "转到后台管理-主页", notes = "转到后台管理-主页")
    @GetMapping(value = "admin")
    public String goToPage(HttpSession session, Map<String, Object> map) throws ParseException {
        // 检查管理员权限
        Object adminId = checkAdmin(session);
        if (adminId == null) {
            return "redirect:/admin/login";
        }
        // 获取管理员信息
        Admin admin = adminService.get(null, Integer.parseInt(adminId.toString()));
        map.put("admin", admin);
        // 获取统计信息
        Integer productTotal = productService.getTotal(null, new Byte[]{0, 2});
        Integer userTotal = userService.getTotal(null);
        Integer orderTotal = productOrderService.getTotal(null, new Byte[]{3});
        // 获取图表信息
        map.put("jsonObject", getChartData(null, null));
        map.put("productTotal", productTotal);
        map.put("userTotal", userTotal);
        map.put("orderTotal", orderTotal);
        // 转到后台管理-主页
        return "admin/homePage";
    }

    // 转到后台管理-home主页-ajax
    @ApiOperation(value = "转到后台管理-home主页", notes = "转到后台管理-home主页")
    @GetMapping(value = "admin/home")
    public String goToPageByAjax(HttpSession session, Map<String, Object> map) throws ParseException {
        // 检查管理员权限
        Object adminId = checkAdmin(session);
        if (adminId == null) {
            return "admin/include/loginMessage";
        }
        // 获取管理员信息
        Admin admin = adminService.get(null, Integer.parseInt(adminId.toString()));
        map.put("admin", admin);
        // 获取统计信息
        Integer productTotal = productService.getTotal(null, new Byte[]{0, 2});
        Integer userTotal = userService.getTotal(null);
        Integer orderTotal = productOrderService.getTotal(null, new Byte[]{3});
        // 获取图表信息
        map.put("jsonObject", getChartData(null, null));
        map.put("productTotal", productTotal);
        map.put("userTotal", userTotal);
        map.put("orderTotal", orderTotal);
        // 转到后台管理-主页-ajax方式
        return "admin/homeManagePage";
    }

    // 按日期查询图表数据-ajax
    @ApiOperation(value = "按日期查询图表数据", notes = "按日期查询图表数据")
    @ResponseBody
    @GetMapping(value = "admin/home/charts", produces = "application/json;charset=utf-8")
    public String getChartDataByDate(@RequestParam(required = false) String beginDate, @RequestParam(required = false) String endDate) throws ParseException {
        if (StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return getChartData(simpleDateFormat.parse(beginDate), simpleDateFormat.parse(endDate)).toJSONString();
        } else {
            return getChartData(null, null).toJSONString();
        }
    }

    // 获取图表的JSON数据
    private JSONObject getChartData(Date beginDate, Date endDate) throws ParseException {
        JSONObject jsonObject = new JSONObject();
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        SimpleDateFormat timeSpecial = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
        if (beginDate == null || endDate == null) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -7);
            beginDate = time.parse(time.format(cal.getTime()));
            cal = Calendar.getInstance();
            endDate = cal.getTime();
        } else {
            beginDate = time.parse(time.format(beginDate));
            endDate = timeSpecial.parse(time.format(endDate) + " 23:59:59");
        }
        String[] dateStr = new String[7];
        SimpleDateFormat time2 = new SimpleDateFormat("yyyy/MM/dd", Locale.UK);
        // 获取时间段数组
        for (int i = 0; i < dateStr.length; i++) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(beginDate);
            cal.add(Calendar.DATE, i);
            dateStr[i] = time2.format(cal.getTime());
        }
        // 获取总交易额订单列表
        List<OrderGroup> orderGroupList = productOrderService.getTotalByDate(beginDate, endDate);
        // 根据订单状态分类
        // 总交易订单数组
        int[] orderTotalArray = new int[7];
        // 未付款订单数组
        int[] orderUnpaidArray = new int[7];
        // 未发货订单叔祖
        int[] orderNotShippedArray = new int[7];
        // 未确认订单数组
        int[] orderUnconfirmedArray = new int[7];
        // 交易成功数组
        int[] orderSuccessArray = new int[7];
        // 交易关闭数组
        int[] orderCloseArray = new int[7];
        for (OrderGroup orderGroup : orderGroupList) {
            // 日期比较
            for (int j = 0; j < dateStr.length; j++) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(orderGroup.getProductOrder_pay_date());
                String format = time2.format(cal.getTime());
                if (dateStr[j].equals(format)) {
                    switch (orderGroup.getProductOrder_status()) {
                        case 0:
                            orderUnpaidArray[j] = orderGroup.getProductOrder_count();
                            break;
                        case 1:
                            orderNotShippedArray[j] = orderGroup.getProductOrder_count();
                            break;
                        case 2:
                            orderUnconfirmedArray[j] = orderGroup.getProductOrder_count();
                            break;
                        case 3:
                            orderSuccessArray[j] = orderGroup.getProductOrder_count();
                            break;
                        case 4:
                            orderCloseArray[j] = orderGroup.getProductOrder_count();
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        // 获取总交易订单数组
        for (int i = 0; i < dateStr.length; i++) {
            orderTotalArray[i] = orderUnpaidArray[i] + orderNotShippedArray[i] + orderUnconfirmedArray[i] + orderSuccessArray[i] + orderCloseArray[i];
        }
        // 返回结果集map
        jsonObject.put("orderTotalArray", JSON.parseArray(JSON.toJSONString(orderTotalArray)));
        jsonObject.put("orderUnpaidArray", JSON.parseArray(JSON.toJSONString(orderUnpaidArray)));
        jsonObject.put("orderNotShippedArray", JSON.parseArray(JSON.toJSONString(orderNotShippedArray)));
        jsonObject.put("orderUnconfirmedArray", JSON.parseArray(JSON.toJSONString(orderUnconfirmedArray)));
        jsonObject.put("orderSuccessArray", JSON.parseArray(JSON.toJSONString(orderSuccessArray)));
        jsonObject.put("orderCloseArray", JSON.parseArray(JSON.toJSONString(orderCloseArray)));
        jsonObject.put("dateStr", JSON.parseArray(JSON.toJSONString(dateStr)));
        return jsonObject;
    }
}