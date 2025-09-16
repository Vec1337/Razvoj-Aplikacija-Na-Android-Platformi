package hr.tvz.android.fitnessapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import hr.tvz.android.fitnessapp.data.model.Exercise

@Dao
interface ExerciseDao {

    @Query("SELECT * FROM exercise WHERE workoutId = :workoutId")
    fun getExercisesByWorkoutId(workoutId: Long): LiveData<List<Exercise>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exercise: Exercise)

    @Update
    suspend fun update(exercise: Exercise)

    @Delete
    suspend fun delete(exercise: Exercise)
}
