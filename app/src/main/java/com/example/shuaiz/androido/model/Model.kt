package com.example.shuaiz.androido.model


data class UIModel(val name: String, val alias: String, val type: String)
data class UI(val activities: List<UIModel>)