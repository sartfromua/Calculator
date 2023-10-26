package com.example.calculator

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.util.Stack

class MainActivity : AppCompatActivity() {

    private fun calculate(example: String): String {
        val elements = Regex("[0-9.]+|[^0-9.]+").findAll(example).map { it.value }.toList().toMutableList()
        if (listOf(".", "/", "*", "-", "+").contains(elements[elements.size-1])) {
            elements.removeAt(elements.size-1)
        }
        if (elements[0] == "-") {
            elements.removeAt(0)
            elements[0] = "-" + elements[0]
        }
        while (elements.size > 1) {
            if (elements.contains("*") || elements.contains("/")) {
                val sign = elements.indexOfFirst { it == "*" || it == "/" }
                var number: Double = 0.0
                number = if (elements[sign] == "*") {
                    val res = elements[sign - 1].toDouble() * elements[sign+1].toDouble()
                    Log.d("XXXXX", "${elements[sign - 1]} * ${elements[sign+1]} = $res")
                    res
                } else {
                    if (elements[sign+1].toDouble() == 0.0) {
//                        Log.d("XXXXX", "Division by Zero!")
                        throw Exception("Division by Zero!")
                    }
                    val res = elements[sign - 1].toDouble() / elements[sign+1].toDouble()
                    Log.d("XXXXX", "${elements[sign - 1]} / ${elements[sign+1]} = $res")
                    res
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
                    val res = elements[sign - 1].toDouble() + elements[sign+1].toDouble()
                    Log.d("XXXXX", "${elements[sign - 1]} + ${elements[sign+1]} = $res")
                    res
                } else {
                    val res = elements[sign - 1].toDouble() - elements[sign+1].toDouble()
                    Log.d("XXXXX", "${elements[sign - 1]} - ${elements[sign+1]} = $res")
                    res
                }
                elements[sign] = number.toString()
                elements.removeAt(sign+1)
                elements.removeAt(sign-1)
                continue
            }
        }
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

    @SuppressLint("SetTextI18n", "DiscouragedApi", "MissingInflatedId", "ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val text = findViewById<TextView>(R.id.textViewInput)
        val prevText = findViewById<TextView>(R.id.textViewExample)

        for (i in 0..9) {
            val resID = resources.getIdentifier("button$i", "id", packageName)
            val butt = findViewById<Button>(resID)
            butt.setOnClickListener{writeSymbol(text, i.toString())}
        }

        val butts = mapOf<String, String>("Delimiter" to ".", "Plus" to "+", "Difference" to "-", "Multiply" to "*", "Division" to "/")
        for (buttName in butts.keys) {
            val resID = resources.getIdentifier("button$buttName", "id", packageName)
            val butt = findViewById<Button>(resID)
            butt.setOnClickListener{writeSymbol(text, butts[buttName].toString())}
        }

        val buttonClear = findViewById<Button>(R.id.buttonClear)
        buttonClear.setOnClickListener{
            if (text.text.isNotEmpty()) text.text = text.text.subSequence(0, text.text.length-1)
        }
        buttonClear.setOnLongClickListener{text.text = ""; true}

        findViewById<Button>(R.id.buttonEqual).setOnClickListener{
            try {
                val number = calculate(text.text.toString())
            prevText.text = text.text.toString() + "="
            text.text = number
            } catch (e: Exception) {
                Log.e("XXXXX", e.message.toString())
                prevText.text = text.text.toString() + "=ERROR"
                text.text = ""
            }
        }

        val calcMemory = Stack<Double>()
        findViewById<Button>(R.id.buttonMPlus).setOnClickListener{
            if (text.text.toString().isNotEmpty()) calcMemory.add(calculate(text.text.toString()).toDouble())
            val toast : Toast = Toast.makeText(applicationContext, "M+ has been pressed!", Toast.LENGTH_SHORT)
            toast.show()
        }
        findViewById<Button>(R.id.buttonMR).setOnClickListener{
            if (!calcMemory.empty()) text.text = text.text.toString() + calcMemory.pop().toString()
            val toast : Toast = Toast.makeText(applicationContext, "MR has been pressed!", Toast.LENGTH_SHORT)
            toast.show()
        }
        findViewById<Button>(R.id.buttonMC).setOnClickListener{
            calcMemory.clear()
            val toast : Toast = Toast.makeText(applicationContext, "MC has been pressed!", Toast.LENGTH_SHORT)
            toast.show()
        }


    }


}