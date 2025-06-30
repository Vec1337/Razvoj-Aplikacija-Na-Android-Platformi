package hr.tvz.android.listdetailapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Car (
    val name: String,
    val manufacturer: String,
    val year: Int,
    val description: String,
    val imageResId: Int,
    val webLink: String
) : Parcelable