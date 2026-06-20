package com.manta.whatsapp

import android.app.Application
import timber.log.Timber

class MantaXApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        StorageManager.init(this)
    }
}