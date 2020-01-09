package com.hf.controllers

import com.hf.dto.Article
import com.hf.service.ArticleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import javax.servlet.http.HttpServletRequest

@Controller
class ArticleController @Autowired constructor(private val articleService: ArticleService) {

    @RequestMapping(value = ["/article/publish"], method = [RequestMethod.POST])
    @ResponseBody
    fun publishArticle(request: HttpServletRequest, article: Article): Article {
        articleService.insertSelective(article)
        return article
    }

}
