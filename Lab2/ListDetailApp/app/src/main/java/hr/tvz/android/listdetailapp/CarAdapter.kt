package hr.tvz.android.listdetailapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CarAdapter(
    private val cars: List<Car>,
    private val onItemClick: (Car) -> Unit
) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    inner class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageCar: ImageView = itemView.findViewById(R.id.imageCar)
        private val textCarName: TextView = itemView.findViewById(R.id.textCarName)

        fun bind(car: Car) {
            imageCar.setImageResource(car.imageResId)
            textCarName.text = car.name

            itemView.setOnClickListener {
                onItemClick(car)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarAdapter.CarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarAdapter.CarViewHolder, position: Int) {
        holder.bind(cars[position])
    }

    override fun getItemCount(): Int = cars.size

}