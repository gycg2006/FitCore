package com.example.fitcore

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MaquinasDefeituosas : AppCompatActivity() {

    lateinit var buttonVoltarMapa: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapeamento_academia_administrador)

        buttonVoltarMapa = findViewById(R.id.buttonVoltarAdm)
    }

    override fun onStart() {
        super.onStart()

        buttonVoltarMapa.setOnClickListener {
            finish()
        }
    }
}