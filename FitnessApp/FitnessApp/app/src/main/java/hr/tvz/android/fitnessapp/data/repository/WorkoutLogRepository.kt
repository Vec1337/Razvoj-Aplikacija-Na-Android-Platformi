package hr.tvz.android.fitnessapp.data.repository

import androidx.lifecycle.LiveData
import hr.tvz.android.fitnessapp.data.db.WorkoutLogDao
import hr.tvz.android.fitnessapp.data.model.WorkoutLog

class WorkoutLogRepository(private val dao: WorkoutLogDao) {

    val allLogs: LiveData<List<WorkoutLog>> = dao.getAllLogs()

    suspend fun insert(log: WorkoutLog) = dao.insert(log)

    suspend fun delete(log: WorkoutLog) = dao.delete(log)
}
