package hr.tvz.android.fitnessapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.tvz.android.fitnessapp.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Dodaj HomeFragment ako još nije dodan
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }
    }
}
