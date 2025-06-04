package com.example.fitcore

import android.content.Context // Importe Context
import android.content.Intent
import android.content.SharedPreferences // Importe SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText // Importe EditText
import android.widget.TextView // Importe TextView
import android.widget.Toast // Importe Toast
import androidx.appcompat.app.AppCompatActivity
// Remova imports não utilizados como ViewCompat e WindowInsetsCompat se não forem necessários
// import androidx.core.view.ViewCompat
// import androidx.core.view.WindowInsetsCompat
// A importação de R já está presente, o que é bom.

class inscricaoAulasColetivasCore : AppCompatActivity() {
    lateinit var buttonVoltarInscricoes: Button
    lateinit var buttonConfirmarInscricao: Button // Botão para confirmar
    lateinit var editTextNome: EditText // Campo para o nome
    lateinit var textNumeroVagas: TextView // TextView para mostrar as vagas

    private lateinit var prefs: SharedPreferences // Declaração do SharedPreferences
    private val aulaKey = AulasColetivas.AppConstants.KEY_VAGAS_CORE // Chave específica para esta aula

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inscricao_aulas_coletivas_core)

        buttonVoltarInscricoes = findViewById(R.id.voltarCore) // ID do XML
        // Inicialize os novos componentes da UI
        buttonConfirmarInscricao = findViewById(R.id.buttonConfirmarInscricaoCore) // ID do XML
        editTextNome = findViewById(R.id.editTextNome) // ID do XML
        textNumeroVagas = findViewById(R.id.textNumeroVagas) // ID do XML

        // Inicialize o SharedPreferences
        prefs = getSharedPreferences(AulasColetivas.AppConstants.PREFS_NAME, Context.MODE_PRIVATE)

        atualizarContagemVagas()

        buttonConfirmarInscricao.setOnClickListener {
            confirmarInscricao()
        }
    }

    override fun onStart() {
        super.onStart()
        // A navegação de volta já está correta
        buttonVoltarInscricoes.setOnClickListener {
            val intencao = Intent(this, AulasColetivas::class.java)
            startActivity(intencao)
            finish() // Adicione finish() para remover esta tela da pilha ao voltar
        }
        // É bom atualizar a contagem de vagas também no onStart caso o usuário navegue de volta
        // para esta tela sem recriá-la.
        atualizarContagemVagas()
    }

    private fun getVagasPreenchidas(): Int {
        return prefs.getInt(aulaKey, 0)
    }

    private fun setVagasPreenchidas(count: Int) {
        with(prefs.edit()) {
            putInt(aulaKey, count)
            apply()
        }
    }

    private fun atualizarContagemVagas() {
        val preenchidas = getVagasPreenchidas()
        textNumeroVagas.text = "$preenchidas/${AulasColetivas.AppConstants.TOTAL_VAGAS}" // Ex: "5/20"

        // Opcional: Desabilitar o botão de confirmar se as vagas estiverem esgotadas
        if (preenchidas >= AulasColetivas.AppConstants.TOTAL_VAGAS) {
            buttonConfirmarInscricao.isEnabled = false
            editTextNome.isEnabled = false // Também desabilita o campo de nome
            Toast.makeText(this, "Vagas esgotadas para esta aula!", Toast.LENGTH_SHORT).show()
        } else {
            buttonConfirmarInscricao.isEnabled = true
            editTextNome.isEnabled = true
        }
    }

    private fun confirmarInscricao() {
        val nome = editTextNome.text.toString().trim()
        if (nome.isEmpty()) {
            Toast.makeText(this, "Por favor, insira seu nome.", Toast.LENGTH_SHORT).show()
            return
        }

        val preenchidas = getVagasPreenchidas()
        if (preenchidas < AulasColetivas.AppConstants.TOTAL_VAGAS) {
            val novasPreenchidas = preenchidas + 1
            setVagasPreenchidas(novasPreenchidas)
            atualizarContagemVagas() // Atualiza a UI e verifica se as vagas esgotaram
            Toast.makeText(this, "Inscrição confirmada para $nome!", Toast.LENGTH_LONG).show()
            editTextNome.text.clear() // Limpa o campo após inscrição bem-sucedida
        } else {
            // Esta mensagem também é mostrada em atualizarContagemVagas se já estiver esgotado
            Toast.makeText(this, "Desculpe, vagas esgotadas para esta aula.", Toast.LENGTH_LONG).show()
        }
    }
}