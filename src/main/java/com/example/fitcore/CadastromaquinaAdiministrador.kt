// CadastromaquinaAdiministrador.kt
package com.example.fitcore

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CadastromaquinaAdiministrador : AppCompatActivity() {

    private lateinit var editTextNomeMaquina: EditText
    private lateinit var editTextMidiaMaquina: EditText // Vai receber a URL da mídia
    private lateinit var imageButtonSubirMidiaMaquina: ImageButton // Sem funcionalidade de upload nesta versão
    private lateinit var editTextLocalizadorMaquina: EditText
    private lateinit var buttonRegistrarMaquina: Button
    private lateinit var buttonVoltarMaquina: Button

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastromaquina_adiministrador)

        editTextNomeMaquina = findViewById(R.id.editTextTextMaximo30Carac)
        editTextMidiaMaquina = findViewById(R.id.editTextTextSubirMidiaMaquina)
        imageButtonSubirMidiaMaquina = findViewById(R.id.imageButtonSubirMidiaMaquina) // Lembrete: Funcionalidade de upload não implementada aqui
        editTextLocalizadorMaquina = findViewById(R.id.editTextTextMaximo10Carac) // ID do XML
        buttonRegistrarMaquina = findViewById(R.id.buttonRegistrarMaquina)
        buttonVoltarMaquina = findViewById(R.id.buttonVoltarMaquina)

        buttonRegistrarMaquina.setOnClickListener {
            registrarNovaMaquina()
        }

        buttonVoltarMaquina.setOnClickListener {
            val intent = Intent(this, CentralAdministrador::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        // TODO: Implementar a lógica do imageButtonSubirMidiaMaquina para selecionar e fazer upload de mídia
        // imageButtonSubirMidiaMaquina.setOnClickListener { abrirSeletorDeMidia() }
    }

    private fun registrarNovaMaquina() {
        val nome = editTextNomeMaquina.text.toString().trim()
        val midiaUrl = editTextMidiaMaquina.text.toString().trim() // Espera-se uma URL aqui
        val localizador = editTextLocalizadorMaquina.text.toString().trim()

        if (nome.isEmpty() || localizador.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return
        }

        // Desabilitar botão para evitar cliques múltiplos
        buttonRegistrarMaquina.isEnabled = false

        val novasInformacoes = hashMapOf(
            "nome" to nome,
            "urlFoto" to midiaUrl,
            "observacoes" to localizador,
            "repeticoes" to 12,
            "series" to 4
        )

        db.collection("Maquina")
            .add(novasInformacoes)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Exercício registrado com ID: ${documentReference.id}", Toast.LENGTH_LONG).show()
                // Limpar campos ou navegar para outra tela
                val intent = Intent(this, CentralAdministrador::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                finish() // Finaliza esta activity
            }
    }
}