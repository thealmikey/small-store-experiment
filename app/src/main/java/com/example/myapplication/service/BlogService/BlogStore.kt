package com.example.myapplication.service.BlogService

import com.example.myapplication.model.Blog
import com.nytimes.android.external.store3.base.Fetcher
import com.nytimes.android.external.store3.base.impl.BarCode
import com.nytimes.android.external.store3.base.impl.MemoryPolicy
import com.nytimes.android.external.store3.base.impl.StalePolicy
import com.nytimes.android.external.store3.base.impl.room.StoreRoom
import com.nytimes.android.external.store3.base.room.RoomPersister
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class BlogStore(val blogRemoteService: BlogRemoteService, val blogLocalService: BlogLocalService) {

    var fetcher = Fetcher<List<Blog>, BarCode> {
        blogRemoteService.fetchBlogs()
    }

    val persister = object : RoomPersister<List<Blog>, List<Blog>, BarCode> {

        override fun read(barCode: BarCode): Observable<List<Blog>> {
            return blogLocalService.fetchAll().toObservable()
        }

        override fun write(barCode: BarCode, blogList: List<Blog>) {
            blogLocalService.addBlogs(blogList)
        }
    }

    var memoryPolicy: MemoryPolicy = MemoryPolicy
        .builder()
        .setExpireAfterWrite(5)
        .setExpireAfterTimeUnit(TimeUnit.SECONDS)
        .build()

    var store = StoreRoom.from(fetcher, persister, StalePolicy.REFRESH_ON_STALE, memoryPolicy)

    fun getBlogs(): Observable<List<Blog>> {
        store.clear()
        return store.fetch(BarCode.empty())
    }
}