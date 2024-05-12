package com.example.taskmanagement.data.repository

import com.example.taskmanagement.data.database.TaskDao
import com.example.taskmanagement.data.model.Task
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {
    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun insert(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun update(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun delete(taskId: Long) {
        taskDao.deleteTask(taskId)
    }

    fun getTaskById(taskId: Long): Flow<Task> {
        return taskDao.getTaskById(taskId)
    }


}
