package hr.tvz.android.fitnessapp.data.repository

import androidx.lifecycle.LiveData
import hr.tvz.android.fitnessapp.data.db.ExerciseDao
import hr.tvz.android.fitnessapp.data.model.Exercise

class ExerciseRepository(private val exerciseDao: ExerciseDao) {

    fun getExercisesForWorkout(workoutId: Long): LiveData<List<Exercise>> {
        return exerciseDao.getExercisesByWorkoutId(workoutId)
    }

    suspend fun insert(exercise: Exercise) {
        exerciseDao.insert(exercise)
    }

    suspend fun delete(exercise: Exercise) {
        exerciseDao.delete(exercise)
    }

    suspend fun update(exercise: Exercise) {
        exerciseDao.update(exercise)
    }

    suspend fun getExercisesByWorkoutIdSync(workoutId: Long): List<Exercise> {
        return exerciseDao.getExercisesByWorkoutIdSync(workoutId)
    }
}
