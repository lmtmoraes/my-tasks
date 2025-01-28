package com.example.mytasks.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mytasks.data.dao.TaskDao
import com.example.mytasks.data.entities.TaskEntity

@Database(entities = [TaskEntity::class], version = 2)
abstract class MyTasksDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

}