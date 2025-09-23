package hr.tvz.android.fitnessapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setupWithNavController(navController)

        // FAB click navigates to AddWorkoutFragment
        val fabAddWorkout = findViewById<FloatingActionButton>(R.id.fabAddWorkout)

        // Toggle FAB visibility based on current destination
        navController.addOnDestinationChangedListener { _, destination, _ ->
            fabAddWorkout.visibility = when (destination.id) {
                R.id.homeFragment -> FloatingActionButton.VISIBLE
                else -> FloatingActionButton.GONE
            }
        }

        fabAddWorkout.setOnClickListener {
            navController.navigate(R.id.addWorkoutFragment)
        }
    }
}
