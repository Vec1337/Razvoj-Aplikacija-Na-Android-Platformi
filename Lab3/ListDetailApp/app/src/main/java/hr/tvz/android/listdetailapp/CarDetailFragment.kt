import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import hr.tvz.android.listdetailapp.Car
import hr.tvz.android.listdetailapp.CarImageActivity
import hr.tvz.android.listdetailapp.R

class CarDetailFragment : Fragment() {

    private var selectedCar: Car? = null

    fun setCar(car: Car) {
        selectedCar = car
        updateUI()
    }

    private lateinit var imageCar: ImageView
    private lateinit var textCarName: TextView
    private lateinit var textCarBrand: TextView
    private lateinit var textCarYear: TextView
    private lateinit var textCarDescription: TextView
    private lateinit var buttonWebsite: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_car_detail, container, false)

        imageCar = view.findViewById(R.id.imageCarDetail)
        textCarName = view.findViewById(R.id.textCarNameDetail)
        textCarBrand = view.findViewById(R.id.textCarBrandDetail)
        textCarYear = view.findViewById(R.id.textCarYearDetail)
        textCarDescription = view.findViewById(R.id.textCarDescriptionDetail)
        buttonWebsite = view.findViewById(R.id.buttonWebsite)

        updateUI()

        buttonWebsite.setOnClickListener {
            selectedCar?.webLink?.let { link ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(intent)
            }
        }

        imageCar.setOnClickListener {
            selectedCar?.let { car ->
                val intent = Intent(requireContext(), CarImageActivity::class.java)
                intent.putExtra("car_image_res_id", car.imageResId)
                startActivity(intent)
            }
        }

        return view
    }

    private fun updateUI() {
        if (this::imageCar.isInitialized && selectedCar != null) {
            imageCar.setImageResource(selectedCar!!.imageResId)
            textCarName.text = selectedCar!!.name
            textCarBrand.text = selectedCar!!.manufacturer
            textCarYear.text = selectedCar!!.year.toString()
            textCarDescription.text = selectedCar!!.description
        }
    }
}
