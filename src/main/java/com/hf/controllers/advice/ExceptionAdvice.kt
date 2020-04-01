package com.hf.controllers.advice

import com.hf.logging.Logging
import com.hf.pojo.ResponseData
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class ExceptionAdvice {
    @ExceptionHandler(value = [Exception::class])
    @ResponseBody
    fun exceptionHandler(exception: Exception, request: HttpServletRequest): Any {
        val message = exception.message
        log.error(message, exception)
        return if (isAjaxRequest(request) || ServletFileUpload.isMultipartContent(request)) ResponseData(success = false, message = message) else ModelAndView("500").apply { addObject("message", message) }
    }

    companion object: Logging() {
        private const val X_REQUESTED_WIDTH = "X-Requested-With"
        private const val XML_HTTP_REQUEST = "XMLHttpRequest"

        private fun isAjaxRequest(request: HttpServletRequest): Boolean {
            val xr = request.getHeader(X_REQUESTED_WIDTH)
            return XML_HTTP_REQUEST.equals(xr, ignoreCase = true)
        }
    }
}