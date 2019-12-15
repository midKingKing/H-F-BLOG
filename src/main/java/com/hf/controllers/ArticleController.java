package com.hf.controllers;

import com.hf.dto.Article;
import com.hf.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by fjm on 2018/1/11.
 */
@Controller
public class ArticleController {

    @Autowired
    private IArticleService articleService;

    @RequestMapping(value = {"/article/publish"},method = RequestMethod.POST)
    @ResponseBody
    public Article publishArticle(HttpServletRequest request, Article article){
        articleService.insertSelective(article);
        return article;
    }

}
