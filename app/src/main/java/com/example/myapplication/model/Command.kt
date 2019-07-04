package com.example.myapplication.model

import androidx.room.Entity
import com.example.myapplication.util.Common
import java.util.*

@Entity
open class Command(
    var aggregateId: String,
    var columnType: String,
    var payload: String,
    var eventType: String,
    var eventTrigger:String,
    var createdAt: Date = Date(),
    var eventId: String = Common.generateRandomUUID(),
    var serverSync: Boolean = false
)