package com.example.brainmon.data

import com.example.brainmon.db.MonsterDao
import com.example.brainmon.db.MonsterEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MonsterRepository(private val monsterDao: MonsterDao) {

    // 1. READ
    val monsters = monsterDao.getAllMonsters().map { monsterList ->
        monsterList.map { entity ->
            Monster(
                entity.id,
                entity.name,
                entity.originalName,
                entity.type,
                entity.combatPower,
                entity.imageUrl
            )
        }
    }

    // 2. CREATE
    suspend fun addMonster(name: String, originalName: String, type: String, power: Int, imageUrl: String) {
        monsterDao.insert(
            MonsterEntity(
                id = 0,
                name = name,
                originalName = originalName,
                type = type,
                combatPower = power,
                imageUrl = imageUrl
            )
        )
    }

    // 3. DELETE
    suspend fun deleteMonster(monster: Monster) {
        monsterDao.delete(
            MonsterEntity(
                monster.id,
                monster.name,
                monster.originalName,
                monster.type,
                monster.combatPower,
                monster.imageUrl
            )
        )
    }

    // 4. UPDATE
    suspend fun updateMonster(monster: Monster) {
        monsterDao.update(
            MonsterEntity(
                id = monster.id,
                name = monster.name,
                originalName = monster.originalName,
                type = monster.type,
                combatPower = monster.combatPower,
                imageUrl = monster.imageUrl
            )
        )
    }

    // 5. STREAM SINGLE
    fun getMonsterStream(id: Int): Flow<Monster?> {
        return monsterDao.getMonsterStream(id).map { entity ->
            entity?.let {
                Monster(
                    it.id,
                    it.name,
                    it.originalName,
                    it.type,
                    it.combatPower,
                    it.imageUrl
                )
            }
        }
    }
}