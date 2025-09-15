package hr.tvz.android.fitnessapp.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import hr.tvz.android.fitnessapp.data.db.AppDatabase
import hr.tvz.android.fitnessapp.data.repository.WorkoutRepository
import hr.tvz.android.fitnessapp.databinding.FragmentAddWorkoutBinding

class AddWorkoutFragment : Fragment() {

    private var _binding: FragmentAddWorkoutBinding? = null
    private val binding get() = _binding!!

    // repository i ViewModel inicijaliziramo s DAO-om iz baze
    private val viewModel: AddWorkoutViewModel by viewModels {
        val dao = AppDatabase.getDatabase(requireContext()).workoutDao()
        val repository = WorkoutRepository(dao)
        AddWorkoutViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddWorkoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonAddWorkout.setOnClickListener {
            val name = binding.editTextWorkoutName.text.toString()
            val durationText = binding.editTextWorkoutDuration.text.toString()

            if (name.isNotEmpty() && durationText.isNotEmpty()) {
                val duration = durationText.toInt()
                viewModel.addWorkout(name, duration)
                Toast.makeText(requireContext(), "Workout added!", Toast.LENGTH_SHORT).show()
                binding.editTextWorkoutName.text.clear()
                binding.editTextWorkoutDuration.text.clear()
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
