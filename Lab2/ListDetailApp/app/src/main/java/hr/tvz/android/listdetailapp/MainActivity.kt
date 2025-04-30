package hr.tvz.android.listdetailapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var carAdapter: CarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var carList = listOf(
            Car("S Class", "Mercedes-Benz", 2022, "Luxury",R.drawable.mercedes_s, "https://www.mercedes-benz.hr/models/s-class-w223-805/"),
            Car("M3", "BMW", 2021, "Sporty and luxurious.", R.drawable.bmw_m3, "https://www.bmw.com/en/models/m3.html"),
            Car("RS6 Avant", "Audi", 2022, "Performance meets practicality.", R.drawable.audi_rs6, "https://www.audi.com/en/models/rs6.html")
        )

        recyclerView = findViewById(R.id.recyclerViewCars)
        recyclerView.layoutManager = LinearLayoutManager(this)
        carAdapter = CarAdapter(carList) { selectedCar ->
            val intent = Intent(this, CarDetailActivity::class.java)
            intent.putExtra("car", selectedCar)
            startActivity(intent)

        }

        recyclerView.adapter = carAdapter

    }
}