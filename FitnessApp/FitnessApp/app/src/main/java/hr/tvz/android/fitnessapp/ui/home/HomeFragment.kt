package hr.tvz.android.fitnessapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import hr.tvz.android.fitnessapp.R
import hr.tvz.android.fitnessapp.data.db.AppDatabase
import hr.tvz.android.fitnessapp.data.model.Workout
import hr.tvz.android.fitnessapp.data.repository.WorkoutRepository
import hr.tvz.android.fitnessapp.databinding.FragmentHomeBinding
import hr.tvz.android.fitnessapp.ui.home.adapter.WorkoutAdapter
import hr.tvz.android.fitnessapp.ui.workoutdetail.WorkoutDetailFragment
import hr.tvz.android.fitnessapp.ui.add.AddWorkoutFragment

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
        }

        // Add workout button
        binding.buttonAddWorkout.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragment_container, AddWorkoutFragment())
                addToBackStack(null)
            }
        }
    }

    private fun openWorkoutDetail(workout: Workout) {
        parentFragmentManager.commit {
            replace(R.id.fragment_container, WorkoutDetailFragment(workout.id.toLong(), workout.name))
            addToBackStack(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
