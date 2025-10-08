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
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
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

        repository = WorkoutRepository(AppDatabase.getDatabase(requireContext()).workoutDao())

        adapter = WorkoutAdapter(emptyList()) { workout ->
            openWorkoutDetail(workout)
        }

        binding.recyclerViewHome.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewHome.adapter = adapter

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

        val itemTouchHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: androidx.recyclerview.widget.RecyclerView,
                viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
                target: androidx.recyclerview.widget.RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val workoutToDelete = adapter.currentList[position]

                lifecycleScope.launch {
                    repository.delete(workoutToDelete)
                }

                Snackbar.make(requireView(), "Workout deleted", Snackbar.LENGTH_LONG)
                    .setAnchorView(R.id.bottom_navigation) // pass resource ID to avoid ambiguity
                    .setAction("UNDO") {
                        lifecycleScope.launch {
                            repository.insert(workoutToDelete)
                        }
                    }.show()
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewHome)
    }

    private fun openWorkoutDetail(workout: Workout) {
        val action = HomeFragmentDirections
            .actionHomeFragmentToWorkoutDetailFragment(workout.id.toLong(), workout.name)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
