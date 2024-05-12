package com.example.taskmanagement.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import com.example.taskmanagement.R
import com.example.taskmanagement.data.database.TaskDatabase
import com.example.taskmanagement.data.model.Task
import com.example.taskmanagement.data.repository.TaskRepository
import com.example.taskmanagement.viewmodel.TaskViewModel
import com.example.taskmanagement.viewmodel.TaskViewModelFactory

class UpdateTaskActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var spinnerPriority: Spinner
    private lateinit var datePickerDeadline: DatePicker
    private lateinit var buttonUpdateTask: Button

    private lateinit var taskViewModel: TaskViewModel
    private var taskId: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_task)

        editTextName = findViewById(R.id.editTextNameUpdate)
        editTextDescription = findViewById(R.id.editTextDescriptionUpdate)
        spinnerPriority = findViewById(R.id.spinnerPriorityUpdate)
        datePickerDeadline = findViewById(R.id.datePickerDeadlineUpdate)
        buttonUpdateTask = findViewById(R.id.buttonUpdateTaskUpdate)


        val taskDao = TaskDatabase.getInstance(applicationContext).taskDao()
        val repository = TaskRepository(taskDao)
        val viewModelFactory = TaskViewModelFactory(repository)
        taskViewModel = ViewModelProvider(this, viewModelFactory).get(TaskViewModel::class.java)

        // Retrieve task ID from intent extras
        taskId = intent.getLongExtra("TASK_ID", 0)

        // Set onClickListener for update button
        buttonUpdateTask.setOnClickListener {
            updateTask()
        }

        setupPrioritySpinner()
        fetchTaskDetails()  // Fetch and populate UI with task details for editing

    }

    private fun fetchTaskDetails() {
        taskViewModel.getTaskById(taskId).observe(this) { task ->
            task?.let {
                // Populate UI with task details for editing
                editTextName.setText(it.name)
                editTextDescription.setText(it.description)
                val priorities = resources.getStringArray(R.array.priorities)
                val priorityIndex = priorities.indexOf(it.priority)
                if (priorityIndex != -1) {
                    spinnerPriority.setSelection(priorityIndex)
                }
//                val priorityIndex = resources.getStringArray(R.array.priorities).indexOf(it.priority)
//                spinnerPriority.setSelection(priorityIndex)

                val deadlineArray = it.deadline.split("/")
                val day = deadlineArray[0].toInt()
                val month = deadlineArray[1].toInt() - 1 // Months are 0-indexed in DatePicker
                val year = deadlineArray[2].toInt()
                datePickerDeadline.updateDate(year, month, day)
            }
        }
    }
    private fun setupPrioritySpinner() {
//        val priorities = arrayOf("High", "Medium", "Low")
        val priorities = resources.getStringArray(R.array.priorities)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, priorities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPriority.adapter = adapter
    }

    private fun updateTask() {
        val name = editTextName.text.toString().trim()
        val description = editTextDescription.text.toString().trim()
        val priority = spinnerPriority.selectedItem.toString()
        val deadline = "${datePickerDeadline.dayOfMonth}/${datePickerDeadline.month + 1}/${datePickerDeadline.year}"

        if (name.isNotEmpty()) {
            val updatedTask = Task(taskId, name, description, priority, deadline)
            taskViewModel.update(updatedTask)
            finish()
        } else {
            // You can replace this with a Toast or any other UI feedback mechanism
            editTextName.error = "Task name cannot be empty"
        }
    }
}