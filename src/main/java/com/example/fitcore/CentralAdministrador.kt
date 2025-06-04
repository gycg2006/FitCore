package com.example.fitcore

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CentralAdministrador : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_central_administrador)

        val botaoGerenciarAlunos = findViewById<Button>(R.id.buttonGerenciamentoAlunos)
        botaoGerenciarAlunos.setOnClickListener {
            val intent = Intent(this, GerenciarAlunosAdministrador::class.java)
            startActivity(intent)
        }
        val botaoSinalizacaoDeMaquinasDefeituosas = findViewById<Button>(R.id.buttonSinalizacaoMaquinasDefeituosas)
        botaoSinalizacaoDeMaquinasDefeituosas.setOnClickListener {
            val intent = Intent(this, MaquinasDefeituosas::class.java)
            startActivity(intent)
        }
        val botaoAdicionarExercicios = findViewById<Button>(R.id.buttonAdicionarExercicios)
        botaoAdicionarExercicios.setOnClickListener {
            val intent = Intent(this, CadastroExercicioAdministrador::class.java)
            startActivity(intent)
        }
        val botaoAdicionarMaquinas = findViewById<Button>(R.id.buttonAdicionarMaquinas)
        botaoAdicionarMaquinas.setOnClickListener {
            val intent = Intent(this, CadastromaquinaAdiministrador::class.java)
            startActivity(intent)
        }
        val botaoDeslogar = findViewById<Button>(R.id.button5)
        botaoDeslogar.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }
}