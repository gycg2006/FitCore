package com.example.fitcore

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ModificandoFichaAdministrador : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modificando_ficha_do_aluno)
        val botaoVoltar = findViewById<Button>(R.id.button15)
        botaoVoltar.setOnClickListener {
            val intent = Intent(this, ModificandoTreinoAdministrador::class.java)
            startActivity(intent)
        }
    }
}