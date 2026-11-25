package com.example.tripjournal

import android.app.Application
import com.example.tripjournal.data.settings.SettingsManager

class TripJournalApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Applique le thème sauvegardé dès l'initialisation de l'application
        SettingsManager.getInstance(this).applySavedTheme()
    }
}
