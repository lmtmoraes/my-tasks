package com.example.mytasks.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytasks.data.TaskRepository
import com.example.mytasks.data.toTask
import com.example.mytasks.ui.states.TasksListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TaskListViewModel(
    private val repository: TaskRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TasksListUiState())
    val uiState
        get() = _uiState
            .combine(repository.tasks) { uiState, tasks ->
                uiState.copy(tasks = tasks.map { it.toTask() })
            }

    init {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(onTaskDoneChange = { task ->
                    viewModelScope.launch {
                        repository.toggleIsCompleted(task)
                    }
                })
            }
        }
    }
}
