package com.dicoding.todoapp.ui.add

import androidx.lifecycle.*
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.data.TaskRepository
import kotlinx.coroutines.launch

class AddTaskViewModel(private val taskRepository: TaskRepository): ViewModel() {
    fun addTask(task: Task) {

        viewModelScope.launch {
            taskRepository.insertTask(task) }
    }
}