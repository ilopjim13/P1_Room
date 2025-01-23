package com.example.p1_room.addtasks.data

import androidx.room.Entity
import androidx.room.PrimaryKey

//La Entidad es el modelo de datos que vamos a persistir en nuestra base de datos...
@Entity(tableName = "tasks")
data class TaskEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val task: String,
    var selected: Boolean = false
)
