package com.dicoding.todoapp.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.dicoding.todoapp.utils.FilterUtils.getFilteredQuery
import com.dicoding.todoapp.utils.TasksFilterType
import java.util.*

class TaskRepository(private val tasksDao: TaskDao) {

    companion object {
        const val PAGE_SIZE = 30
        const val PLACEHOLDERS = true

        @Volatile
        private var instance: TaskRepository? = null

        fun getInstance(context: Context): TaskRepository {
            return instance ?: synchronized(this) {
                if (instance == null) {
                    val database = TaskDatabase.getInstance(context)
                    instance = TaskRepository(database.taskDao())
                }
                return instance as TaskRepository
            }

        }
    }

    fun getTasks(filter: TasksFilterType): LiveData<PagedList<Task>> {
        val query = getFilteredQuery(filter)
        val tasks = tasksDao.getTasks(query)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(PLACEHOLDERS)
            .setInitialLoadSizeHint(30)
            .setPageSize(PAGE_SIZE)
            .build()
        return LivePagedListBuilder(tasks, config).build()
    }

    fun getTaskById(taskId: Int): LiveData<Task> {
        return tasksDao.getTaskById(taskId)
    }

    fun getNearestActiveTask(): Task {
        val calendar: Calendar = Calendar.getInstance()
        val dateTodayLong = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        val dateTomorrow: Date = calendar.time
        val dateTomorrowLong: Long = dateTomorrow.time
        return tasksDao.getNearestActiveTask(dateTodayLong, dateTomorrowLong)
    }

    suspend fun insertTask(newTask: Task): Long{
        return tasksDao.insertTask(newTask)
    }

    suspend fun deleteTask(task: Task) {
        tasksDao.deleteTask(task)
    }

    suspend fun completeTask(task: Task, isCompleted: Boolean) {
        task.isCompleted = isCompleted
        tasksDao.updateCompleted(task)
    }
}