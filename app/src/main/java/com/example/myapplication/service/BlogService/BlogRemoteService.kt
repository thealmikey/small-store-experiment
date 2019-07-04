package com.example.myapplication.service.BlogService

import android.util.Log
import com.example.myapplication.model.Blog
import com.example.myapplication.service.BlogService.api.BlogRemoteRaw
import io.reactivex.Single

class BlogRemoteService(var blogRemoteRaw: BlogRemoteRaw) {

    fun addBlog(blog: Blog): Single<String> {
        return blogRemoteRaw.addBlog(blog)
    }

    fun fetchBlogs(): Single<List<Blog>> {
        return blogRemoteRaw.fetchBlogs().map {
            it.data!!
        }
    }
}