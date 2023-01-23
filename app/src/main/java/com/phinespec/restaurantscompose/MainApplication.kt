package com.phinespec.restaurantscompose

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {

    init { app = this }

    companion object {
        private lateinit var app: MainApplication
        fun getAppContext(): Context = app.applicationContext
    }

}