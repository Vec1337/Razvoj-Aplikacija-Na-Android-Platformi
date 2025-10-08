package hr.tvz.android.fitnessapp.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import hr.tvz.android.fitnessapp.R
import hr.tvz.android.fitnessapp.data.db.AppDatabase
import hr.tvz.android.fitnessapp.databinding.FragmentWorkoutHistoryBinding
import kotlinx.coroutines.launch

class WorkoutHistoryFragment : Fragment() {

    private var _binding: FragmentWorkoutHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: WorkoutLogAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = WorkoutLogAdapter(emptyList())
        binding.recyclerViewWorkoutLogs.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewWorkoutLogs.adapter = adapter

        val repository = AppDatabase.getDatabase(requireContext()).workoutLogDao()

        repository.getAllLogs().observe(viewLifecycleOwner) { logs ->
            adapter.updateData(logs)
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
                val logToDelete = adapter.logs[position]

                lifecycleScope.launch {
                    repository.delete(logToDelete)
                }

                Snackbar.make(requireView(), "Workout log deleted", Snackbar.LENGTH_LONG)
                    .setAnchorView(R.id.bottom_navigation)
                    .setAction("UNDO") {
                        lifecycleScope.launch {
                            repository.insert(logToDelete)
                        }
                    }.show()
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.recyclerViewWorkoutLogs)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
