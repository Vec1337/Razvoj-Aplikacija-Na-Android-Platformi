package hr.tvz.android.listdetailapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class CarImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_image)

        val imageView = findViewById<ImageView>(R.id.imageFullScreen)

        val imageResId = intent.getIntExtra("car_image_res_id", 0)
        if(imageResId != 0) {
            imageView.setImageResource(imageResId)

            imageView.rotation = 0f
            imageView.animate()
                .rotation(360f)
                .setDuration(700)
                .start()
        }

        imageView.setOnClickListener{
            finish()
        }
    }



}