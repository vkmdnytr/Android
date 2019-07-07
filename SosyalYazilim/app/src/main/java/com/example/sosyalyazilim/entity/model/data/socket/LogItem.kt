package com.example.sosyalyazilim.entity.model.data.socket

import com.example.sosyalyazilim.entity.model.RecyclerViewBaseItem

data class LogItem(var message:String): RecyclerViewBaseItem(LOG_TYPE) {
    companion object{
        const val LOG_TYPE=1
    }
}