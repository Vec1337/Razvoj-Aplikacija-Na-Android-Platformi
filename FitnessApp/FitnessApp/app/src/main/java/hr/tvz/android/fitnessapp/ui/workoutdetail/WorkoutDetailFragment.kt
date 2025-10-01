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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import hr.tvz.android.fitnessapp.R
import hr.tvz.android.fitnessapp.data.db.AppDatabase
import hr.tvz.android.fitnessapp.data.model.Exercise
import hr.tvz.android.fitnessapp.data.model.WorkoutLog
import hr.tvz.android.fitnessapp.data.repository.ExerciseRepository
import hr.tvz.android.fitnessapp.data.repository.WorkoutLogRepository
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

    private val workoutLogRepository by lazy {
        WorkoutLogRepository(AppDatabase.getDatabase(requireContext()).workoutLogDao())
    }

    private val viewModel: WorkoutDetailViewModel by viewModels {
        WorkoutDetailViewModelFactory(exerciseRepository)
    }

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

        // 🔹 Long-press edit exercise
        adapter.onExerciseLongClick = { exercise ->
            showEditExerciseDialog(exercise)
        }

        // 🔹 Click exercise → toggle completed
        adapter.onExerciseClick = { exercise ->
            val toggledExercise = exercise.copy(isCompleted = !exercise.isCompleted)
            lifecycleScope.launch {
                exerciseRepository.update(toggledExercise)
            }
        }

        // 🔹 Swipe-to-delete for exercises
        val itemTouchHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(
                recyclerView: androidx.recyclerview.widget.RecyclerView,
                viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
                target: androidx.recyclerview.widget.RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val exerciseToDelete = adapter.exercises[position]

                lifecycleScope.launch {
                    exerciseRepository.delete(exerciseToDelete)

                    val workout = workoutRepository.getWorkoutById(workoutId)
                    if (workout != null) {
                        val updatedWorkout = workout.copy(
                            exerciseCount = (workout.exerciseCount - 1).coerceAtLeast(0)
                        )
                        workoutRepository.update(updatedWorkout)
                    }
                }

                Snackbar.make(binding.root, "Exercise deleted", Snackbar.LENGTH_LONG)
                    .setAnchorView(R.id.bottom_navigation)
                    .setAction("UNDO") {
                        lifecycleScope.launch {
                            exerciseRepository.insert(exerciseToDelete)

                            val workout = workoutRepository.getWorkoutById(workoutId)
                            if (workout != null) {
                                val restoredWorkout =
                                    workout.copy(exerciseCount = workout.exerciseCount + 1)
                                workoutRepository.update(restoredWorkout)
                            }
                        }
                    }
                    .show()
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewExercises)

        binding.buttonAddExercise.setOnClickListener {
            showAddExerciseDialog(workoutId)
        }

        binding.buttonCompleteWorkout.setOnClickListener {
            lifecycleScope.launch {
                val workout = workoutRepository.getWorkoutById(workoutId)
                if (workout != null) {
                    // ✅ Log workout
                    val log = WorkoutLog(
                        workoutId = workoutId,
                        workoutName = workout.name,
                        duration = workout.duration
                    )
                    workoutLogRepository.insert(log)

                    // ✅ Reset all exercises to uncompleted
                    val exercises = exerciseRepository.getExercisesByWorkoutIdSync(workoutId)
                    exercises.forEach { ex ->
                        exerciseRepository.update(ex.copy(isCompleted = false))
                    }

                    Toast.makeText(requireContext(), "Workout logged!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Workout not found", Toast.LENGTH_SHORT).show()
                }
            }
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
                        reps = reps,
                        isCompleted = false
                    )

                    viewModel.addExercise(exercise)

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

    private fun showEditExerciseDialog(exercise: Exercise) {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_add_exercise, null)

        val nameEdit = dialogView.findViewById<EditText>(R.id.editTextExerciseName)
        val setsEdit = dialogView.findViewById<EditText>(R.id.editTextSets)
        val repsEdit = dialogView.findViewById<EditText>(R.id.editTextReps)

        // Pre-fill existing values
        nameEdit.setText(exercise.name)
        setsEdit.setText(exercise.sets.toString())
        repsEdit.setText(exercise.reps.toString())

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Exercise")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val name = nameEdit.text.toString()
                val sets = setsEdit.text.toString().toIntOrNull() ?: 0
                val reps = repsEdit.text.toString().toIntOrNull() ?: 0

                if (name.isNotBlank() && sets > 0 && reps > 0) {
                    val updatedExercise = exercise.copy(name = name, sets = sets, reps = reps)
                    lifecycleScope.launch {
                        exerciseRepository.update(updatedExercise)
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
