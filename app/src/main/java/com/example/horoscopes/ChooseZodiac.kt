package com.example.horoscopes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class ChooseZodiac : AppCompatActivity() {

    var buttonTwins: Button? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_zodiac)
        buttonTwins = findViewById(R.id.buttonTwins) as Button // Хули блять тут ошибка
        buttonTwins.setOnClickListener {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
        }

    }
}