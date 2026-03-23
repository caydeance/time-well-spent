package com.example.timewellspent

import java.util.Date



data class Session(
    var name: String = "Random Name",
    var date: Date = Date(),
    var elapsedTime: Int = 0, // time in minutes
    var heartRate: Int = 0,
    var emotion: String = EMOTION.NEUTRAL.name,
    var ownerId: String? = null,
    var objectId: String? = null  // null so that new objects receive an id from the server
) {
    enum class EMOTION(var emoji: String) {
        HAPPY ("\uD83D\uDE03"),
        SAD ("\u2639\uFE0F"),
        FRUSTRATED("\uD83D\uDE2C"),
        ANGRY("\uD83E\uDD2C"),
        NEUTRAL("\uD83D\uDE10")
    }
}
