package com.hf.controllers;

import com.hf.dto.User;
import com.hf.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by fjm on 2017/12/25.
 */
@Controller
public class LoginController {

    private static final String VIEW_LOGIN = "/login";
    private static final String VIEW_INDEX = "/index";

    @Autowired
    private IUserService userService;

    /**
     * 登录操作(表单登录为post请求)
     */
    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request, String username, String password) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            userService.loginByUsernameAndPassword(username, password);
            modelAndView.setViewName("redirect:" + VIEW_INDEX + ".html");
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.setViewName(VIEW_LOGIN);
            modelAndView.addObject("message", "账号或密码错误");
        }
        return modelAndView;
    }

    /**
     * 进入登录页面
     */
    @RequestMapping(value = {"/login", "/login.html"}, method = RequestMethod.GET)
    public String logOut(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user == null || user.getUsername() == null) {//当session中的user为空,跳转到login视图
                return VIEW_LOGIN;
            } else if (request.getParameter("logout") != null) {//当url为登出时,则清除当前subject的信息,跳转到login视图
                return "redirect:" + VIEW_INDEX + ".html";
            } else if (request.getParameter("changeUser") != null) {
                return VIEW_LOGIN;
            }
            return "redirect:" + VIEW_INDEX + ".html";
        }
        return VIEW_LOGIN; //如果session为null，返回login
    }


    /**
     * 由于使用他人的qq互联appid和secret，所以回调地址只能。。。
     */
    @RequestMapping(value = "/login/qq")
    public String redirectProvider(HttpServletRequest request) {
        return "redirect:" + "/signin/qq?" + request.getQueryString();
    }
}
