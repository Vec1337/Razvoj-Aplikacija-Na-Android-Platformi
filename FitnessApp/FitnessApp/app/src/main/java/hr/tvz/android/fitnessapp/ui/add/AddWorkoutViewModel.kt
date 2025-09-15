package hr.tvz.android.fitnessapp.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.tvz.android.fitnessapp.data.model.Workout
import hr.tvz.android.fitnessapp.data.repository.WorkoutRepository
import kotlinx.coroutines.launch

class AddWorkoutViewModel(private val repository: WorkoutRepository) : ViewModel() {

    fun addWorkout(name: String, duration: Int) {
        val workout = Workout(name = name, duration = duration)
        viewModelScope.launch {
            repository.insert(workout)
        }
    }
}

class AddWorkoutViewModelFactory(private val repository: WorkoutRepository) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddWorkoutViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddWorkoutViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
