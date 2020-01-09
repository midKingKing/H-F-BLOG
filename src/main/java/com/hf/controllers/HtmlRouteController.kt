package com.hf.controllers

import com.hf.exception.HfExceptions
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import java.util.regex.Pattern
import javax.servlet.http.HttpServletRequest

@Controller
class HtmlRouteController {

    @RequestMapping(value = ["**.html"])
    fun renderToHtmlPage(request: HttpServletRequest): String {
        val matcher = pattern.matcher(request.requestURI)
        if (matcher.find()) {
            return matcher.group(1)
        }
        throw HfExceptions.invalidRoute()
    }

    companion object {
        private val pattern = Pattern.compile("^/(.*)\\.html")
    }
}
