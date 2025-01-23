package com.example.p1_room.addtasks.domain

import com.example.p1_room.addtasks.data.TaskRepository
import com.example.p1_room.addtasks.ui.model.TaskModel

class UpdateTaskUseCase(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(taskModel: TaskModel) {
        taskRepository.update(taskModel)
    }
}