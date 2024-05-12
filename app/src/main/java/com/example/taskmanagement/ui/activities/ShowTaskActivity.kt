package com.example.taskmanagement.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.taskmanagement.R
import com.example.taskmanagement.data.database.TaskDatabase
import com.example.taskmanagement.data.repository.TaskRepository
import com.example.taskmanagement.viewmodel.TaskViewModel
import com.example.taskmanagement.viewmodel.TaskViewModelFactory

class ShowTaskActivity : AppCompatActivity() {

    private lateinit var textViewTaskName: TextView
    private lateinit var textViewTaskDescription: TextView
    private lateinit var textViewTaskPriority: TextView
    private lateinit var textViewTaskDeadline: TextView

    private lateinit var taskViewModel: TaskViewModel
    private var taskId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_task)

        textViewTaskName = findViewById(R.id.textViewTaskName)
        textViewTaskDescription = findViewById(R.id.textViewTaskDescription)
        textViewTaskPriority = findViewById(R.id.textViewTaskPriority)
        textViewTaskDeadline = findViewById(R.id.textViewTaskDeadline)


        val taskDao = TaskDatabase.getInstance(applicationContext).taskDao()
        val repository = TaskRepository(taskDao)
        val viewModelFactory = TaskViewModelFactory(repository)
        taskViewModel = ViewModelProvider(this, viewModelFactory).get(TaskViewModel::class.java)

        taskId = intent.getLongExtra("TASK_ID", 0)
        /*val taskName = intent.getStringExtra("TASK_NAME") ?: ""
        val taskDescription = intent.getStringExtra("TASK_DESCRIPTION") ?: ""
        val taskPriority = intent.getStringExtra("TASK_PRIORITY") ?: ""
        val taskDeadline = intent.getStringExtra("TASK_DEADLINE") ?: ""*/

        fetchTaskDetails()

    }

    private fun fetchTaskDetails() {
        taskViewModel.getTaskById(taskId).observe(this) { task ->
            task?.let {
                textViewTaskName.text = it.name
                textViewTaskDescription.text = it.description
                textViewTaskPriority.text = it.priority
                textViewTaskDeadline.text = it.deadline
            }
        }
    }
}