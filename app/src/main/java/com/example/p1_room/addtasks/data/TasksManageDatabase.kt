package com.example.p1_room.addtasks.data

import androidx.room.Database
import androidx.room.RoomDatabase

//TaskEntity::class te dará una referencia al objeto KClass que representa la clase TaskEntity.
//Esto es útil, por ejemplo, cuando necesitas trabajar con la clase de manera dinámica,
//como para obtener información sobre sus miembros, crear instancias de la clase, etc.
@Database(entities = [TaskEntity::class], version = 1,exportSchema = false)
abstract class TasksManageDatabase: RoomDatabase() {
    //DAO
    abstract fun taskDao():TaskDao
}