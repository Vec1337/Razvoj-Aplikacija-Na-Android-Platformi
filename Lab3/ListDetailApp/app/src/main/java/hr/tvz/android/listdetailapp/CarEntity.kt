package hr.tvz.android.listdetailapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cars")
data class CarEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val manufacturer: String,
    val year: Int,
    val description: String,
    val imageResId: Int,
    val websiteUrl: String
)