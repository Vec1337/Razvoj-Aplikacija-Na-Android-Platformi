package hr.tvz.android.fitnessapp.ui.workoutdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.navArgs
import hr.tvz.android.fitnessapp.R
import hr.tvz.android.fitnessapp.data.db.AppDatabase
import hr.tvz.android.fitnessapp.data.model.Exercise
import hr.tvz.android.fitnessapp.data.repository.ExerciseRepository
import hr.tvz.android.fitnessapp.data.repository.WorkoutRepository
import hr.tvz.android.fitnessapp.databinding.FragmentWorkoutDetailBinding
import hr.tvz.android.fitnessapp.ui.workoutdetail.adapter.ExerciseAdapter
import kotlinx.coroutines.launch

class WorkoutDetailFragment : Fragment() {

    private var _binding: FragmentWorkoutDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ExerciseAdapter

    private val exerciseRepository by lazy {
        ExerciseRepository(AppDatabase.getDatabase(requireContext()).exerciseDao())
    }

    private val workoutRepository by lazy {
        WorkoutRepository(AppDatabase.getDatabase(requireContext()).workoutDao())
    }

    private val viewModel: WorkoutDetailViewModel by viewModels {
        WorkoutDetailViewModelFactory(exerciseRepository)
    }

    // ✅ Safe Args
    private val args: WorkoutDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val workoutId = args.workoutId
        val workoutName = args.workoutName

        binding.textViewWorkoutName.text = workoutName

        adapter = ExerciseAdapter(emptyList())
        binding.recyclerViewExercises.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewExercises.adapter = adapter

        viewModel.setWorkoutId(workoutId)

        viewModel.exercises.observe(viewLifecycleOwner) { exercises ->
            adapter.updateData(exercises)
        }

        binding.buttonAddExercise.setOnClickListener {
            showAddExerciseDialog(workoutId)
        }
    }

    private fun showAddExerciseDialog(workoutId: Long) {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_add_exercise, null)

        val nameEdit = dialogView.findViewById<EditText>(R.id.editTextExerciseName)
        val setsEdit = dialogView.findViewById<EditText>(R.id.editTextSets)
        val repsEdit = dialogView.findViewById<EditText>(R.id.editTextReps)

        AlertDialog.Builder(requireContext())
            .setTitle("Add Exercise")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = nameEdit.text.toString()
                val sets = setsEdit.text.toString().toIntOrNull() ?: 0
                val reps = repsEdit.text.toString().toIntOrNull() ?: 0

                if (name.isNotBlank() && sets > 0 && reps > 0) {
                    val exercise = Exercise(
                        workoutId = workoutId,
                        name = name,
                        sets = sets,
                        reps = reps
                    )

                    // Add exercise to DB
                    viewModel.addExercise(exercise)

                    // Update exerciseCount in the corresponding workout
                    lifecycleScope.launch {
                        val workout = workoutRepository.getWorkoutById(workoutId)
                        if (workout != null) {
                            val updatedWorkout =
                                workout.copy(exerciseCount = workout.exerciseCount + 1)
                            workoutRepository.update(updatedWorkout)
                        }
                    }

                } else {
                    Toast.makeText(requireContext(), "Enter valid values", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
