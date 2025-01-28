package com.example.mytasks.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mytasks.data.models.Task
import com.example.mytasks.ui.states.TasksListUiState
import com.example.mytasks.ui.theme.MyTasksTheme
import com.example.mytasks.ui.viewmodel.TaskListViewModel
import org.koin.androidx.compose.koinViewModel
import java.util.UUID

@Composable
fun TaskListScreen(
    onNewTaskClick: () -> Unit,
    onTaskClick: (Task) -> Unit
){
    val viewModel: TaskListViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState(TasksListUiState())

    TaskListBody(
        uiState = uiState,
        onNewTaskClick = onNewTaskClick,
        onTaskClick = onTaskClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListBody(
    uiState: TasksListUiState,
    modifier: Modifier = Modifier,
    onNewTaskClick: () -> Unit = {},
    onTaskClick: (Task) -> Unit = {},
) {
    Column {
        TopAppBar(
            title = {
                Text(
                    text = "My Tasks",
                    style = TextStyle(
                        color = Color.White,
                        fontStyle = FontStyle.Italic,
                        fontSize = 38.sp
                    )
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF03A9F4)),
            actions = {
                var isSearchTextFieldEnabled by remember {
                    mutableStateOf(false)
                }
                var text by remember {
                    mutableStateOf("")
                }
                AnimatedVisibility(visible = isSearchTextFieldEnabled) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "ícone para fechar campo de texto de busca",
                        Modifier
                            .clip(CircleShape)
                            .clickable {
                                isSearchTextFieldEnabled = false
                                text = ""
                            }
                            .padding(8.dp),
                        tint = Color.White
                    )
                }
                BasicTextField(
                    value = text,
                    onValueChange = {
                        text = it
                    }, modifier.fillMaxWidth(
                        animateFloatAsState(
                            targetValue = if (isSearchTextFieldEnabled) 1f else 0f,
                            label = "basic text field width"
                        ).value
                    ),
                    decorationBox = { innerTextField ->
                        if (text.isEmpty()) {
                            Text(
                                text = "O que você busca?",
                                style = TextStyle(
                                    color = Color.White.copy(alpha = 0.5f),
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 18.sp
                                )
                            )
                        }
                        innerTextField()
                    },
                    textStyle = TextStyle.Default.copy(
                        fontSize = 18.sp,
                        color = Color.White,
                    ),
                    cursorBrush = SolidColor(Color.White)
                )
                AnimatedVisibility(visible = !isSearchTextFieldEnabled) {
                    Row {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = "ícone de busca",
                            Modifier
                                .clip(CircleShape)
                                .clickable { isSearchTextFieldEnabled = true }
                                .padding(8.dp),
                            tint = Color.White
                        )
                    }
                }

            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            ExtendedFloatingActionButton(
                onClick = onNewTaskClick,
                containerColor = Color(0xFF03A9F4)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Ícone para adicionar nova tarefa",
                        modifier = Modifier.size(32.dp),
                        tint = Color.White
                    )
                    Text(
                        text = "Nova Tarefa",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }
        Box(modifier) {
            if (uiState.tasks.isEmpty()) {
                Text(
                    textAlign = TextAlign.Center,
                    text = "Sua lista de tarefas está vazia.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
            LazyColumn(Modifier.fillMaxSize()) {
                items(uiState.tasks) { task ->
                    var showDescription by remember {
                        mutableStateOf(false)
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                    ) {
                        Box(
                            Modifier
                                .align(Alignment.CenterVertically)
                                .padding(
                                    vertical = 16.dp, horizontal = 8.dp
                                )
                                .size(30.dp)
                                .border(
                                    border = BorderStroke(2.dp, color = Color.Gray),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clip(shape = RoundedCornerShape(8.dp))
                                .clickable {
                                    Log.i("TasksListScreen", "$task")
                                    uiState.onTaskDoneChange(task)
                                }) {
                            if (task.isCompleted) {
                                Icon(
                                    Icons.Filled.Done,
                                    contentDescription = "Done icon",
                                    Modifier.size(100.dp),
                                    tint = Color.Green
                                )
                            }
                        }
                        Column(
                            Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = task.title, style = TextStyle.Default.copy(
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 2,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    imageVector = Icons.Filled.Info,
                                    contentDescription = "Ícone de informações",
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clickable { showDescription = !showDescription }
                                        .padding(8.dp),
                                    tint = Color(0xFF03A9F4)
                                )
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = "Ícone de edição",
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clickable { onTaskClick(task) }
                                        .padding(8.dp),
                                    tint = Color(0xFF03A9F4)
                                )
                            }
                            task.description?.let { description ->
                                AnimatedVisibility(
                                    visible = showDescription && description.isNotBlank()
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = description,
                                            style = TextStyle.Default.copy(fontSize = 24.sp),
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 3,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TasksListScreenPreview() {
    val taskList = listOf(
        Task(
            id = UUID.randomUUID().toString(),
            title = "Mercado",
            description = "Comprar leite, ovos e pão.",
            isCompleted = false
        ),
        Task(
            id = UUID.randomUUID().toString(),
            title = "Academia",
            description = "Malhar costa e bíceps",
            isCompleted = true
        ),
        Task(
            id = UUID.randomUUID().toString(),
            title = "Ler um livro",
            description = "Ler dez páginas do livro 'Código Limpo' ",
            isCompleted = false
        )
    )

    MyTasksTheme {
        TaskListBody(
            uiState = TasksListUiState(
                tasks = taskList
            )
        )
    }
}