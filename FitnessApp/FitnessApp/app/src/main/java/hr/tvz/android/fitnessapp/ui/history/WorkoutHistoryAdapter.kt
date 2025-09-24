package hr.tvz.android.fitnessapp.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.tvz.android.fitnessapp.R
import hr.tvz.android.fitnessapp.data.model.WorkoutLog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WorkoutHistoryAdapter(private var logs: List<WorkoutLog>) :
    RecyclerView.Adapter<WorkoutHistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.textViewWorkoutName)
        val time = itemView.findViewById<TextView>(R.id.textViewWorkoutTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout_log, parent, false)
        return HistoryViewHolder(view)
    }

    override fun getItemCount(): Int = logs.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val log = logs[position]
        holder.name.text = log.workoutName
        holder.time.text = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
            .format(Date(log.completedAt))
    }

    fun updateData(newLogs: List<WorkoutLog>) {
        logs = newLogs
        notifyDataSetChanged()
    }
}