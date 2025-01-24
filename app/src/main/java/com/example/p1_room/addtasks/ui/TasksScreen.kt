package com.example.p1_room.addtasks.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.example.p1_room.addtasks.ui.model.TaskModel

/**
 * Función principal básica
 */
@Composable
fun TasksScreen(tasksViewModel: TasksViewModel, modifier: Modifier = Modifier) {

    val showDialog: Boolean by tasksViewModel.showDialog.observeAsState(false)
    val showUpdate: Boolean by tasksViewModel.showUpdate.observeAsState(false)
    val myTaskText: String by tasksViewModel.myTaskText.observeAsState("")
    val taskUpdate: TaskModel by tasksViewModel.taskUpdate.observeAsState(initial = TaskModel(task = ""))
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<TaskUiState>(
        initialValue = TaskUiState.Loading,
        key1 = lifecycle,
        key2 = tasksViewModel
    ){
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            tasksViewModel.uiState.collect{ value = it }
        }
    }


    when (uiState) {
        is TaskUiState.Error -> {  }
        is TaskUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(150.dp)
                        .align(Alignment.Center)
                )
            }
        }
        is TaskUiState.Success -> {
            Box(modifier = modifier.fillMaxSize()) {
                AddTasksDialog(
                    show = showDialog,
                    myTaskText = myTaskText,
                    onDismiss = { tasksViewModel.onDialogClose() },
                    onTaskAdded = { tasksViewModel.onTaskCreated() },
                    onTaskTextChanged = { tasksViewModel.onTaskTextChanged(it) }
                )
                UpdateTaskDialog(
                    show = showUpdate,
                    myTaskText = myTaskText,
                    onDismiss = { tasksViewModel.showUpdateClose() },
                    onTaskUpdate = { tasksViewModel.onTaskUpdate(taskUpdate) },
                    onTaskTextChanged = { tasksViewModel.onTaskTextChanged(it) }
                )
                FabDialog(
                    Modifier.align(Alignment.BottomEnd),
                    onNewTask = { tasksViewModel.onShowDialogClick() })
                TasksList((uiState as TaskUiState.Success).tasks, tasksViewModel)
            }
        }
    }
}

@Composable
fun FabDialog(
    modifier: Modifier,
    onNewTask: () -> Unit
) {
    FloatingActionButton(
        onClick = {
            onNewTask()
        }, modifier = modifier.padding(16.dp)
    ) {
        Icon(Icons.Filled.Add, contentDescription = "")
    }
}

@Composable
fun AddTasksDialog(
    show: Boolean,
    myTaskText: String,
    onDismiss: () -> Unit,
    onTaskAdded: () -> Unit,
    onTaskTextChanged: (String) -> Unit
) {
    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Añade tu tarea",
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(16.dp))
                TextField(
                    value = myTaskText,
                    onValueChange = { onTaskTextChanged(it) },
                    singleLine = true,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.size(16.dp))
                Button(
                    onClick = {
                        onTaskAdded()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Añadir tarea")
                }
            }
        }
    }
}

@Composable
fun UpdateTaskDialog(
    show: Boolean,
    myTaskText: String,
    onDismiss: () -> Unit,
    onTaskUpdate: () -> Unit,
    onTaskTextChanged: (String) -> Unit
) {
    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Actualiza tu tarea",
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(16.dp))
                TextField(
                    value = myTaskText,
                    onValueChange = { onTaskTextChanged(it) },
                    singleLine = true,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.size(16.dp))
                Button(
                    onClick = {
                        onTaskUpdate()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Actualizar tarea")
                }
            }
        }
    }
}

@Composable
fun TasksList(tasks: List<TaskModel>, tasksViewModel: TasksViewModel) {
    LazyColumn {
        items(tasks, key = { it.id }) { task ->
            ItemTask(
                task,
                onTaskRemove = { tasksViewModel.onItemRemove(it) },
                onTaskCheckChanged = { tasksViewModel.onCheckBoxSelected(it) },
                showUpdate = {tasksViewModel.showUpdate(it)}
            )
        }
    }
}

@Composable
fun ItemTask (
    taskModel: TaskModel,
    onTaskRemove: (TaskModel) -> Unit,
    showUpdate: (TaskModel) -> Unit,
    onTaskCheckChanged: (TaskModel) -> Unit
) {
    Card(
        //pointerInput es una función se utiliza para configurar la entrada de puntero (input)
        //para el componente visual al que se le aplica... la detección de gestos de entrada táctil
        //En nuestro caso queremos eliminar una tarea con el gesto de pulsación larga (onLongPress)
        //sobre la tarea, por lo tanto el componente visual dónde aplicar el modificador debe ser el Card.
        //En la expresión lambda no podemos utilizar it como parámetro de la llamada a onTaskRemove(it)
        //it es el Offset y nosotros necesitamos pasarle el taskModel que debe eliminarse...
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { onTaskRemove(taskModel) },
                    onDoubleTap = { showUpdate(taskModel) })
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = taskModel.task,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .weight(1f)
            )
            Checkbox(
                checked = taskModel.selected.value,
                onCheckedChange = { onTaskCheckChanged(taskModel) }
            )
            Log.d("TaskViewModel", "en lista: $taskModel")
        }
    }
}