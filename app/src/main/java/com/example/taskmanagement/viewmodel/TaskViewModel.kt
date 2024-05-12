package com.example.taskmanagement.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.data.model.Task
import com.example.taskmanagement.data.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {
    val allTasks = repository.allTasks.asLiveData()

    fun insert(task: Task) = viewModelScope.launch {
        repository.insert(task)
    }

    fun update(task: Task) = viewModelScope.launch {
        repository.update(task)
    }

    fun delete(taskId: Long) = viewModelScope.launch {
        repository.delete(taskId)
    }

//    fun getTaskById(taskId: Long) = repository.getTaskById(taskId).asLiveData()
    fun getTaskById(taskId: Long): LiveData<Task?> {
        return repository.getTaskById(taskId).asLiveData()
    }


}
