package com.example.taskmanagement.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanagement.R
import com.example.taskmanagement.data.database.TaskDatabase
import com.example.taskmanagement.data.model.Task
import com.example.taskmanagement.data.repository.TaskRepository
import com.example.taskmanagement.viewmodel.TaskViewModel
import com.example.taskmanagement.ui.adapters.TaskListAdapter
import com.example.taskmanagement.viewmodel.TaskViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), TaskListAdapter.OnItemClickListener {

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskListAdapter
    private lateinit var btnAddTodo: FloatingActionButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val taskDao = TaskDatabase.getInstance(applicationContext).taskDao()
        val repository = TaskRepository(taskDao)
        val viewModelFactory = TaskViewModelFactory(repository)

        taskViewModel = ViewModelProvider(this, viewModelFactory).get(TaskViewModel::class.java)
//        taskViewModel = ViewModelProvider(this, TaskViewModelFactory(repository)).get(TaskViewModel::class.java)


        // Initialize RecyclerView and adapter
        recyclerView = findViewById(R.id.rvTodoList)
        adapter = TaskListAdapter(this)            //////////////////////////////////////////////

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Observe LiveData from ViewModel
        taskViewModel.allTasks.observe(this) { tasks ->
            adapter.submitList(tasks)
        }

        btnAddTodo = findViewById(R.id.btnAddTodo)
        btnAddTodo.setOnClickListener {
            startActivity(Intent(this@MainActivity, AddTaskActivity::class.java))

        }
    }
                                                        ////////////////////////
    override fun onItemClickShow(task: Task){
        val intent = Intent(this, ShowTaskActivity::class.java)
        intent.putExtra("TASK_ID", task.id)
        startActivity(intent)
    }

    override fun onItemClick(task: Task) {
        Toast.makeText(this, "Clicked on task: ${task.name}", Toast.LENGTH_SHORT).show()
    }

    override fun onEditClick(task: Task) {
        val intent = Intent(this, UpdateTaskActivity::class.java)
        intent.putExtra("TASK_ID", task.id)
        startActivity(intent)
    }

    override fun onDeleteClick(task: Task) {
        taskViewModel.delete(task.id)
        Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show()
    }
}