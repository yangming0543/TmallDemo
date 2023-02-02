package com.xq.tmall.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.xq.tmall.controller.BaseController;
import com.xq.tmall.entity.Admin;
import com.xq.tmall.service.AdminService;
import com.xq.tmall.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * 后台管理-登录页
 */
@Controller
public class AdminLoginController extends BaseController {
    @Autowired
    private AdminService adminService;

    //转到后台管理-登录页
    @RequestMapping("admin/login")
    public String goToPage() {
        //转到后台管理-登录页
        return "admin/loginPage";
    }

    //登陆验证-ajax
    @ResponseBody
    @PostMapping(value = "admin/login/doLogin", produces = "application/json;charset=utf-8")
    public String checkLogin(HttpSession session, @RequestParam String username, @RequestParam String password) {
        //管理员登录验证
        Integer admin = adminService.login(username, password);

        JSONObject object = new JSONObject();
        if (admin == null) {
            //登录验证失败
            object.put(Constants.SUCCESS, false);
        } else {
            //登录验证成功，管理员ID传入会话
            session.setAttribute(Constants.ADMIN_ID, admin);
            object.put(Constants.SUCCESS, true);
        }

        return String.valueOf(object);
    }

    //获取管理员头像路径-ajax
    @ResponseBody
    @GetMapping(value = "admin/login/profile_picture", produces = "application/json;charset=utf-8")
    public String getAdminProfilePicture(@RequestParam String username) {
        //根据用户名获取管理员头像路径
        Admin admin = adminService.get(username, null);
        JSONObject object = new JSONObject();
        if (admin == null) {
            //未找到头像路径
            object.put(Constants.SUCCESS, false);
        } else {
            //成功获取头像路径
            object.put(Constants.SUCCESS, true);
            object.put("srcString", admin.getAdmin_profile_picture_src());
        }
        return String.valueOf(object);
    }
}
