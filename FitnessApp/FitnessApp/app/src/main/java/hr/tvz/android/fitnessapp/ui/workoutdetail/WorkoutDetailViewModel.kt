package hr.tvz.android.fitnessapp.ui.workoutdetail

import androidx.lifecycle.*
import hr.tvz.android.fitnessapp.data.model.Exercise
import hr.tvz.android.fitnessapp.data.repository.ExerciseRepository
import kotlinx.coroutines.launch

class WorkoutDetailViewModel(private val repository: ExerciseRepository) : ViewModel() {

    private val workoutId = MutableLiveData<Long>()

    val exercises: LiveData<List<Exercise>> = workoutId.switchMap { id ->
        repository.getExercisesForWorkout(id)
    }

    fun setWorkoutId(id: Long) {
        workoutId.value = id
    }

    fun addExercise(exercise: Exercise) {
        viewModelScope.launch {
            repository.insert(exercise)
        }
    }
}

class WorkoutDetailViewModelFactory(private val repository: ExerciseRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkoutDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WorkoutDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
