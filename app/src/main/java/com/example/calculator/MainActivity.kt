package com.example.calculator

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    fun writeSymbol(text: TextView, symbol: String) {
        val symbols = listOf(",", "/", "*", "-", "+")
        if (symbol in symbols) if (text.text.subSequence(text.text.length-1, text.text.length) in symbols) {
            text.text = text.text.subSequence(0, text.text.length-1).toString() + symbol
        } else {
            text.text = text.text.toString() + symbol
        }
    }

    @SuppressLint("SetTextI18n", "DiscouragedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val text = findViewById<TextView>(R.id.textView)
        for (i in 0..9) {
            val resID = resources.getIdentifier("button$i", "id", packageName)
            val butt = findViewById<Button>(resID)
            butt.setOnClickListener{text.text = text.text.toString() + i}
        }
        val buttonClear = findViewById<Button>(R.id.buttonClear)
        buttonClear.setOnClickListener{text.text = text.text.subSequence(0, text.text.length-1)}
        buttonClear.setOnLongClickListener{text.text = ""; true}
        findViewById<Button>(R.id.buttonDelimiter).setOnClickListener { writeSymbol(text, ",") }
        findViewById<Button>(R.id.buttonPlus).setOnClickListener { writeSymbol(text, "+") }
        findViewById<Button>(R.id.buttonDifference).setOnClickListener { writeSymbol(text, "-") }
        findViewById<Button>(R.id.buttonMultiply).setOnClickListener { writeSymbol(text, "*") }
        findViewById<Button>(R.id.buttonDivision).setOnClickListener { writeSymbol(text, "/") }

    }


}