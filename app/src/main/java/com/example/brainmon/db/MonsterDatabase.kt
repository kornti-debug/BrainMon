package com.example.brainmon.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [MonsterEntity::class],
    version = 1
)
abstract class MonsterDatabase : RoomDatabase() {

    abstract fun monsterDao(): MonsterDao

    companion object {
        @Volatile
        private var Instance: MonsterDatabase? = null

        fun getDatabase(context: Context): MonsterDatabase {
            return Instance ?: synchronized(this) {
                val instance = Room
                    .databaseBuilder(context, MonsterDatabase::class.java, "monster_database")
                    .build()

                Instance = instance
                return instance
            }
        }
    }
}