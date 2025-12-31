package com.example.brainmon.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface MonsterDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(monster: MonsterEntity)

    @Update
    suspend fun update(monster: MonsterEntity)

    @Delete
    suspend fun delete(monster: MonsterEntity)

    @Query("SELECT * from monsters ORDER BY name ASC")
    fun getAllMonsters(): Flow<List<MonsterEntity>>

    @Query("SELECT * from monsters WHERE id = :id")
    fun getMonsterStream(id: Int): Flow<MonsterEntity>

    @Query("SELECT * from monsters WHERE id = :id")
    fun getMonster(id: Int): Flow<MonsterEntity>

    @Query("SELECT * from monsters WHERE id = :id")
    suspend fun getMonsterSnapshot(id: Int): MonsterEntity
}