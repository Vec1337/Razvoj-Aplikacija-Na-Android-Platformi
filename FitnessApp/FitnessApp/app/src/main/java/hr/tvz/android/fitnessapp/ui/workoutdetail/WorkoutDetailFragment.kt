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
import androidx.recyclerview.widget.LinearLayoutManager
import hr.tvz.android.fitnessapp.R
import hr.tvz.android.fitnessapp.data.db.AppDatabase
import hr.tvz.android.fitnessapp.data.model.Exercise
import hr.tvz.android.fitnessapp.data.repository.ExerciseRepository
import hr.tvz.android.fitnessapp.databinding.FragmentWorkoutDetailBinding
import hr.tvz.android.fitnessapp.ui.workoutdetail.adapter.ExerciseAdapter

class WorkoutDetailFragment(private val workoutId: Long, private val workoutName: String) : Fragment() {

    private var _binding: FragmentWorkoutDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ExerciseAdapter

    private val repository by lazy {
        ExerciseRepository(AppDatabase.getDatabase(requireContext()).exerciseDao())
    }

    private val viewModel: WorkoutDetailViewModel by viewModels {
        WorkoutDetailViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewWorkoutName.text = workoutName

        adapter = ExerciseAdapter(emptyList())
        binding.recyclerViewExercises.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewExercises.adapter = adapter

        viewModel.setWorkoutId(workoutId)

        viewModel.exercises.observe(viewLifecycleOwner) { exercises ->
            adapter.updateData(exercises)
        }

        binding.buttonAddExercise.setOnClickListener {
            showAddExerciseDialog()
        }
    }

    private fun showAddExerciseDialog() {
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
                    viewModel.addExercise(exercise)
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
