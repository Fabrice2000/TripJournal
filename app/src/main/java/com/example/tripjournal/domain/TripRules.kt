package com.example.tripjournal.domain

import com.example.tripjournal.data.database.entity.Trip

object TripRules {

    private const val DAY_MS = 24L * 60 * 60 * 1000

    /** Numéro du jour (UTC) correspondant à une date en millisecondes. */
    private fun day(millis: Long): Long = Math.floorDiv(millis, DAY_MS)

    /** Un voyage doit avoir un nom, une destination, et une fin qui n'est pas avant le début. */
    fun isValid(trip: Trip): Boolean =
        trip.name.isNotBlank() &&
                trip.destination.isNotBlank() &&
                day(trip.endDate) >= day(trip.startDate)

    /** Doublon = un autre voyage avec le même nom (sans tenir compte des majuscules) et le même jour de départ. */
    fun isDuplicate(trip: Trip, existing: List<Trip>): Boolean =
        existing.any {
            it.id != trip.id &&
                    it.name.trim().equals(trip.name.trim(), ignoreCase = true) &&
                    day(it.startDate) == day(trip.startDate)
        }
}