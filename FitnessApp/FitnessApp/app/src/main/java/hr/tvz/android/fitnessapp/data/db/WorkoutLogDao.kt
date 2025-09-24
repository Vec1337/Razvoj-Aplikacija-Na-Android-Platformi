package hr.tvz.android.fitnessapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import hr.tvz.android.fitnessapp.data.model.WorkoutLog

@Dao
interface WorkoutLogDao {

    @Query("SELECT * FROM workout_logs ORDER BY completedAt DESC")
    fun getAllLogs(): LiveData<List<WorkoutLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: WorkoutLog)

    @Delete
    suspend fun delete(log: WorkoutLog)
}
