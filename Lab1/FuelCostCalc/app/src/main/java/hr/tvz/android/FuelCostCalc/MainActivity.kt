package hr.tvz.android.FuelCostCalc

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import hr.tvz.android.FuelCostCalc.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        enableEdgeToEdge()


        binding.buttonCalculate.setOnClickListener {
            calculateFuelCost()
        }

        binding.seekBarFontSize.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val fontSize = progress.toFloat()
                binding.textViewResult.textSize = fontSize
                binding.buttonCalculate.textSize = fontSize
                binding.editTextConsumption.textSize = fontSize
                binding.editTextDistance.textSize = fontSize
                binding.editTextPrice.textSize = fontSize

                binding.textViewFontSize.textSize = fontSize
                binding.textViewBackground.textSize = fontSize
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        val colorThemes = resources.getStringArray(R.array.colorThemes)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, colorThemes)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerChangeTheme.adapter = spinnerAdapter


        binding.spinnerChangeTheme.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(position) {
                    0 -> binding.root.setBackgroundColor(resources.getColor(R.color.white))
                    1 -> binding.root.setBackgroundColor(resources.getColor(R.color.black))
                    2 -> binding.root.setBackgroundColor(resources.getColor(R.color.red))
                    3 -> binding.root.setBackgroundColor(resources.getColor(R.color.blue))
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

    }


    fun calculateFuelCost() {
        val distance = binding.editTextDistance.text.toString().toDoubleOrNull()
        val consumption = binding.editTextConsumption.text.toString().toDoubleOrNull()
        val price = binding.editTextPrice.text.toString().toDoubleOrNull()

        if(distance == null || consumption == null || price == null) {
            Toast.makeText(this, getString(R.string.invalid_input), Toast.LENGTH_SHORT).show()
            return
        }

        val fuelUsed = (distance / 100) * consumption
        val result = fuelUsed * price

        val cost = String.format("%.2f", result)
        binding.textViewResult.text = getString(R.string.total_fuel_cost, cost)
    }
}