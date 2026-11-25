package com.example.tripjournal.ui.trips

import com.example.tripjournal.data.database.TripJournalDatabase
import com.example.tripjournal.data.database.entity.Trip
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object TripDates {
    private fun format(pattern: String, millis: Long): String =
        SimpleDateFormat(pattern, Locale.FRANCE)
            .apply { timeZone = TimeZone.getTimeZone("UTC") }
            .format(Date(millis))

    fun day(millis: Long): String = format("EEEE d MMMM", millis)
    fun short(millis: Long): String = format("d MMM yyyy", millis)
}

/** TEMPORAIRE : à retirer quand la liste des voyages ouvrira TripDetailActivity. */
object DemoData {
    suspend fun firstOrCreateTrip(db: TripJournalDatabase): Long {
        val existing = db.tripDao().getAllTrips().first().firstOrNull()
        if (existing != null) return existing.id
        return db.tripDao().insertTrip(
            Trip(
                name = "Week-end à Lisbonne",
                destination = "Lisbonne, Portugal",
                startDate = utcDay(2026, 12, 4),
                endDate = utcDay(2026, 12, 6),
                description = "Voyage de démonstration"
            )
        )
    }

    private fun utcDay(y: Int, m: Int, d: Int): Long =
        Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            clear(); set(y, m - 1, d)
        }.timeInMillis
}