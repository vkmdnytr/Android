package com.example.sosyalyazilim.entity.model.data.socket

import com.example.sosyalyazilim.entity.model.RecyclerViewBaseItem


data class ActionItem(var userName:String): RecyclerViewBaseItem(ACTION_TYPE) {
    companion object{
        const val ACTION_TYPE=0
    }
}