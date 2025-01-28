package com.example.mytasks.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.mytasks.ui.screens.TaskFormScreen
import kotlinx.serialization.Serializable

@Serializable
data class TaskFormRoute(val taskId: String? = null)

fun NavGraphBuilder.taskFormScreen(
    onPopBackStack: () -> Unit,
) {
    composable<TaskFormRoute> { backStackEntry ->
        val taskId = backStackEntry.toRoute<TaskFormRoute>().taskId

        TaskFormScreen(
            taskId = taskId,
            onPopBackStack = onPopBackStack
        )
    }
}
