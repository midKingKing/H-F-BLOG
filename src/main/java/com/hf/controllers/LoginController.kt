package com.hf.controllers

import com.hf.exception.HfExceptions
import com.hf.logging.Logging
import com.hf.service.UserService
import com.hf.util.SessionUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest

@Controller
class LoginController @Autowired constructor(private val userService: UserService) {

    /**
     * 登录操作(表单登录为post请求)
     */
    @RequestMapping(value = ["/login"], method = [RequestMethod.POST])
    fun login(username: String, password: String): ModelAndView {
        return ModelAndView().apply {
            try {
                userService.loginByUsernameAndPassword(username, password)
                viewName = "redirect:$VIEW_INDEX.html"
            } catch (e: Exception) {
                log.error(e.message, e)
                viewName = VIEW_LOGIN
                addObject("message", "账号或密码错误")
            }
        }
    }

    /**
     * 登出并进入登录页面
     */
    @RequestMapping(value = ["/login"], params = ["logout"], method = [RequestMethod.GET])
    fun logOut(request: HttpServletRequest): String {
        request.cookies.find { it.name == "hf-session" }?.value?.apply {
            userService.logoutBySessionId(this)
        }
        return VIEW_LOGIN //如果session为null，返回login
    }

    companion object : Logging() {
        private const val VIEW_LOGIN = "/login"
        private const val VIEW_INDEX = "/index"
    }
}
