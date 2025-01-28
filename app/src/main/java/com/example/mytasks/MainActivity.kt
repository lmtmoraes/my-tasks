package com.example.mytasks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.mytasks.ui.navigation.TaskFormRoute
import com.example.mytasks.ui.navigation.TasksListRoute
import com.example.mytasks.ui.navigation.taskFormScreen
import com.example.mytasks.ui.navigation.taskListFragment

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = TasksListRoute.ROUTE,
                    Modifier.padding(it)
                ) {
                    taskListFragment(
                        onNavigateToNewTaskForm = {
                            navController.navigate(TaskFormRoute())
                        },
                        onNavigateToEditTaskForm = { task ->
                            navController.navigate(TaskFormRoute(task.id))
                        }
                    )
                    taskFormScreen {
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}
