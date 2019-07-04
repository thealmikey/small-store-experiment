package com.example.myapplication.service.BlogService.api

import com.example.myapplication.model.Blog

data class FetchBlogListResponseSuccess(
    var message: String? = "",
    var data: List<Blog>
)