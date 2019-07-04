package com.example.myapplication.repository.BlogRepository

import com.example.myapplication.service.BlogService.BlogLocalService
import com.example.myapplication.service.BlogService.BlogRemoteService

class BlogRepositoryImpl(var blogLocalService: BlogLocalService, var blogRemoteService: BlogRemoteService){

}