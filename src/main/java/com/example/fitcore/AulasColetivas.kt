package com.example.fitcore

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AulasColetivas : AppCompatActivity() {
    lateinit var buttonVoltarHome: Button
    lateinit var buttonZumba: Button
    lateinit var buttonCore: Button
    lateinit var buttonResistencia: Button
    lateinit var buttonFitburn: Button



    object AppConstants {
        const val PREFS_NAME = "FitCoreAulasPrefs" // Nome do arquivo SharedPreferences
        const val TOTAL_VAGAS = 20 // Total de vagas por aula

        const val KEY_INSCRITOS_CORE = "inscritos_aula_core"
        const val KEY_INSCRITOS_FITBURN = "inscritos_aula_fitburn"
        const val KEY_INSCRITOS_RESISTENCIA = "inscritos_aula_resistencia"
        const val KEY_INSCRITOS_ZUMBA = "inscritos_aula_zumba"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aulas_coletivas)
        buttonVoltarHome = findViewById(R.id.buttonVoltarHome)
        buttonZumba = findViewById(R.id.buttonZumba)
        buttonCore = findViewById(R.id.buttonCore)
        buttonResistencia = findViewById(R.id.buttonResistencia)
        buttonFitburn = findViewById(R.id.buttonFitBurn)

    }
    override fun onStart() {
        super.onStart()
        buttonVoltarHome.setOnClickListener{
            var intencao = Intent(this,CentralDeInformacoes::class.java)
            startActivity(intencao)
        }
        buttonZumba.setOnClickListener{
            var intencao = Intent(this,InscricaoAulasColetivasZumba::class.java)
            startActivity(intencao)
        }
        buttonCore.setOnClickListener{
            var intencao = Intent(this,inscricaoAulasColetivasCore::class.java)
            startActivity(intencao)
        }
        buttonResistencia.setOnClickListener{
            var intencao = Intent(this,inscricaoAulasColetivasResistencia::class.java)
            startActivity(intencao)
        }
        buttonFitburn.setOnClickListener{
            var intencao = Intent(this,inscricaoAulasColetivasFitburn::class.java)
            startActivity(intencao)
        }


    }

}