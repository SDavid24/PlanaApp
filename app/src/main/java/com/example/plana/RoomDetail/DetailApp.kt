package com.example.plana.RoomDetail

import android.app.Application

class DetailApp : Application() {
    val db  by lazy {
        DetailDatabase.getInstance(this)
    }
}