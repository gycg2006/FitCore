package com.example.fitcore
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class ContratoNutricionista : AppCompatActivity() {
    lateinit var Assinar: Button
    lateinit var ConfirmarPolitica: CheckBox
    lateinit var LinkTermos: TextView
    lateinit var BotaoVoltarContrato: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contrato_nutricionista)

        ConfirmarPolitica = findViewById(R.id.ConfirmacaoPoliticas)
        Assinar = findViewById(R.id.ContratoAssinar)
        LinkTermos = findViewById(R.id.LinkTermos)
        BotaoVoltarContrato = findViewById(R.id.botaoVoltarContrato)


    }

    override fun onStart() {
        super.onStart()
        LinkTermos.setOnClickListener {
            val intent = Intent(this, TermosPoliticaPrivacidade::class.java)
            startActivity(intent)
        }
        BotaoVoltarContrato.setOnClickListener {
            finish()
        }
        Assinar.setOnClickListener {
            if(ConfirmarPolitica.isChecked){
                var intencao = Intent(this, Nutricionista_chat::class.java)
                startActivity(intencao)
                finish()
            }
            else {
                Toast.makeText(this, "Você precisa aceitar as políticas primeiro!", Toast.LENGTH_SHORT).show()
            }
        }

    }
}