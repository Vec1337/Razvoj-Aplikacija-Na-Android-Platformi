package hr.tvz.android.listdetailapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class CarListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CarAdapter

    private var onCarSelected: ((Car) -> Unit)? = null

    fun setOnCarSelectedListener(listener: (Car) -> Unit) {
        onCarSelected = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_car_list, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewCars)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = CarAdapter(emptyList()) { car -> onCarSelected?.invoke(car) }
        recyclerView.adapter = adapter

        val db = AppDatabase.getDatabase(requireContext())
        val carDao = db.carDao()

        lifecycleScope.launch {
            if (carDao.getAllCars().isEmpty()) {
                carDao.insertAll(
                    listOf(
                        CarEntity(
                            name = "S Class",
                            manufacturer = "Mercedes-Benz",
                            year = 2022,
                            description = "Luxury",
                            imageResId = R.drawable.mercedes_s,
                            websiteUrl = "https://www.mercedes-benz.hr/models/s-class-w223-805/"
                        ),
                        CarEntity(
                            name = "M3",
                            manufacturer = "BMW",
                            year = 2021,
                            description = "Sporty and luxurious.",
                            imageResId = R.drawable.bmw_m3,
                            websiteUrl = "https://www.bmw.com/en/models/m3.html"
                        ),
                        CarEntity(
                            name = "RS6 Avant",
                            manufacturer = "Audi",
                            year = 2022,
                            description = "Performance meets practicality.",
                            imageResId = R.drawable.audi_rs6,
                            websiteUrl = "https://www.audi.com/en/models/rs6.html"
                        )
                    )
                )
            }

            val carsFromDb = carDao.getAllCars()
            val carsForUI = carsFromDb.map {
                Car(
                    name = it.name,
                    manufacturer = it.manufacturer,
                    year = it.year,
                    description = it.description,
                    imageResId = it.imageResId,
                    webLink = it.websiteUrl
                )
            }
            adapter.updateCars(carsForUI)
        }

        return view
    }
}
