
package com.example.tripjournal.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "visits",
    foreignKeys = [
        ForeignKey(
            entity = Trip::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Place::class,
            parentColumns = ["id"],
            childColumns = ["placeId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index("tripId"),
        Index("placeId")
    ]
)
data class Visit(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val tripId: Long,
    val placeId: Long,
    val visitDate: Long,
    val notes: String = "",
    val isFavorite: Boolean = false
)
