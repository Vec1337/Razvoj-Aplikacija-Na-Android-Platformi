package hr.tvz.android.fitnessapp.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.tvz.android.fitnessapp.data.model.WorkoutLog
import hr.tvz.android.fitnessapp.databinding.ItemWorkoutLogBinding
import java.text.SimpleDateFormat
import java.util.*

class WorkoutLogAdapter(var logs: List<WorkoutLog>) :
    RecyclerView.Adapter<WorkoutLogAdapter.LogViewHolder>() {

    class LogViewHolder(val binding: ItemWorkoutLogBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val binding = ItemWorkoutLogBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val log = logs[position]

        holder.binding.textViewWorkoutName.text = log.workoutName

        val date = Date(log.completedAt)
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        holder.binding.textViewWorkoutTime.text = sdf.format(date)

        holder.binding.textViewDuration.text = "${log.duration} min"

        if (!log.completedExercises.isNullOrBlank()) {
            holder.binding.textViewCompletedExercises.visibility = View.VISIBLE

            val exercisesList = log.completedExercises
                .split("\n", ",")
                .filter { it.isNotBlank() }
                .joinToString("\n") { "• ${it.trim()}" }

            holder.binding.textViewCompletedExercises.text = exercisesList
        } else {
            holder.binding.textViewCompletedExercises.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = logs.size

    fun updateData(newLogs: List<WorkoutLog>) {
        logs = newLogs
        notifyDataSetChanged()
    }
}
