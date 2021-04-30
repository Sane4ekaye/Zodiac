package com.example.horoscopes

import android.content.Context
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
    val APP_PREFERENCES_SELECTED_HOROSCOPE: String = "selectedHoroscope"
    var isSelectedHoroscope: Boolean? = null
    var Setting: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_zodiac)
        Setting = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        btnOven = findViewById(R.id.buttonOven)

        if(Setting!!.contains(APP_PREFERENCES_SELECTED_HOROSCOPE)) {
            val intent = Intent(this@ChooseZodiac, MainActivity::class.java)
            startActivity(intent)
        } else {
            isSelectedHoroscope = false
        }

        btnOven?.setOnClickListener{
            if(isSelectedHoroscope == false){
                val intent = Intent(this@ChooseZodiac, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}