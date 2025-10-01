package hr.tvz.android.fitnessapp.ui.workoutdetail.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.tvz.android.fitnessapp.R
import hr.tvz.android.fitnessapp.data.model.Exercise

class ExerciseAdapter(
    var exercises: List<Exercise>
) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    // 🔹 Callback for long-click (edit)
    var onExerciseLongClick: ((Exercise) -> Unit)? = null

    // 🔹 Callback for normal click (toggle completed)
    var onExerciseClick: ((Exercise) -> Unit)? = null

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.textViewExerciseName)
        val setsRepsText: TextView = itemView.findViewById(R.id.textViewSetsReps)
    }

    val currentList: List<Exercise>
        get() = exercises

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun getItemCount(): Int = exercises.size

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.nameText.text = exercise.name
        holder.setsRepsText.text = "${exercise.sets} sets x ${exercise.reps} reps"

        // ✅ Show completed state visually (strike-through if completed)
        if (exercise.isCompleted) {
            holder.nameText.paintFlags = holder.nameText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.setsRepsText.paintFlags = holder.setsRepsText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.nameText.paintFlags = holder.nameText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.setsRepsText.paintFlags = holder.setsRepsText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        // 🔹 Normal click → toggle completed
        holder.itemView.setOnClickListener {
            onExerciseClick?.invoke(exercise)
        }

        // 🔹 Long click → edit
        holder.itemView.setOnLongClickListener {
            onExerciseLongClick?.invoke(exercise)
            true
        }
    }

    fun updateData(newExercises: List<Exercise>) {
        exercises = newExercises
        notifyDataSetChanged()
    }
}
