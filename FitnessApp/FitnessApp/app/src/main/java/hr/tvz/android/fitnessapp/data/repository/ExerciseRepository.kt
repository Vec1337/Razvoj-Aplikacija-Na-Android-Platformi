package hr.tvz.android.fitnessapp.data.repository

import androidx.lifecycle.LiveData
import hr.tvz.android.fitnessapp.data.db.ExerciseDao
import hr.tvz.android.fitnessapp.data.model.Exercise

class ExerciseRepository(private val exerciseDao: ExerciseDao) {

    // Returns all exercises for a specific workout
    fun getExercisesForWorkout(workoutId: Long): LiveData<List<Exercise>> {
        return exerciseDao.getExercisesByWorkoutId(workoutId)
    }

    // Insert a new exercise
    suspend fun insert(exercise: Exercise) {
        exerciseDao.insert(exercise)
    }

    // Delete an exercise
    suspend fun delete(exercise: Exercise) {
        exerciseDao.delete(exercise)
    }

    // Optional: update an exercise
    suspend fun update(exercise: Exercise) {
        exerciseDao.update(exercise)
    }

    // ✅ New function for synchronous fetch in coroutines
    suspend fun getExercisesByWorkoutIdSync(workoutId: Long): List<Exercise> {
        return exerciseDao.getExercisesByWorkoutIdSync(workoutId)
    }
}
