package com.example.fitcore

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.fitcore.R

class InscricaoAulasColetivasZumba : AppCompatActivity() {
    lateinit var buttonVoltarInscricoes: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inscricao_aulas_coletivas_zumba)
        buttonVoltarInscricoes = findViewById(R.id.voltarZumba)
    }
    override fun onStart() {
        super.onStart()
        buttonVoltarInscricoes.setOnClickListener{
            var intencao = Intent(this,AulasColetivas::class.java)
            startActivity(intencao)
        }

    }
}