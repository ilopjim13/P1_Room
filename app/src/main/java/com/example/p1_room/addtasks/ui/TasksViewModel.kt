package com.example.p1_room.addtasks.ui

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.p1_room.addtasks.domain.AddTaskUseCase
import com.example.p1_room.addtasks.domain.DeleteTaskUseCase
import com.example.p1_room.addtasks.domain.GetTasksUseCase
import com.example.p1_room.addtasks.domain.UpdateTaskUseCase
import com.example.p1_room.addtasks.ui.model.TaskModel
import com.example.p1_room.addtasks.ui.TaskUiState.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TasksViewModel(
    private val addTaskUseCase: AddTaskUseCase,
    getTasksUseCase: GetTasksUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase
) : ViewModel() {

    val uiState: StateFlow<TaskUiState> = getTasksUseCase().map(::Success)
        .catch { Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    private val _showUpdate = MutableLiveData<Boolean>()
    val showUpdate: LiveData<Boolean> = _showUpdate

    private val _taskUpdate = MutableLiveData<TaskModel>(TaskModel(task = ""))
    val taskUpdate: LiveData<TaskModel> = _taskUpdate

    private val _myTaskText = MutableLiveData<String>()
    val myTaskText: LiveData<String> = _myTaskText

    fun onDialogClose() {
        _showDialog.value = false
    }

    fun onTaskCreated() {
        onDialogClose()

        //Un viewModelScope es una corutina.
        viewModelScope.launch {
            addTaskUseCase(TaskModel(task = _myTaskText.value ?: ""))
        }

        _myTaskText.value = ""
    }

    fun onShowDialogClick() {
        _showDialog.value = true
    }

    fun onTaskTextChanged(taskText: String) {
        _myTaskText.value = taskText
    }

    fun onItemRemove(taskModel: TaskModel) {
        viewModelScope.launch {
            deleteTaskUseCase(taskModel)
        }
        _showUpdate.value = false
    }

    fun onCheckBoxSelected(taskModel: TaskModel) {
        taskModel.selected.value = !taskModel.selected.value
        viewModelScope.launch {
            updateTaskUseCase(taskModel)
        }
    }

    fun showUpdate(taskModel: TaskModel) {
        _showUpdate.value = true
        _myTaskText.value = taskModel.task
        _taskUpdate.value = taskModel
        Log.d("TaskViewModel", "textoooo: $taskModel")
    }

    fun showUpdateClose() {
        _showUpdate.value = false
        _myTaskText.value = ""
    }

    fun onTaskUpdate(taskModel: TaskModel) {
        val updatedTask = taskModel.copy(task = _myTaskText.value.toString())

        Log.d("TaskViewModel", "Updating task to: $updatedTask")
        Log.d("TaskViewModel", "Updating task to: $taskModel")

        viewModelScope.launch {
            updateTaskUseCase(updatedTask)
        }

        _taskUpdate.value = updatedTask
        _showUpdate.value = false
        _myTaskText.value = ""
    }

}