package com.example.p1_room.addtasks.domain

import com.example.p1_room.addtasks.data.TaskEntity
import com.example.p1_room.addtasks.data.TaskRepository
import com.example.p1_room.addtasks.ui.model.TaskModel
import kotlinx.coroutines.flow.Flow

/**
 * Caso de uso para recuperar las tareas
 *
 * Para acceder al data vamos a necesitar el repositorio, ya que es nuestra puerta de entrada al data.
 * Gracias a Dagger Hilt lo vamos a inyectar en el constructor.
 */

class GetTasksUseCase(private val taskRepository: TaskRepository) {
    operator fun invoke(): Flow<List<TaskModel>> {
        return taskRepository.getTasks()
    }
}