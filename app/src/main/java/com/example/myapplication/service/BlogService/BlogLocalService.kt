package com.example.myapplication.service.BlogService

import com.example.myapplication.database.dao.BlogDao
import com.example.myapplication.model.Blog
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class BlogLocalService(var blogDao: BlogDao) {

    fun fetchAll(): Flowable<List<Blog>> {
        return blogDao.fetchAll()
    }
    fun addBlog(blog: Blog): Single<Long> {
        return blogDao.insert(blog)
    }

    fun addBlogs(blog: List<Blog>): Completable {
        return blogDao.insertAll(*blog.toTypedArray())
    }

    fun removeBlog(blog: Blog): Single<Int> {
        return blogDao.delete(blog)
    }

}