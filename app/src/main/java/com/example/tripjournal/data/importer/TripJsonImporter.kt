package com.example.tripjournal.data.importer

import android.content.Context
import android.util.Log
import com.example.tripjournal.data.database.dao.TripDao
import com.example.tripjournal.domain.ImportResult
import com.example.tripjournal.domain.TripImport
import com.example.tripjournal.domain.TripRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.json.JSONArray

class TripJsonImporter(
    private val context: Context,
    private val tripDao: TripDao
) {

    /** Importe le JSON une seule fois (au premier lancement). Retourne null si déjà fait. */
    suspend fun importIfNeeded(): ImportResult? = withContext(Dispatchers.IO) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        if (prefs.getBoolean(KEY_DONE, false)) return@withContext null

        val json = context.assets.open(FILE).bufferedReader().use { it.readText() }
        val result = TripImport.prepare(parse(json), tripDao.getAllTrips().first())
        result.toInsert.forEach { tripDao.insertTrip(it) }

        prefs.edit().putBoolean(KEY_DONE, true).apply()
        Log.i(TAG, "Import : ${result.toInsert.size} ajoutés, ${result.invalid} invalides, ${result.duplicates} doublons")
        result
    }

    private fun parse(json: String): List<TripRecord> {
        val array = JSONArray(json)
        return (0 until array.length()).map { i ->
            val o = array.getJSONObject(i)
            TripRecord(
                name = o.optString("name"),
                destination = o.optString("destination"),
                startDate = o.optString("startDate"),
                endDate = o.optString("endDate"),
                description = o.optString("description")
            )
        }
    }

    companion object {
        private const val TAG = "TripJsonImporter"
        private const val FILE = "trips_seed.json"
        private const val PREFS = "trip_import"
        private const val KEY_DONE = "seed_imported"
    }
}