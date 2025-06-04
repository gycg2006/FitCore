package com.example.fitcore

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class FichaDeTreino : AppCompatActivity() {

    lateinit var botaoVoltar: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ficha_de_treino)


        botaoVoltar = findViewById(R.id.buttonVoltarFicha)
    }

    override fun onStart() {
        super.onStart()

        botaoVoltar.setOnClickListener {
            finish()
        }
    }
}