package com.example.p1_room.addtasks.ui

import com.example.p1_room.addtasks.ui.model.TaskModel

/**
 * Estados de la pantalla
 */
sealed interface TaskUiState {
    //Si no recibes datos... usamos object
    object Loading: TaskUiState
    data class Error(val throwable: Throwable): TaskUiState
    //La lista debe ser de elementos de tipo TaskModel porque estamos en la capa de UI.
    //Recordad que TaskEntity solo va a ser accesible desde la capa de data y domain.
    data class Success(val tasks:List<TaskModel>) : TaskUiState
}