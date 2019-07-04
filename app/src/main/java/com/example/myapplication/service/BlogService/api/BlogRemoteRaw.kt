package com.example.myapplication.service.BlogService.api

import com.example.myapplication.model.Blog
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BlogRemoteRaw {
    @POST("blogs/add")
    fun addBlog(@Body blog: Blog): Single<String>

    @GET("blogs")
    fun fetchBlogs(): Single<FetchBlogListResponseSuccess>

}