package com.example.myapplication.database.dao

import androidx.room.*
import com.example.myapplication.model.Command
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface CommandDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(command: Command): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg command:Command): Completable

    @Update
    fun update(command: Command): Single<Int>

    @Update
    fun update(vararg commands:Command): Single<Int>

    @Delete
    fun delete(command: Command): Single<Int>

    @Delete
    fun delete(vararg commands:Command): Single<Int>

    @Query("SELECT * FROM command")
    fun getAll(): Flowable<List<Command>>

    @Query("SELECT * FROM command WHERE aggregateId=:aggregateId")
    fun findAllByAggregateId(aggregateId:String): Flowable<List<Command>>

    @Query("SELECT * FROM command ORDER BY createdAt LIMIT 1")
    fun fetchFirst(): Flowable<Command>
}