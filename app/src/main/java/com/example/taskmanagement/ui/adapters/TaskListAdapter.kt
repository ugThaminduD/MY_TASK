package com.example.taskmanagement.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanagement.R
import com.example.taskmanagement.data.model.Task
import com.example.taskmanagement.ui.activities.MainActivity

//class TaskListAdapter : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(TaskDiffCallback()) {
class TaskListAdapter(private val listener: MainActivity) : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(TaskDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = getItem(position)
        holder.bind(currentTask)
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val nameTextView: CheckBox = itemView.findViewById(R.id.cbTodo)
        private val itemTaskName: TextView = itemView.findViewById(R.id.itemTaskName)
        private val editImageView: ImageView = itemView.findViewById(R.id.ivEdit)
        private val deleteImageView: ImageView = itemView.findViewById(R.id.ivDelete)

        init {
            itemView.setOnClickListener(this)
            nameTextView.setOnClickListener(this)
            itemTaskName.setOnClickListener(this)
            editImageView.setOnClickListener(this)
            deleteImageView.setOnClickListener(this)
        }

        fun bind(task: Task) {
            itemTaskName.text = task.name
        }

        override fun onClick(view: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val task = getItem(position)
                when (view.id) {
                    R.id.ivEdit -> {
                        listener.onEditClick(task)
                    }
                    R.id.ivDelete -> {
                        listener.onDeleteClick(task)
                    }
                    R.id.itemTaskName -> {
                        listener.onItemClickShow(task)
                    }
                    else -> {
                        listener.onItemClick(task)
                    }
                }
            }
        }
    }
    interface OnItemClickListener {
        fun onItemClickShow(task: Task)

        fun onItemClick(task: Task)
        fun onEditClick(task: Task)
        fun onDeleteClick(task: Task)
//        fun onCompleteClick(task: Task)

    }

    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
}