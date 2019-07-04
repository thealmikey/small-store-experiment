package com.example.myapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.util.Common
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity
data class Blog(
    @PrimaryKey(autoGenerate = false)
    var blogUuid:String = Common.generateRandomUUID(),
    var title: String,
    var body: String,
    @SerializedName("created_at")
    var created_at: Date? = Date(),
    @SerializedName("updated_at")
    var updated_at: Date? = Date(),
    var localDbSync: Boolean? = false
)