package com.hf.dto

import java.io.Serializable
import java.util.*
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class Article : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    var articleId: Long? = null

    @Column(name = "user_id")
    var userId: Long? = null

    @Column(name = "group_id")
    var groupId: Long? = null

    @Column(name = "article_name")
    var articleName: String? = null

    @Column(name = "creation_date")
    var creationDate: Date? = null

    @Column(name = "last_update_date")
    var lastUpdateDate: Date? = null

    @Column(name = "article_content")
    var articleContent: String? = null
}