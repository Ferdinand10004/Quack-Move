package com.quackmove.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [LocalTripEntity::class],
    version = 1,
    exportSchema = false
)
abstract class QuackMoveDatabase : RoomDatabase() {

    abstract fun localTripDao(): LocalTripDao

    companion object {

        @Volatile
        private var instance: QuackMoveDatabase? = null

        fun getDatabase(
            context: Context
        ): QuackMoveDatabase {

            return instance ?: synchronized(this) {

                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    QuackMoveDatabase::class.java,
                    "quackmove_local.db"
                ).build().also { database ->
                    instance = database
                }
            }
        }
    }
}
