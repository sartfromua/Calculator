package com.example.calculator

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    fun calculate(example: String): String {
        val elements = Regex("[0-9.]+|[^0-9.]+").findAll(example).map { it.value }.toList().toMutableList()
        while (elements.size > 1) {
            Log.d("XXXXX", elements.toString())
            if (elements.contains("*") || elements.contains("/")) {
                val sign = elements.indexOfFirst { it == "*" || it == "/" }
                Log.d("XXXXX", "$sign: ${elements[sign]}")
                var number: Double = 0.0
                number = if (elements[sign] == "*") {
                    elements[sign - 1].toDouble() * elements[sign+1].toDouble()
                } else {
                    elements[sign - 1].toDouble() / elements[sign+1].toDouble()
                }
                elements[sign] = number.toString()
                elements.removeAt(sign+1)
                elements.removeAt(sign-1)
                Log.d("XXXXX", elements.toString())
                continue
            }
            if (elements.contains("+") || elements.contains("-")) {
                val sign = elements.indexOfFirst { it == "+" || it == "-" }
                Log.d("XXXXX", "$sign: ${elements[sign]}")
                var number: Double = 0.0
                number = if (elements[sign] == "+") {
                    elements[sign - 1].toDouble() + elements[sign+1].toDouble()
                } else {
                    elements[sign - 1].toDouble() - elements[sign+1].toDouble()
                }
                elements[sign] = number.toString()
                elements.removeAt(sign+1)
                elements.removeAt(sign-1)
                continue
            }
            Log.d("XXXXX", elements.toString())
        }
        Log.d("XXXXX", "Result: ${elements.toString()}")
        var result = elements[0]
        if (result.length - result.indexOf(".") > 5)
            result = String.format("%.5f", result.toDouble())
        result = result.replace(",", ".")
        return result
    }

    @SuppressLint("SetTextI18n")
    fun writeSymbol(text: TextView, symbol: String) {
        val symbols = listOf(".", "/", "*", "-", "+")
        if (symbol in symbols && text.text.subSequence(text.text.length-1, text.text.length) in symbols) {
                text.text = text.text.subSequence(0, text.text.length-1).toString() + symbol
            } else {
            text.text = text.text.toString() + symbol
        }
    }

    @SuppressLint("SetTextI18n", "DiscouragedApi", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val text = findViewById<TextView>(R.id.textViewInput)
        val prevText = findViewById<TextView>(R.id.textViewExample)
        val butts = mapOf<String, String>("Delimiter" to ".", "Plus" to "+", "Difference" to "-", "Multiply" to "*", "Division" to "/")
        for (i in 0..9) {
            val resID = resources.getIdentifier("button$i", "id", packageName)
            val butt = findViewById<Button>(resID)
            butt.setOnClickListener{writeSymbol(text, i.toString())}
        }
        for (buttName in butts.keys) {
            val resID = resources.getIdentifier("button$buttName", "id", packageName)
            val butt = findViewById<Button>(resID)
            butt.setOnClickListener{writeSymbol(text, butts[buttName].toString())}
        }
        val buttonClear = findViewById<Button>(R.id.buttonClear)
        buttonClear.setOnClickListener{text.text = text.text.subSequence(0, text.text.length-1)}
        buttonClear.setOnLongClickListener{text.text = ""; true}

//        findViewById<Button>(R.id.buttonDelimiter).setOnClickListener { writeSymbol(text, ".") }
//        findViewById<Button>(R.id.buttonPlus).setOnClickListener { writeSymbol(text, "+") }
//        findViewById<Button>(R.id.buttonDifference).setOnClickListener { writeSymbol(text, "-") }
//        findViewById<Button>(R.id.buttonMultiply).setOnClickListener { writeSymbol(text, "*") }
//        findViewById<Button>(R.id.buttonDivision).setOnClickListener { writeSymbol(text, "/") }

        findViewById<Button>(R.id.buttonEqual).setOnClickListener {
            val number = calculate(text.text.toString())
            prevText.text = text.text.toString() + "="
            text.text = number
        }


    }


}