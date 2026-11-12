
package com.example.tripjournal.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class Trip(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,
    val destination: String,
    val startDate: Long,
    val endDate: Long,
    val description: String = "",
    val isFavorite: Boolean = false
)
