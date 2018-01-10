package com.hf.controllers;

import com.hf.pojo.ResponseData;
import org.apache.ibatis.ognl.OgnlException;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by fjm on 2018/1/9.
 */
@ControllerAdvice
public class ExceptionController {

    public static final String X_REQUESTED_WIDTH = "X-Requested-With";
    public static final String XML_HTTP_REQUEST = "XMLHttpRequest";

    protected final static Logger log = LoggerFactory.getLogger(ExceptionController.class);

//    @ExceptionHandler(value = {IOException.class, RuntimeException.class})
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ResponseBody
//    public ModelAndView handException(HttpServletRequest request,Exception ex){
//        log.info("Exception " + ex.getMessage());
//        ResponseData rs = new ResponseData(false);
//        rs.setMessage(ex.getMessage());
//        return new ModelAndView("500");
//    }

    @ExceptionHandler(value = { Exception.class })
    public Object exceptionHandler(Exception exception, HttpServletRequest request) {
        String message = exception.getMessage();
        log.error(message, exception);
        if (isAjaxRequest(request) || isAjaxRequest(request) || ServletFileUpload.isMultipartContent(request)) {
            ResponseData res = new ResponseData(false);
            res.setMessage(message);
            return res;
        } else {
            ModelAndView view = new ModelAndView("500");
            view.addObject("message", message);
            return view;
        }
    }

    @ExceptionHandler(value = {NoHandlerFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView noMapping(HttpServletRequest request,Exception ex) {
        log.info("404 not found:" + request.getContextPath(), ex);
        return new ModelAndView("404");
    }


    /**
     * 判断是否ajax请求.
     *
     * @param request
     *            HttpServletRequest
     * @return 是否ajax请求.
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String xr = request.getHeader(X_REQUESTED_WIDTH);
        return (xr != null && XML_HTTP_REQUEST.equalsIgnoreCase(xr));
    }

}
