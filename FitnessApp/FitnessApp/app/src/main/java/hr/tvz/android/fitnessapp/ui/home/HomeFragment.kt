package hr.tvz.android.fitnessapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import hr.tvz.android.fitnessapp.R
import hr.tvz.android.fitnessapp.data.db.AppDatabase
import hr.tvz.android.fitnessapp.data.model.Workout
import hr.tvz.android.fitnessapp.data.repository.WorkoutRepository
import hr.tvz.android.fitnessapp.databinding.FragmentHomeBinding
import hr.tvz.android.fitnessapp.ui.home.adapter.WorkoutAdapter
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: WorkoutAdapter
    private lateinit var repository: WorkoutRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize repository
        repository = WorkoutRepository(AppDatabase.getDatabase(requireContext()).workoutDao())

        // Initialize adapter with click listener
        adapter = WorkoutAdapter(emptyList()) { workout ->
            openWorkoutDetail(workout)
        }

        // Setup RecyclerView
        binding.recyclerViewHome.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewHome.adapter = adapter

        // Observe workouts
        repository.allWorkouts.observe(viewLifecycleOwner) { workouts ->
            adapter.updateData(workouts)

            if (workouts.isEmpty()) {
                binding.textViewEmpty.visibility = View.VISIBLE
                binding.recyclerViewHome.visibility = View.GONE
            } else {
                binding.textViewEmpty.visibility = View.GONE
                binding.recyclerViewHome.visibility = View.VISIBLE
            }
        }

        // Swipe-to-delete
        val itemTouchHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val workoutToDelete = adapter.currentList[position]

                // Delete from DB
                lifecycleScope.launch {
                    repository.delete(workoutToDelete)
                }

                // Show Undo Snackbar
                Snackbar.make(binding.root, "Workout deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                        lifecycleScope.launch {
                            repository.insert(workoutToDelete)
                        }
                    }.show()
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewHome)

        // Add workout button (empty state) -> use GLOBAL nav action
        binding.buttonAddWorkout.setOnClickListener {
            findNavController().navigate(R.id.action_global_addWorkoutFragment)
        }
    }

    private fun openWorkoutDetail(workout: Workout) {
        // Navigate with Safe Args
        val action = HomeFragmentDirections
            .actionHomeFragmentToWorkoutDetailFragment(workout.id.toLong(), workout.name)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
