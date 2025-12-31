package com.example.brainmon

import android.app.Application
import com.example.brainmon.data.MonsterRepository
import com.example.brainmon.db.MonsterDatabase


class MonsterApplication : Application() {
    private val database by lazy { MonsterDatabase.getDatabase(this) }

    val repository by lazy { MonsterRepository(database.monsterDao()) }
}