package com.example.myapplication.database.dao

import androidx.room.*
import com.example.myapplication.model.Blog
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface BlogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(blog: Blog): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg blogs: Blog): Completable

    @Update
    fun update(blog: Blog): Single<Int>

    @Update
    fun update(vararg Blog: Blog): Single<Int>

    @Delete
    fun delete(blog: Blog): Single<Int>

    @Delete
    fun delete(vararg blog: Blog): Single<Int>

    @Query("SELECT * FROM blog")
    fun fetchAll(): Flowable<List<Blog>>

    @Query("SELECT * FROM blog ORDER BY created_at LIMIT 1")
    fun fetchFirst(): Flowable<Blog>
}