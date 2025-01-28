package com.example.mytasks.ui.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.mytasks.data.models.Task
import com.example.mytasks.ui.states.TasksListUiState
import com.example.mytasks.ui.viewmodel.TaskListViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import com.example.mytasks.ui.screens.TaskListScreen

@Serializable
object TasksListRoute {
    const val ROUTE = "tasks_list"
}

fun NavGraphBuilder.taskListFragment(
    onNavigateToNewTaskForm: () -> Unit,
    onNavigateToEditTaskForm: (Task) -> Unit,
) {
    composable(route = TasksListRoute.ROUTE) {
        TaskListScreen(
            onNewTaskClick = onNavigateToNewTaskForm,
            onTaskClick = onNavigateToEditTaskForm
        )
    }
}