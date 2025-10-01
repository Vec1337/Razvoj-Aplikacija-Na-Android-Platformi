package hr.tvz.android.fitnessapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hr.tvz.android.fitnessapp.data.db.ExerciseDao
import hr.tvz.android.fitnessapp.data.db.WorkoutDao
import hr.tvz.android.fitnessapp.data.model.Exercise
import hr.tvz.android.fitnessapp.data.model.Workout
import hr.tvz.android.fitnessapp.data.model.WorkoutLog

@Database(entities = [Workout::class, Exercise::class, WorkoutLog::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun workoutDao(): WorkoutDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutLogDao(): WorkoutLogDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fitness_database"
                )
                    // Optional: fallbackToDestructiveMigration allows DB to reset if no migration provided
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

