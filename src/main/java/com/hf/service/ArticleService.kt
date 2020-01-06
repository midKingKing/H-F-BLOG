package com.hf.service

import com.hf.dto.Article
import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.parser.ParserEmulationProfile
import com.vladsch.flexmark.util.options.MutableDataSet
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
open class ArticleService : BaseService<Article>() {
    fun markDown2html(article: Article): String {
        val options = MutableDataSet().apply {
            setFrom(ParserEmulationProfile.MARKDOWN)
            set(Parser.EXTENSIONS, listOf(TablesExtension.create()))
        }
        val parser = Parser.builder(options).build()
        val renderer = HtmlRenderer.builder(options).build()
        val document = parser.parse(article.articleContent)
        return renderer.render(document)
    }

}
