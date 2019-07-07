package com.example.sosyalyazilim.entity.model.data.socket

import com.example.sosyalyazilim.entity.model.RecyclerViewBaseItem

data class MessageItem(var userName:String,var message: String): RecyclerViewBaseItem(MESSAGE_TYPE) {
    companion object{
        const val MESSAGE_TYPE=2
    }
}