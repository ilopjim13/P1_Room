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
) : ViewModel()  {

    val uiState: StateFlow<TaskUiState> = getTasksUseCase().map(::Success)
        .catch { Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    private val _myTaskText = MutableLiveData<String>()
    val myTaskText: LiveData<String> = _myTaskText

    //Utilizamos mutableStateListOf porque se lleva mejor con LazyColumn a la hora
    //de refrescar la información en la vista...
    //private val _tasks = mutableStateListOf<TaskModel>()
    //val tasks: List<TaskModel> = _tasks

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
        //No podemos usar directamente _tasks.remove(taskModel) porque no es posible por el uso de let con copy para modificar el checkbox...
        //Para hacerlo correctamente, debemos previamente buscar la tarea en la lista por el id y después eliminarla
        //val task = _tasks.find { it.id == taskModel.id }
        //_tasks.remove(task)
        viewModelScope.launch {
            deleteTaskUseCase(taskModel)
        }
    }

    fun onCheckBoxSelected(taskModel: TaskModel) {
        //val index = taskModel.id

        //Si se modifica directamente _tasks[index].selected = true no se recompone el item en el LazyColumn
        //Esto nos ha pasado ya en el proyecto BlackJack... ¿¿os acordáis?? :-(
        //Y es que la vista no se entera que debe recomponerse, aunque realmente si se ha modificado el valor en el item
        //Para solucionarlo y que se recomponga sin problemas en la vista, lo hacemos con un let...

        //El método let toma como parámetro el objeto y devuelve el resultado de la expresión lambda
        //En nuestro caso, el objeto que recibe let es de tipo TaskModel, que está en _tasks[index]
        //(sería el it de la exprexión lambda)
        //El método copy realiza una copia del objeto, pero modificando la propiedad selected a lo contrario
        //El truco está en que no se modifica solo la propiedad selected de tasks[index],
        //sino que se vuelve a reasignar para que la vista vea que se ha actualizado un item y se recomponga.
        //_tasks[index] = _tasks[index].let { it.copy(selected = !it.selected) }
        taskModel.selected = true
    }

}