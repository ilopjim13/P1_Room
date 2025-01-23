package com.example.p1_room.addtasks.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Define una interfaz que contiene métodos para interactuar con la tabla TaskEntity en la base de datos.
 */
@Dao
interface TaskDao {
    @Query("SELECT * from tasks")
    //Básicamente nos vamos a enganchar a través de Flow, va a retornar un Flow con una lista de TaskEntity,
    //y las librerías de Flow se encargarán de avisar cuando algún dato de la Entidad se haya agregado, actualizado o eliminado
    fun getTasks(): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask(item: TaskEntity)

    @Update
    suspend fun updateTask(item: TaskEntity)

    @Delete
    suspend fun deleteTask(item: TaskEntity)
}