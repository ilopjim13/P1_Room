package com.example.p1_room.addtasks.data

import androidx.compose.runtime.mutableStateOf
import com.example.p1_room.addtasks.ui.model.TaskModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepository(private val taskDao: TaskDao) {

    suspend fun add(taskModel: TaskModel) {
        taskDao.addTask(taskModel.toData())
    }

    suspend fun update(taskModel: TaskModel) {
        taskDao.updateTask(taskModel.toData())
    }

    suspend fun delete(taskModel: TaskModel) {
        taskDao.deleteTask(taskModel.toData())
    }

    fun getTasks(): Flow<List<TaskModel>> {
        return taskDao.getTasks().map { items ->
            items.map { it.toModel() }
        }
    }
}

fun TaskModel.toData(): TaskEntity {
    return TaskEntity(this.id, this.task, this.selected.value)
}

fun TaskEntity.toModel(): TaskModel {
    return TaskModel(this.id, this.task, mutableStateOf( this.selected))
}
