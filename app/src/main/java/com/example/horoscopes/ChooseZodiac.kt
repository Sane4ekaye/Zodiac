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
        var Mamasha_dasha = "Людмила"
        var mur = 11
        var mur2 = 124
        var mur4 = 18

        var kashtanchik: Boolean = false

        var slava_gavrilov = "MARTISHKAAKAKA ludmila"
        if(kashtanchik) println("Каштанчик ON!") else println("Каштанчик OFF!")
    }
}