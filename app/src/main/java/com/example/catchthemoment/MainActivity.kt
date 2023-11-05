package com.example.catchthemoment

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val readyButton = findViewById<Button>(R.id.readyButton)
        readyButton.setOnClickListener {
            // Действия при нажатии на кнопку "Готово"
            val text = findViewById<EditText>(R.id.textEditText).text.toString()
            // Здесь можно выполнить дополнительные действия с введенным текстом
            Toast.makeText(this, "Текст: $text", Toast.LENGTH_SHORT).show()
        }
    }
}

