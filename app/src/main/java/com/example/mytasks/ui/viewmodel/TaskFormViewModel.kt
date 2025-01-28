package com.example.mytasks.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytasks.data.TaskRepository
import com.example.mytasks.data.models.Task
import com.example.mytasks.data.toTask
import com.example.mytasks.ui.states.TaskFormUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class TaskFormViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: TaskRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskFormUiState())
    val uiState = _uiState.asStateFlow()

    private val id: String? = savedStateHandle["taskId"]

    private val _navigateBack = MutableSharedFlow<Unit>()
    val navigateBack = _navigateBack.asSharedFlow()

    init {
        _uiState.update { currentState ->
            currentState.copy(
                onTitleChange = { title ->
                    _uiState.update {
                        it.copy(title = title)
                    }
                },
                onDescriptionChange = { description ->
                    _uiState.update {
                        it.copy(description = description)
                    }
                },
                topAppBarTitle = "Creating new task"
            )
        }

        id?.let {
            viewModelScope.launch {
                repository.findTaskById(id)
                    .filterNotNull()
                    .mapNotNull { it.toTask() }
                    .collectLatest { task ->
                        _uiState.update { currentState ->
                            currentState.copy(
                                topAppBarTitle = "Editing task",
                                title = task.title,
                                description = task.description ?: "",
                                isDeleteEnabled = true
                            )
                        }
                    }
            }
        }
    }

    fun onSaveClick() {
        viewModelScope.launch {
            save()
            _navigateBack.emit(Unit)
        }
    }

    private suspend fun save() {
        with(_uiState.value) {
            repository.insertTask(
                Task(
                    id = id ?: UUID.randomUUID().toString(),
                    title = title,
                    description = description
                )
            )
        }
    }

    fun onDeleteClick() {
        viewModelScope.launch {
            delete()
            _navigateBack.emit(Unit)
        }
    }

    private suspend fun delete() {
        id?.let {
            repository.delete(it)
        }
    }
}