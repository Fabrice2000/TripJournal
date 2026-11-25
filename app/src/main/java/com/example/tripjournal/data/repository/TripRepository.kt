package com.example.tripjournal.data.repository

import com.example.tripjournal.data.database.dao.TripDao
import com.example.tripjournal.data.database.entity.Trip
import com.example.tripjournal.domain.TripRules
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

sealed class TripSaveResult {
    data class Saved(val id: Long) : TripSaveResult()
    object Invalid : TripSaveResult()
    object Duplicate : TripSaveResult()
}

class TripRepository(private val tripDao: TripDao) {

    fun observeTrips(): Flow<List<Trip>> = tripDao.getAllTrips()

    fun observeTrip(id: Long): Flow<Trip?> = tripDao.getTripById(id)

    suspend fun save(trip: Trip): TripSaveResult {
        val clean = trip.copy(
            name = trip.name.trim(),
            destination = trip.destination.trim(),
            description = trip.description.trim()
        )
        if (!TripRules.isValid(clean)) return TripSaveResult.Invalid
        if (TripRules.isDuplicate(clean, tripDao.getAllTrips().first())) return TripSaveResult.Duplicate

        val id = if (clean.id == 0L) {
            tripDao.insertTrip(clean)
        } else {
            tripDao.updateTrip(clean)
            clean.id
        }
        return TripSaveResult.Saved(id)
    }

    /** Les visites du voyage sont supprimées automatiquement (clé étrangère CASCADE). */
    suspend fun delete(trip: Trip) = tripDao.deleteTrip(trip)
}