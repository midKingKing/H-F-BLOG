package com.hf.controllers;

import com.hf.exception.HfExceptions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fjm on 2017/12/22.
 */
@Controller
public class HtmlController {
    private static Pattern pattern = Pattern.compile("^/(.*)\\.html");

    @RequestMapping(value = "**.html")
    public String renderToHtmlPage(HttpServletRequest request) {
        Matcher matcher = pattern.matcher(request.getRequestURI());
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw HfExceptions.Companion.invalidRoute();
    }
}
