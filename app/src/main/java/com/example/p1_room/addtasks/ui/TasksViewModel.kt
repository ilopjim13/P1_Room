package com.example.p1_room.addtasks.ui

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.p1_room.addtasks.ui.model.TaskModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(): ViewModel() {

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    private val _myTaskText = MutableLiveData<String>()
    val myTaskText: LiveData<String> = _myTaskText

    private val _tasks = mutableStateListOf<TaskModel>()
    val tasks: List<TaskModel> = _tasks

    //Actualizamos la función para crear la tarea en la lista anterior
    //Comentamos el mensaje al log que hemos realizado inicialmente y añadimos una nueva tarea a la lista _tasks
    fun onTaskCreated() {
        onDialogClose()
        //Log.i("dam2", _myTaskText.value ?: "")
        _tasks.add(TaskModel(task = _myTaskText.value ?: ""))
        _myTaskText.value = ""
    }

    fun onItemRemove(taskModel: TaskModel) {
        //No podemos usar directamente _tasks.remove(taskModel) porque no es posible por el uso de let con copy para modificar el checkbox...
        //Para hacerlo correctamente, debemos previamente buscar la tarea en la lista por el id y después eliminarla
        val task = _tasks.find { it.id == taskModel.id }
        _tasks.remove(task)
    }

    fun onDialogClose() {
        _showDialog.value = false
    }

    fun onShowDialogClick() {
        _showDialog.value = true
    }

    fun onTaskTextChanged(taskText: String) {
        _myTaskText.value = taskText
    }

    fun onCheckBoxSelected(taskModel: TaskModel) {
        val index = _tasks.indexOf(taskModel)

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
        _tasks[index] = _tasks[index].let { it.copy(selected = !it.selected) }
    }

}