package com.hf.controllers.advice

import com.hf.pojo.ResponseData
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.NoHandlerFoundException
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class ExceptionAdvice {
    @ExceptionHandler(value = [Exception::class])
    @ResponseBody
    fun exceptionHandler(exception: Exception, request: HttpServletRequest): Any {
        val message = exception.message
        log.error(message, exception)
        return if (isAjaxRequest(request) || ServletFileUpload.isMultipartContent(request)) ResponseData(false).apply { this.message = message } else ModelAndView("500").apply { addObject("message", message) }
    }

    @ExceptionHandler(value = [NoHandlerFoundException::class])
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun noMapping(request: HttpServletRequest, ex: Exception?): ModelAndView {
        log.info("404 not found:" + request.contextPath, ex)
        return ModelAndView("404")
    }

    companion object {
        private const val X_REQUESTED_WIDTH = "X-Requested-With"
        private const val XML_HTTP_REQUEST = "XMLHttpRequest"
        private val log = LoggerFactory.getLogger(ExceptionAdvice::class.java)

        private fun isAjaxRequest(request: HttpServletRequest): Boolean {
            val xr = request.getHeader(X_REQUESTED_WIDTH)
            return XML_HTTP_REQUEST.equals(xr, ignoreCase = true)
        }
    }
}