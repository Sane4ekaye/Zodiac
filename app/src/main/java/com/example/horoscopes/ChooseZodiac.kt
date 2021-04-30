package com.example.horoscopes

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

class ChooseZodiac : AppCompatActivity() {

    var btnOven: ImageView? = null
    val APP_PREFERENCES: String = "horoscope"
    var Setting: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_zodiac)
        btnOven = findViewById(R.id.buttonOven)
        btnOven?.setOnClickListener{
            var intent = Intent(this@ChooseZodiac, MainActivity::class.java)
            startActivity(intent)
        }
    }
}