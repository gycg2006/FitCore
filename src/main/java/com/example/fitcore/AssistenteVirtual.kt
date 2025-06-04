package com.example.fitcore

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch

class AssistenteVirtual : AppCompatActivity() {

    private lateinit var botaoVoltarIA: ImageButton
    private lateinit var botaoEnviar: ImageButton
    private lateinit var editTextMensagem: EditText
    private lateinit var recyclerViewMensagens: RecyclerView

    private val mensagens = mutableListOf<Mensagem>()
    private lateinit var adapter: MensagemAdapter

    private var mensagemDigitandoPosicao: Int? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assistente_virtual)

        botaoVoltarIA = findViewById(R.id.botaoVoltarIA)
        botaoEnviar = findViewById(R.id.botaoEnviar)
        editTextMensagem = findViewById(R.id.editTextMensagem)
        recyclerViewMensagens = findViewById(R.id.recyclerViewMensagens)

        adapter = MensagemAdapter(mensagens)
        recyclerViewMensagens.adapter = adapter
        recyclerViewMensagens.layoutManager = LinearLayoutManager(this)

        botaoVoltarIA.setOnClickListener {
            finish()
        }

        botaoEnviar.setOnClickListener {
            val texto = editTextMensagem.text.toString().trim()
            if (texto.isNotEmpty()) {
                enviarMensagemUsuario(texto)
            }
        }
    }

    private fun enviarMensagemUsuario(texto: String) {
        // Adiciona mensagem do usuário visível no chat
        mensagens.add(Mensagem(texto, ehUsuario = true))
        adapter.notifyItemInserted(mensagens.size - 1)
        recyclerViewMensagens.scrollToPosition(mensagens.size - 1)
        editTextMensagem.text.clear()

        // Envia prompt completo para IA (instrução fixa + texto do usuário)
        sendPromptComInstrucoes(texto)
    }

    private fun sendPromptComInstrucoes(textoUsuario: String) {
        // Aqui você monta o prompt com a instrução fixa + texto do usuário
        val promptCompleto = """
            Responda apenas questões relacionadas a academia de forma amigável,
            se não for relacionado a academia diga que não pode ajudar.

            Usuário: $textoUsuario
        """.trimIndent()

        // Adiciona mensagem "digitando" visível no chat
        mensagens.add(Mensagem("...", ehUsuario = false))
        mensagemDigitandoPosicao = mensagens.size - 1
        adapter.notifyItemInserted(mensagemDigitandoPosicao!!)
        recyclerViewMensagens.scrollToPosition(mensagemDigitandoPosicao!!)

        val generative = GenerativeModel(
            modelName = "gemini-2.0-flash",
            apiKey = "AIzaSyAn0HJly1WwbYZ9rSNQ3VqssiucEjfMBuM"
        )

        lifecycleScope.launch {
            val response = generative.generateContent(promptCompleto)
            response.text?.let { output ->
                runOnUiThread {
                    // Remove a mensagem "digitando"
                    mensagemDigitandoPosicao?.let {
                        mensagens.removeAt(it)
                        adapter.notifyItemRemoved(it)
                    }
                    mensagemDigitandoPosicao = null
                    // Adiciona a resposta real da IA
                    adicionarRespostaIA(output)
                }
            }
        }
    }

    private fun adicionarRespostaIA(texto: String) {
        mensagens.add(Mensagem(texto, ehUsuario = false))
        adapter.notifyItemInserted(mensagens.size - 1)
        recyclerViewMensagens.scrollToPosition(mensagens.size - 1)
    }
}
