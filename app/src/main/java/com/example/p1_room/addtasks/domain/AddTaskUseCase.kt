package com.example.p1_room.addtasks.domain

import com.example.p1_room.addtasks.data.TaskRepository
import com.example.p1_room.addtasks.ui.model.TaskModel

/**
 * Caso de uso para añadir una tarea
 */
class AddTaskUseCase(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(taskModel: TaskModel) {
        taskRepository.add(taskModel)
    }
}