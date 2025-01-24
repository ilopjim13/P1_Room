package com.example.p1_room

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.p1_room.addtasks.data.DatabaseModule
import com.example.p1_room.addtasks.data.TaskDao
import com.example.p1_room.addtasks.data.TaskRepository
import com.example.p1_room.addtasks.data.TasksManageDatabase
import com.example.p1_room.addtasks.domain.AddTaskUseCase
import com.example.p1_room.addtasks.domain.DeleteTaskUseCase
import com.example.p1_room.addtasks.domain.GetTasksUseCase
import com.example.p1_room.addtasks.domain.UpdateTaskUseCase
import com.example.p1_room.addtasks.ui.TasksScreen
import com.example.p1_room.addtasks.ui.TasksViewModel
import com.example.p1_room.ui.theme.P1_RoomTheme

class MainActivity : ComponentActivity() {

    private val tasksViewModel: TasksViewModel by viewModels {
        TasksViewModelFactory(addTaskUseCase, getTasksUseCase, deleteTaskUseCase, updateTaskUseCase)
    }

    private lateinit var addTaskUseCase: AddTaskUseCase
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
    private lateinit var updateTaskUseCase: UpdateTaskUseCase
    private lateinit var getTasksUseCase: GetTasksUseCase
    private lateinit var taskRepository: TaskRepository
    private lateinit var taskDao: TaskDao
    private lateinit var tasksManageDatabase: TasksManageDatabase

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val databaseModule = DatabaseModule(this)
        taskDao = databaseModule.provideTaskDao()
        tasksManageDatabase = databaseModule.provideTasksManageDatabase()

        taskRepository = TaskRepository(taskDao)
        addTaskUseCase = AddTaskUseCase(taskRepository)
        deleteTaskUseCase = DeleteTaskUseCase(taskRepository)
        updateTaskUseCase = UpdateTaskUseCase(taskRepository)
        getTasksUseCase = GetTasksUseCase(taskRepository)

        enableEdgeToEdge()
        setContent {
            P1_RoomTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TasksScreen(tasksViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


class TasksViewModelFactory(
    private val addTaskUseCase: AddTaskUseCase,
    private val getTasksUseCase: GetTasksUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TasksViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TasksViewModel(addTaskUseCase, getTasksUseCase, deleteTaskUseCase, updateTaskUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

