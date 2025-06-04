package com.example.fitcore

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CadastromaquinaAdiministrador : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastromaquina_adiministrador)

        val botaoRegistrarMaquina = findViewById<Button>(R.id.buttonRegistrarMaquina)
        botaoRegistrarMaquina.setOnClickListener {
            val intent = Intent(this, CentralAdministrador::class.java)
            startActivity(intent)
        }
        val botaoVoltarMaquina = findViewById<Button>(R.id.buttonVoltarMaquina)
        botaoVoltarMaquina.setOnClickListener {
            val intent = Intent(this, CentralAdministrador::class.java)
            startActivity(intent)
        }
    }
}