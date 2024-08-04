package com.pal.chattingapplication.data

open class Event<out T> (val content: T) {
    var hasbeenHandled = false
    fun getContentOrNull(): T?{
        return if (hasbeenHandled) null
        else{
            hasbeenHandled = true
            content
        }
    }
}