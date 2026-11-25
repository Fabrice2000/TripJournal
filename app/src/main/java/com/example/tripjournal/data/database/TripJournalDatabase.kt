package com.example.tripjournal.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tripjournal.data.database.dao.PlaceDao
import com.example.tripjournal.data.database.dao.TripDao
import com.example.tripjournal.data.database.dao.VisitDao
import com.example.tripjournal.data.database.entity.Place
import com.example.tripjournal.data.database.entity.Trip
import com.example.tripjournal.data.database.entity.Visit

@Database(
    entities = [Trip::class, Place::class, Visit::class],
    version = 1,
    exportSchema = false
)
abstract class TripJournalDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun placeDao(): PlaceDao
    abstract fun visitDao(): VisitDao

    companion object {
        @Volatile
        private var instance: TripJournalDatabase? = null

        fun get(context: Context): TripJournalDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    TripJournalDatabase::class.java,
                    "tripjournal.db"
                ).build().also { instance = it }
            }
    }
}
