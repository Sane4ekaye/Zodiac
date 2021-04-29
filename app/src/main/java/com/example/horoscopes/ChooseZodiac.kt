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
        var mur4 = 1

        var kashtanchik: Boolean = false

        var slava_gavrilov = "MARTISHKAAKAKA ludmila"
        var slava_gavrilov111 = "1 ludmila"
        var slava_gavrilov222 = "afds ludmila1"
        var slava_gav1 = "afds ludmila"
        if(kashtanchik) println("Каштанчик ON!") else println("Каштанчик OFF!")
    }
}