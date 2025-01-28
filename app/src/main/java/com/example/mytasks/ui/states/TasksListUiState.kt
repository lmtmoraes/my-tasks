package com.example.mytasks.ui.states

import com.example.mytasks.data.models.Task

data class TasksListUiState(
    val tasks: List<Task> = emptyList(),
    val onTaskDoneChange: (Task) -> Unit = {},
)
