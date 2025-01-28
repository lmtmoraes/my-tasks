package com.example.mytasks.data

import com.example.mytasks.data.dao.TaskDao
import com.example.mytasks.data.entities.TaskEntity
import com.example.mytasks.data.models.Task
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class TaskRepository(
    private val taskDao: TaskDao
) {

    val tasks = taskDao.findAllTasks()

    suspend fun insertTask(task: Task) {
        taskDao.save(task.toTaskEntity())
    }

    suspend fun toggleIsCompleted(task: Task) = withContext(IO) {
        val entity = task.copy(isCompleted = !task.isCompleted)
            .toTaskEntity()
        taskDao.save(entity)
    }

    suspend fun delete(id: String) {
        taskDao.delete(
            TaskEntity(id = id, title = "")
        )
    }

    fun findTaskById(id: String) = taskDao.findTaskById(id)
}


fun Task.toTaskEntity() = TaskEntity(
    id = this.id,
    title = this.title,
    description = this.description,
    isCompleted = this.isCompleted
)

fun TaskEntity.toTask() = Task(
    id = this.id,
    title = this.title,
    description = this.description,
    isCompleted = this.isCompleted
)