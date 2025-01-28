package com.example.mytasks.data.dao

import androidx.room.*
import com.example.mytasks.data.entities.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM TaskEntity")
    fun findAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM TaskEntity WHERE id = :id")
    fun findTaskById(id: String): Flow<TaskEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)

}