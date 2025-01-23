package com.example.p1_room.addtasks.ui.model

data class TaskModel(
    //El método hashcode() nos crea un código único a través del número de milisegundos al que aplica...
    //El hascode() es lo que utiliza el compilador para comparar objetos, es decir,
    //lo hace a través del hashcode() que se genera en cada objeto.
    val id: Int = System.currentTimeMillis().hashCode(),
    val task: String,
    var selected: Boolean = false
)
