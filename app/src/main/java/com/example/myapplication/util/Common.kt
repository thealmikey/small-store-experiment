package com.example.myapplication.util

import java.util.*

object Common {


    fun generateRandomUUID(): String {
        return UUID.randomUUID().toString()
    }

    fun <T> chooseRandomFromList(dataList: MutableList<T>): T {
        var listSize = dataList.size + 1
        var chosen: Int = (Math.random() * listSize).toInt()
        if (chosen <= 0) {
            chosen = 0
            var chosenData = dataList[chosen]
            return chosenData
        }
        var chosenData = dataList[chosen - 1]
        return chosenData
    }

    var userRoles = mutableListOf<String>("agent", "asschief", "agent", "landlord", "has_bodaboda")


    var messageTypes =
        mutableListOf<String>("alert", "notice", "important", "status", "warning", "info", "invite", "general")

    var messageMedium = mutableListOf<String>("web", "sms", "web-sms")

    var sendStatus = mutableListOf<String>("unsent", "sent", "received")

    var listingTypes = mutableListOf<String>("realestate", "bodaboda")

    var readStatus = mutableListOf<String>("read", "unread")

    var randomMessageType = chooseRandomFromList(messageTypes)
    var randomUserRole = chooseRandomFromList(userRoles)
    var randomSendStatus = chooseRandomFromList(sendStatus)
    var randomReadStatus = chooseRandomFromList(readStatus)


}