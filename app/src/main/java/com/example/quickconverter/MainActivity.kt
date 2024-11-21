package com.example.quickconverter

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import android.widget.Spinner
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var conversionTypeSpinner: Spinner
    private lateinit var fromUnitSpinner: Spinner
    private lateinit var toUnitSpinner: Spinner
    private lateinit var valueInput: EditText
    private lateinit var convertButton: Button
    private lateinit var resultText: TextView

    private var conversionType = "Length"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        conversionTypeSpinner = findViewById(R.id.conversionTypeSpinner)
        fromUnitSpinner = findViewById(R.id.fromUnitSpinner)
        toUnitSpinner = findViewById(R.id.toUnitSpinner)
        valueInput = findViewById(R.id.valueInput)
        convertButton = findViewById(R.id.convertButton)
        resultText = findViewById(R.id.resultText)

        val conversionTypes = listOf("Length", "Weight", "Temperature")
        val conversionTypeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, conversionTypes)
        conversionTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        conversionTypeSpinner.adapter = conversionTypeAdapter

        // Set listener for conversion type change
        conversionTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                conversionType = when (p2) {
                    0 -> "Length"
                    1 -> "Weight"
                    2 -> "Temperature"
                    else -> "Length"
                }
                updateUnitSpinners()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }

        // Initialize the spinners when the activity is created
        updateUnitSpinners()

        convertButton.setOnClickListener {
            val value = valueInput.text.toString().toDoubleOrNull()
            if (value != null) {
                val result = when (conversionType) {
                    "Length" -> convertLength(value)
                    "Weight" -> convertWeight(value)
                    "Temperature" -> convertTemperature(value)
                    else -> value
                }
                resultText.text = "Result: $result"
            } else {
                resultText.text = "Please enter a valid number."
            }
        }
    }

    private fun updateUnitSpinners() {
        val units = when (conversionType) {
            "Length" -> listOf("Meters", "Kilometers", "Miles", "Feet")
            "Weight" -> listOf("Kilograms", "Pounds")
            "Temperature" -> listOf("Celsius", "Fahrenheit")
            else -> listOf()
        }

        val unitAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, units)
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fromUnitSpinner.adapter = unitAdapter
        toUnitSpinner.adapter = unitAdapter
    }

    private fun convertLength(value: Double): Double {
        val fromUnit = fromUnitSpinner.selectedItem.toString()
        val toUnit = toUnitSpinner.selectedItem.toString()

        return when {
            fromUnit == "Meters" && toUnit == "Kilometers" -> value / 1000
            fromUnit == "Kilometers" && toUnit == "Meters" -> value * 1000
            fromUnit == "Miles" && toUnit == "Feet" -> value * 5280
            fromUnit == "Feet" && toUnit == "Miles" -> value / 5280
            else -> value // No conversion
        }
    }

    private fun convertWeight(value: Double): Double {
        val fromUnit = fromUnitSpinner.selectedItem.toString()
        val toUnit = toUnitSpinner.selectedItem.toString()

        return when {
            fromUnit == "Kilograms" && toUnit == "Pounds" -> value * 2.20462
            fromUnit == "Pounds" && toUnit == "Kilograms" -> value / 2.20462
            else -> value // No conversion
        }
    }

    private fun convertTemperature(value: Double): Double {
        val fromUnit = fromUnitSpinner.selectedItem.toString()
        val toUnit = toUnitSpinner.selectedItem.toString()

        return when {
            fromUnit == "Celsius" && toUnit == "Fahrenheit" -> value * 9 / 5 + 32
            fromUnit == "Fahrenheit" && toUnit == "Celsius" -> (value - 32) * 5 / 9
            else -> value // No conversion
        }
    }
}
