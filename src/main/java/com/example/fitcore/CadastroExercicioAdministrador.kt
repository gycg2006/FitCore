// CadastroExercicioAdministrador.kt
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
import com.google.firebase.firestore.ktx.toObject

class CadastroExercicioAdministrador : AppCompatActivity() {

    private lateinit var editTextNomeExercicio: EditText
    private lateinit var editTextMidiaExercicio: EditText // Vai receber a URL da mídia
    private lateinit var imageButtonSubirMidia: ImageButton // Sem funcionalidade de upload nesta versão
    private lateinit var editTextDescricaoExercicio: EditText
    private lateinit var buttonRegistrarExercicio: Button
    private lateinit var buttonVoltar: Button

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_exercicio_administrador)

        editTextNomeExercicio = findViewById(R.id.editTextTextMaximo30)
        editTextMidiaExercicio = findViewById(R.id.editTextTextSubirMidia)
        imageButtonSubirMidia = findViewById(R.id.imageButtonSubirMidia) // Lembrete: Funcionalidade de upload não implementada aqui
        editTextDescricaoExercicio = findViewById(R.id.editTextTextMaximo150)
        buttonRegistrarExercicio = findViewById(R.id.buttonRegistrarExercicio)
        buttonVoltar = findViewById(R.id.buttonVoltar)

        buttonRegistrarExercicio.setOnClickListener {
            registrarNovoExercicio()
        }

        buttonVoltar.setOnClickListener {
            // Apenas volta para a CentralAdministrador, sem salvar
            val intent = Intent(this, CentralAdministrador::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        // TODO: Implementar a lógica do imageButtonSubirMidia para selecionar e fazer upload de mídia
        // imageButtonSubirMidia.setOnClickListener { abrirSeletorDeMidia() }
    }

    private fun registrarNovoExercicio() {
        val nome = editTextNomeExercicio.text.toString().trim()
        val midiaUrl = editTextMidiaExercicio.text.toString().trim() // Espera-se uma URL aqui
        val descricao = editTextDescricaoExercicio.text.toString().trim()

        if (nome.isEmpty() || descricao.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return
        }

        // Desabilitar botão para evitar cliques múltiplos
        buttonRegistrarExercicio.isEnabled = false

        // Cria um novo exercício com um ID gerado automaticamente
        val novoExercicio = Exercicio(
            nome = nome,
            urlFoto = midiaUrl,
            descricao = descricao
        )

        db.collection("Exercicios") // Usando "Exercicios" como nome da coleção
            .add(novoExercicio)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Exercício registrado com ID: ${documentReference.id}", Toast.LENGTH_LONG).show()
                // Limpar campos ou navegar para outra tela
                val intent = Intent(this, CentralAdministrador::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                finish() // Finaliza esta activity
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao registrar exercício: ${e.message}", Toast.LENGTH_LONG).show()
                buttonRegistrarExercicio.isEnabled = true // Reabilitar botão em caso de falha
            }
    }
}