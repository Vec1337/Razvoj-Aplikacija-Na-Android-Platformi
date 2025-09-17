package hr.tvz.android.fitnessapp.data.repository

import androidx.lifecycle.LiveData
import hr.tvz.android.fitnessapp.data.db.WorkoutDao
import hr.tvz.android.fitnessapp.data.model.Workout

class WorkoutRepository(private val dao: WorkoutDao) {

    val allWorkouts: LiveData<List<Workout>> = dao.getAllWorkouts()

    // Insert a new workout
    suspend fun insert(workout: Workout) = dao.insert(workout)

    // Delete a workout
    suspend fun delete(workout: Workout) = dao.delete(workout)

    // Update a workout (for updating exerciseCount, duration, etc.)
    suspend fun update(workout: Workout) = dao.update(workout)

    suspend fun getWorkoutById(id: Long): Workout? = dao.getWorkoutById(id)
}
