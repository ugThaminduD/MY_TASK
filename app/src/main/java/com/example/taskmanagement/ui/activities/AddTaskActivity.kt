package com.example.taskmanagement.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.example.taskmanagement.R
import com.example.taskmanagement.data.database.TaskDatabase
import com.example.taskmanagement.data.model.Task
import com.example.taskmanagement.data.repository.TaskRepository
import com.example.taskmanagement.viewmodel.TaskViewModel
import com.example.taskmanagement.viewmodel.TaskViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var spinnerPriority: Spinner
    private lateinit var datePickerDeadline: DatePicker
    private lateinit var buttonAddTask: Button

    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        editTextName = findViewById(R.id.editTextName)
        editTextDescription = findViewById(R.id.editTextDescription)
        spinnerPriority = findViewById(R.id.spinnerPriority)
        datePickerDeadline = findViewById(R.id.datePickerDeadline)
        buttonAddTask = findViewById(R.id.buttonAddTask)

        val taskDao = TaskDatabase.getInstance(applicationContext).taskDao()
        val repository = TaskRepository(taskDao)
        val viewModelFactory = TaskViewModelFactory(repository)
        taskViewModel = ViewModelProvider(this, viewModelFactory).get(TaskViewModel::class.java)

        buttonAddTask.setOnClickListener {
            addTask()
        }

        setupPrioritySpinner()
    }

    private fun setupPrioritySpinner() {
//        val priorities = arrayOf("High", "Medium", "Low")
        val priorities = resources.getStringArray(R.array.priorities)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, priorities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPriority.adapter = adapter
    }

    private fun addTask() {
        val name = editTextName.text.toString().trim()
        val description = editTextDescription.text.toString().trim()
        val priority = spinnerPriority.selectedItem.toString()
        val deadline = getFormattedDate(datePickerDeadline.year, datePickerDeadline.month, datePickerDeadline.dayOfMonth)

        if (name.isNotEmpty()) {
            val task = Task(name = name, description = description, priority = priority, deadline = deadline)
            taskViewModel.insert(task)
            finish()
        } else {
            Toast.makeText(this, "Task name cannot be empty", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFormattedDate(year: Int, month: Int, dayOfMonth: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}
