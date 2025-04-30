package hr.tvz.android.listdetailapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import org.w3c.dom.Text
import androidx.core.net.toUri

class CarDetailActivity : AppCompatActivity() {
    private lateinit var imageCar : ImageView
    private lateinit var textCarName: TextView
    private lateinit var textCarBrand: TextView
    private lateinit var textCarYear: TextView
    private lateinit var textCarDescription: TextView
    private lateinit var buttonWebsite: Button
    private lateinit var toolbar: Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_detail)

        toolbar= findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        imageCar = findViewById(R.id.imageCarDetail)
        textCarName = findViewById(R.id.textCarNameDetail)
        textCarBrand = findViewById(R.id.textCarBrandDetail)
        textCarYear = findViewById(R.id.textCarYearDetail)
        textCarDescription = findViewById(R.id.textCarDescriptionDetail)
        buttonWebsite = findViewById(R.id.buttonWebsite)

        val car = intent.getParcelableExtra<Car>("car")

        if(car != null) {
            imageCar.setImageResource(car.imageResId)
            textCarName.text = car.name
            textCarBrand.text = car.manufacturer
            textCarYear.text = car.year.toString()
            textCarDescription.text = car.description

            buttonWebsite.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, car.webLink.toUri()) //Uri.parse()
                startActivity(intent)
            }

            imageCar.setOnClickListener {
                val intent = Intent(this, CarImageActivity::class.java)
                intent.putExtra("car_image_res_id", car.imageResId)
                startActivity(intent)
            }

        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_share -> {
                showShareDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showShareDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.share))
        builder.setMessage(getString(R.string.confirm_share))

        builder.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
            val intent = Intent(this, ShareBroadcastReceiver::class.java)
            intent.action = "hr.tvz.android.listdetailapp.ACTION_SHARE"
            sendBroadcast(intent)
            dialog.dismiss()
        }

        builder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
            print("NO IS PRESSED")
            dialog.dismiss()
        }

        builder.create().show()
    }

}