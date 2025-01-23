package com.example.p1_room.addtasks.data

import android.content.Context
import androidx.room.Room

class DatabaseModule(context: Context) {

    private val tasksManageDatabase: TasksManageDatabase = Room.databaseBuilder(
        context.applicationContext,
        TasksManageDatabase::class.java,
        "TaskDatabase"
    ).build()

    fun provideTaskDao(): TaskDao {
        return tasksManageDatabase.taskDao()
    }

    fun provideTasksManageDatabase(): TasksManageDatabase {
        return tasksManageDatabase
    }
}

