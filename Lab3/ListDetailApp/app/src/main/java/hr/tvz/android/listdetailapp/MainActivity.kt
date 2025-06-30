package hr.tvz.android.listdetailapp

import CarDetailFragment
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class MainActivity : AppCompatActivity() {

    private var isTwoPane = false
    private lateinit var carListFragment: CarListFragment
    private var carDetailFragment: CarDetailFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        isTwoPane = findViewById<View?>(R.id.fragmentContainerDetail) != null


        carListFragment = CarListFragment()


        carListFragment.setOnCarSelectedListener { selectedCar ->
            if (isTwoPane) {
                if (carDetailFragment == null) {
                    carDetailFragment = CarDetailFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerDetail, carDetailFragment!!)
                        .commit()
                }
                carDetailFragment?.setCar(selectedCar)
            } else {
                val intent = Intent(this, CarDetailActivity::class.java)
                intent.putExtra("car", selectedCar)
                startActivity(intent)
            }
        }


        val fragmentContainerId = if (isTwoPane) R.id.fragmentContainerList else R.id.fragmentContainer
        supportFragmentManager.beginTransaction()
            .replace(fragmentContainerId, carListFragment)
            .commit()
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Default Channel"
            val descriptionText = "Firebase notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("default_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}
