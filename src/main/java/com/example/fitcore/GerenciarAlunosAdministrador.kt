package com.example.fitcore

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class GerenciarAlunosAdministrador : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gerenciar_alunos_administrador)

        val botaoFichaJulia = findViewById<Button>(R.id.buttonEditarFichaJulia)
        botaoFichaJulia.setOnClickListener {
            val intent = Intent(this, ModificandoTreinoAdministrador::class.java)
            startActivity(intent)
        }
        val botaoFichaPedro = findViewById<Button>(R.id.buttonEditarFichaPedro)
        botaoFichaPedro.setOnClickListener {
            val intent = Intent(this, ModificandoTreinoAdministrador::class.java)
            startActivity(intent)
        }
        val botaoFichaAirton = findViewById<Button>(R.id.buttonEditarFichaAirton)
        botaoFichaAirton.setOnClickListener {
            val intent = Intent(this, ModificandoTreinoAdministrador::class.java)
            startActivity(intent)
        }
        val botaoFichaCarla = findViewById<Button>(R.id.buttonEditarFichaCarla)
        botaoFichaCarla.setOnClickListener {
            val intent = Intent(this, ModificandoTreinoAdministrador::class.java)
            startActivity(intent)
        }
        val botaoVoltar = findViewById<Button>(R.id.buttonVoltarListaAlunos)
        botaoVoltar.setOnClickListener {
            val intent = Intent(this, CentralAdministrador::class.java)
            startActivity(intent)
        }
    }
}