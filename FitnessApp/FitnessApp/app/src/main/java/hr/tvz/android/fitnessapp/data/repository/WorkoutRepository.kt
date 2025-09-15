package hr.tvz.android.fitnessapp.data.repository

import androidx.lifecycle.LiveData
import hr.tvz.android.fitnessapp.data.db.WorkoutDao
import hr.tvz.android.fitnessapp.data.model.Workout

class WorkoutRepository(private val dao: WorkoutDao) {

    val allWorkouts: LiveData<List<Workout>> = dao.getAllWorkouts()

    suspend fun insert(workout: Workout) = dao.insert(workout)

    suspend fun delete(workout: Workout) = dao.delete(workout)
}