package com.example.mytasks.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mytasks.ui.states.TaskFormUiState
import com.example.mytasks.ui.theme.MyTasksTheme
import com.example.mytasks.ui.viewmodel.TaskFormViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun TaskFormScreen(
    taskId: String?,
    onPopBackStack: () -> Unit
) {
    val viewModel: TaskFormViewModel = koinViewModel(parameters = { parametersOf(taskId) })
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel.navigateBack) {
        viewModel.navigateBack.collect {
            onPopBackStack()
        }
    }

    TaskFormBody(
        uiState = uiState,
        onSaveClick = { viewModel.onSaveClick() },
        onDeleteClick = { viewModel.onDeleteClick() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskFormBody(
    uiState: TaskFormUiState,
    modifier: Modifier = Modifier,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 72.dp)
        ) {
            val topAppBarTitle = uiState.topAppBarTitle
            TopAppBar(
                title = {
                    Text(
                        text = topAppBarTitle,
                        style = TextStyle(
                            color = Color.White,
                            fontStyle = FontStyle.Italic,
                            fontSize = 38.sp
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF03A9F4))
            )
            Spacer(modifier = Modifier.size(8.dp))
            val title = uiState.title
            val description = uiState.description
            val titleFontStyle = TextStyle.Default.copy(fontSize = 24.sp)
            val descriptionFontStyle = TextStyle.Default.copy(fontSize = 18.sp)
            BasicTextField(
                value = title,
                onValueChange = uiState.onTitleChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                decorationBox = { innerTextField ->
                    if (title.isEmpty()) {
                        Text(
                            text = "Title",
                            style = titleFontStyle.copy(color = Color.Black)
                        )
                    }
                    innerTextField()
                },
                textStyle = titleFontStyle
            )
            Spacer(modifier = Modifier.size(16.dp))
            BasicTextField(
                value = description,
                onValueChange = uiState.onDescriptionChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                decorationBox = { innerTextField ->
                    if (description.isEmpty()) {
                        Text(
                            text = "Description",
                            style = descriptionFontStyle.copy(color = Color.Black)
                        )
                    }
                    innerTextField()
                },
                textStyle = descriptionFontStyle
            )
        }

        if (uiState.isDeleteEnabled) {
            FloatingActionButton(
                onClick = { onDeleteClick() },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                containerColor = Color.Red
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete task icon",
                    tint = Color.White
                )
            }
        }

        FloatingActionButton(
            onClick = { onSaveClick() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color.Green
        ) {
            Icon(
                imageVector = Icons.Filled.Done,
                contentDescription = "Save task icon",
                tint = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskFormScreenPreview() {
    MyTasksTheme {
        TaskFormBody(
            uiState = TaskFormUiState(
                topAppBarTitle = "Creating new task"
            ),
            onSaveClick = {},
            onDeleteClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TaskFormScreenWithEditModePreview() {
    MyTasksTheme {
        TaskFormBody(
            uiState = TaskFormUiState(
                topAppBarTitle = "Editando tarefa",
                isDeleteEnabled = true
            ),
            onSaveClick = {},
            onDeleteClick = {}
        )
    }
}