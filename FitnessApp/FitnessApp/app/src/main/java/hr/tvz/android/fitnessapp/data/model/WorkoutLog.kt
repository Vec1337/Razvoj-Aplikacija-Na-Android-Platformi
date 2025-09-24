package hr.tvz.android.fitnessapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_logs")
data class WorkoutLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val workoutId: Long,
    val workoutName: String,
    val completedAt: Long = System.currentTimeMillis(), // timestamp
    val duration: Int = 0 // optional: store duration in minutes
)
