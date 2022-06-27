package com.dicoding.todoapp.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface TaskDao {
    @Transaction
    @RawQuery(observedEntities = [Task::class])
    fun getTasks(query: SupportSQLiteQuery): DataSource.Factory<Int, Task>

    @Transaction
    @Query("SELECT * from tasks WHERE id == :taskId LIMIT 1")
    fun getTaskById(taskId: Int): LiveData<Task>

    @Transaction
    @Query("SELECT * FROM tasks WHERE dueDate <= :dueDateInLong AND dueDate >= :dateTodayLong AND completed = 0 ORDER BY id ASC LIMIT 1" )
    fun getNearestActiveTask(dateTodayLong: Long, dueDateInLong: Long): Task

    @Insert
    suspend fun insertTask(task: Task): Long

    @Insert
    suspend fun insertAll(vararg tasks: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Update
    suspend fun updateCompleted(task: Task)
}
