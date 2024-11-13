package com.example.shoppinglist

import android.app.Application
import com.google.firebase.FirebaseApp

class ShopMe : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}