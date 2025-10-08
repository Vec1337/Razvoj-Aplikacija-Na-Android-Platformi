package hr.tvz.android.fitnessapp.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import hr.tvz.android.fitnessapp.data.db.AppDatabase
import hr.tvz.android.fitnessapp.data.model.Workout
import hr.tvz.android.fitnessapp.data.repository.WorkoutRepository
import hr.tvz.android.fitnessapp.databinding.FragmentAddWorkoutBinding
import kotlinx.coroutines.launch

class AddWorkoutFragment : Fragment() {

    private var _binding: FragmentAddWorkoutBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: WorkoutRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddWorkoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = WorkoutRepository(AppDatabase.getDatabase(requireContext()).workoutDao())

        binding.buttonSaveWorkout.setOnClickListener {
            val name = binding.editTextWorkoutName.text.toString()
            val duration = binding.editTextWorkoutTime.text.toString().toIntOrNull() ?: 0

            if (name.isNotBlank() && duration > 0) {
                val workout = Workout(name = name, duration = duration)

                lifecycleScope.launch {
                    repository.insert(workout)
                    parentFragmentManager.popBackStack()
                }
            } else {
                Toast.makeText(requireContext(), "Enter valid values", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
