package com.pef.mendelu.novotnysamuel.spendradar

import android.app.Application
import com.google.android.gms.maps.MapsInitializer
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SpendRadarApp : Application(){
    override fun onCreate() {
        super.onCreate()
        MapsInitializer.initialize(applicationContext)
    }
}