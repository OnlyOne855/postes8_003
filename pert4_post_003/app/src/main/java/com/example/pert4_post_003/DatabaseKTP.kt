package com.example.pert4_post_003

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pert4_post_003.KTPDao

@Database(entities = [KTP::class], version = 1, exportSchema = false)
abstract class DatabaseKTP : RoomDatabase() {
    abstract fun ktpdao(): KTPDao

    companion object {
        @Volatile
        private var INSTANCE: DatabaseKTP? = null

        fun getDatabase(context: Context): DatabaseKTP {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseKTP::class.java,
                    "db_ktp"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}