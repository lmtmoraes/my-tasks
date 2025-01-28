package com.example.mytasks.data.models

import java.util.UUID

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String? = null,
    val isCompleted: Boolean = false,
    val creationDate: Long = System.currentTimeMillis()
)