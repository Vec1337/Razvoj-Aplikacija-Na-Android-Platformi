package hr.tvz.android.fitnessapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import hr.tvz.android.fitnessapp.R
import hr.tvz.android.fitnessapp.data.db.AppDatabase
import hr.tvz.android.fitnessapp.data.repository.WorkoutRepository
import hr.tvz.android.fitnessapp.databinding.FragmentHomeBinding
import hr.tvz.android.fitnessapp.ui.add.AddWorkoutFragment
import hr.tvz.android.fitnessapp.ui.home.adapter.WorkoutAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: WorkoutAdapter
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize repository and ViewModel safely
        val repository = WorkoutRepository(AppDatabase.getDatabase(requireContext()).workoutDao())
        homeViewModel = HomeViewModelFactory(repository).create(HomeViewModel::class.java)

        // Setup RecyclerView
        adapter = WorkoutAdapter(emptyList())
        binding.recyclerViewHome.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewHome.adapter = adapter

        // Observe database changes
        homeViewModel.workouts.observe(viewLifecycleOwner, Observer { workouts ->
            adapter.updateData(workouts)
        })

        // Button to add new workout
        binding.buttonAddWorkout.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AddWorkoutFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
