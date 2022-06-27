package com.dicoding.todoapp.ui.add

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.todoapp.R
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.utils.DatePickerFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener {
    private var dueDateMillis: Long = System.currentTimeMillis()
    private lateinit var addTaskViewModel: AddTaskViewModel
    private lateinit var addEdTitle: TextInputEditText
    private lateinit var addEdDescription: TextInputEditText
    private lateinit var addTvDueDate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        setupViewModel()

        addEdTitle = findViewById(R.id.add_ed_title)
        addEdDescription = findViewById(R.id.add_ed_description)
        addTvDueDate = findViewById(R.id.add_tv_due_date)

        supportActionBar?.title = getString(R.string.add_task)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                val taskTitle = addEdTitle.text.toString()
                val taskDesc = addEdDescription.text.toString()
                val taskDueDate = addTvDueDate.text
                when {
                    taskTitle.isEmpty() -> Toast.makeText(this, "Please insert title first!", Toast.LENGTH_LONG).show()
                    taskDesc.isEmpty() -> Toast.makeText(this, "Please insert description first!", Toast.LENGTH_LONG).show()
                    taskDueDate.isEmpty() -> Toast.makeText(this, "Please insert due date first!", Toast.LENGTH_LONG).show()
                    else -> {
                        val task = Task(0, taskTitle, taskDesc, dueDateMillis)
                        addTaskViewModel.addTask(task)
                        Toast.makeText(this, "New task created successfully!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showDatePicker(view: View) {
        val dialogFragment = DatePickerFragment()
        dialogFragment.show(supportFragmentManager, "datePicker")
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        findViewById<TextView>(R.id.add_tv_due_date).text = dateFormat.format(calendar.time)

        dueDateMillis = calendar.timeInMillis
    }

    private fun setupViewModel(){
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        addTaskViewModel = factory.create(AddTaskViewModel::class.java)
    }
}