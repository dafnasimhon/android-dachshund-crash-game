package com.example.assighment1

import android.app.Application
import com.example.assighment1.logic.RecordListManager
import com.example.assighment1.utilities.SharedPreferencesManager
import com.example.assighment1.utilities.SignalManager

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        SharedPreferencesManager.init(this)

        RecordListManager.init(this)

        SignalManager.init(this)
    }
}