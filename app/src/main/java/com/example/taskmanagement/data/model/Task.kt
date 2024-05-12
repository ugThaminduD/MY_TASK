package com.example.taskmanagement.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String,
    val priority: String,
    val deadline: String,
//    val completed: Boolean = false // Add completed property with default value

)
