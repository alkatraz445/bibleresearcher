package com.mandk.biblereasercher.utils

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun observe(): Flow<Status>

    fun isConnected(): Boolean

    enum class Status(val message: String) {
        Available("Back online"),
        Unavailable("No internet connection"),
        Losing("Internet connection lost"),
        Lost("Internet connection lost")
    }
}