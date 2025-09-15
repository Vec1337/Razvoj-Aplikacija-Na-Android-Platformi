package hr.tvz.android.fitnessapp.ui.home


import androidx.lifecycle.*
import hr.tvz.android.fitnessapp.data.model.Workout
import hr.tvz.android.fitnessapp.data.repository.WorkoutRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: WorkoutRepository) : ViewModel() {

    val workouts: LiveData<List<Workout>> = repository.allWorkouts

    fun deleteWorkout(workout: Workout) {
        viewModelScope.launch {
            repository.delete(workout)
        }
    }
}

class HomeViewModelFactory(private val repository: WorkoutRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}