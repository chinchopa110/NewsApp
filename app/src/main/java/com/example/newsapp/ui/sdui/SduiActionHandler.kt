package com.example.newsapp.ui.sdui

import com.example.newsapp.domain.model.Action

fun interface SduiActionHandler {
    fun handle(action: Action)
}
