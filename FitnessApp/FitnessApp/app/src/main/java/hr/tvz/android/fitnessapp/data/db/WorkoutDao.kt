package hr.tvz.android.fitnessapp.data.db



import androidx.lifecycle.LiveData
import androidx.room.*
import hr.tvz.android.fitnessapp.data.model.Workout

@Dao
interface WorkoutDao {

    @Query("SELECT * FROM workouts")
    fun getAllWorkouts(): LiveData<List<Workout>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workout: Workout)

    @Delete
    suspend fun delete(workout: Workout)
}