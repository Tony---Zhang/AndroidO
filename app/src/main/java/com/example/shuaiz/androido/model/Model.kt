package com.example.shuaiz.androido.model


data class Activity(val name: String, val alias: String, val type: String)
data class UI(val activity: List<Activity>)