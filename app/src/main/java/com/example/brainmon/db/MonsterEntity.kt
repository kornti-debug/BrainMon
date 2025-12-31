package com.example.brainmon.db

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "monsters")
data class MonsterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val originalName: String,
    val type: String,
    val combatPower: Int,
    val imageUrl: String
)