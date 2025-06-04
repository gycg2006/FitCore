package com.example.fitcore

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CadastroExercicioAdministrador : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_exercicio_administrador)

        val botaoRegistrarExercicio = findViewById<Button>(R.id.buttonRegistrarExercicio)
        botaoRegistrarExercicio.setOnClickListener {
            val intent = Intent(this, CentralAdministrador::class.java)
            startActivity(intent)
        }
        val botaoVoltar = findViewById<Button>(R.id.buttonVoltar)
        botaoVoltar.setOnClickListener {
            val intent = Intent(this, CentralAdministrador::class.java)
            startActivity(intent)
        }
    }
}