package com.example.sosyalyazilim.entity.model.sealed

sealed class Results<out T : Any> {
    class Success<out T : Any>(val data: T) : Results<T>()
    class Error<out T : Any>(val data: T) : Results<T>()
}