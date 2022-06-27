package com.dicoding.todoapp.ui.detail

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.todoapp.R
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.utils.DateConverter
import com.dicoding.todoapp.utils.TASK_ID
import com.google.android.material.textfield.TextInputEditText

class DetailTaskActivity : AppCompatActivity() {
    private lateinit var detailTaskViewModel: DetailTaskViewModel
    private lateinit var detailEdTitle: TextInputEditText
    private lateinit var detailEdDescription: TextInputEditText
    private lateinit var detailEdDueDate: TextInputEditText
    private lateinit var btnDeleteTask: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)
        setupViewModel()

        detailEdTitle = findViewById(R.id.detail_ed_title)
        detailEdDescription = findViewById(R.id.detail_ed_description)
        detailEdDueDate = findViewById(R.id.detail_ed_due_date)
        btnDeleteTask = findViewById(R.id.btn_delete_task)

        val taskId = intent.getIntExtra(TASK_ID, -1)

        detailTaskViewModel.setTaskId(taskId)

        detailTaskViewModel.task.observe(this){
            if(it != null){
                detailEdTitle.setText(it.title)
                detailEdDescription.setText(it.description)
                detailEdDueDate.setText(DateConverter.convertMillisToString(it.dueDateMillis))
                btnDeleteTask.setOnClickListener {
                    detailTaskViewModel.deleteTask()
                    finish()
                }
            }
        }
    }

    private fun setupViewModel(){
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        detailTaskViewModel = factory.create(DetailTaskViewModel::class.java)
    }
}