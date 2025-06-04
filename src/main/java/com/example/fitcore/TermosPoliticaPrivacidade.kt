package com.example.fitcore

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class TermosPoliticaPrivacidade : AppCompatActivity() {

    lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_termos_de_uso)

        backButton = findViewById(R.id.backButtonNutri)

    }

    override fun onStart() {
        super.onStart()

        backButton.setOnClickListener {
            finish()
        }
    }
}