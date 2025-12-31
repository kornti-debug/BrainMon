package com.example.brainmon.data

data class Monster(
    val id: Int,
    val name: String,         // nickname
    val originalName: String, // pikachu
    val type: String,
    val combatPower: Int,
    val imageUrl: String
)