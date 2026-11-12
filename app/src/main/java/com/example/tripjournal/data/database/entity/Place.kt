
package com.example.tripjournal.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class Place(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,
    val city: String,
    val country: String,
    val description: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null
)
