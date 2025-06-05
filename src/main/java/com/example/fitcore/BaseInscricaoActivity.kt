package com.example.fitcore

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

abstract class BaseInscricaoActivity : AppCompatActivity() {

    // Propriedades abstratas a serem fornecidas pelas classes filhas
    @get:LayoutRes
    abstract val layoutRes: Int // ID do layout XML da activity filha
    abstract val aulaInscritosKey: String // Chave SharedPreferences para os inscritos desta aula
    abstract val idBotaoVoltar: Int // ID do botão "Voltar" no XML
    // Os IDs abaixo são consistentes nos seus XMLs, mas poderiam ser abstratos se variassem
    // abstract val idBotaoConfirmar: Int
    // abstract val idEditTextNome: Int
    // abstract val idTextNumeroVagas: Int

    lateinit var buttonVoltar: Button
    lateinit var buttonConfirmarInscricao: Button
    lateinit var editTextNome: EditText
    lateinit var textNumeroVagas: TextView

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes) // Define o layout da classe filha

        // Inicializa SharedPreferences
        prefs = getSharedPreferences(AulasColetivas.AppConstants.PREFS_NAME, Context.MODE_PRIVATE)

        // Inicializa componentes da UI (IDs são os mesmos nos seus XMLs, exceto o botão voltar)
        buttonVoltar = findViewById(idBotaoVoltar)
        buttonConfirmarInscricao = findViewById(R.id.buttonConfirmarInscricaoCore) // ID consistente nos XMLs
        editTextNome = findViewById(R.id.editTextNome) // ID consistente nos XMLs
        textNumeroVagas = findViewById(R.id.textNumeroVagas) // ID consistente nos XMLs

        atualizarContagemVagas()

        buttonConfirmarInscricao.setOnClickListener {
            confirmarInscricao()
        }

        buttonVoltar.setOnClickListener {
            // Volta para a tela AulasColetivas (ou a tela que lista as aulas)
            val intent = Intent(this, AulasColetivas::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        // Atualiza a contagem caso o usuário volte para esta tela
        atualizarContagemVagas()
    }

    private fun getInscritosNestaAula(): Set<String> {
        return prefs.getStringSet(aulaInscritosKey, emptySet()) ?: emptySet()
    }

    private fun setInscritosNestaAula(inscritos: Set<String>) {
        with(prefs.edit()) {
            putStringSet(aulaInscritosKey, inscritos)
            apply()
        }
    }

    private fun atualizarContagemVagas() {
        val inscritos = getInscritosNestaAula()
        val preenchidas = inscritos.size
        textNumeroVagas.text = "$preenchidas/${AulasColetivas.AppConstants.TOTAL_VAGAS}"

        if (preenchidas >= AulasColetivas.AppConstants.TOTAL_VAGAS) {
            buttonConfirmarInscricao.isEnabled = false
            editTextNome.isEnabled = false // Também desabilita o campo de nome
            // Mostra o Toast apenas se não estiver já desabilitado por outro motivo
            if(editTextNome.text.toString().trim().isNotEmpty() || buttonConfirmarInscricao.isEnabled) {
                Toast.makeText(this, "Vagas esgotadas para esta aula!", Toast.LENGTH_SHORT).show()
            }
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

        val inscritosAtuais = getInscritosNestaAula().toMutableSet() // Obter uma cópia mutável

        if (inscritosAtuais.contains(nome)) {
            Toast.makeText(this, "$nome, você já está inscrito(a) nesta aula!", Toast.LENGTH_LONG).show()
            return // Usuário já inscrito
        }

        if (inscritosAtuais.size < AulasColetivas.AppConstants.TOTAL_VAGAS) {
            inscritosAtuais.add(nome) // Adiciona o novo nome ao conjunto
            setInscritosNestaAula(inscritosAtuais) // Salva o conjunto atualizado
            atualizarContagemVagas() // Atualiza a UI e verifica se as vagas esgotaram
            Toast.makeText(this, "Inscrição confirmada para $nome!", Toast.LENGTH_LONG).show()
            editTextNome.text.clear() // Limpa o campo após inscrição bem-sucedida
        } else {
            // Esta mensagem também é mostrada em atualizarContagemVagas se já estiver esgotado
            Toast.makeText(this, "Desculpe, vagas esgotadas para esta aula.", Toast.LENGTH_LONG).show()
        }
    }
}