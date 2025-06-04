package com.example.fitcore

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Nutricionista_chat : AppCompatActivity() {

    private lateinit var botaoVoltarNutriChat: ImageButton
    private lateinit var botaoEnviarNutri: ImageButton
    private lateinit var editTextMensagemNutri: EditText
    private lateinit var recyclerViewMensagensNutri: RecyclerView
    private lateinit var nomeNutricionista: TextView // Se quiser mudar dinamicamente

    private val mensagens = mutableListOf<Mensagem>()
    private lateinit var adapter: MensagemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nutricionista_chat)

        botaoVoltarNutriChat = findViewById(R.id.botaoVoltarNutriChat)
        botaoEnviarNutri = findViewById(R.id.botaoEnviarNutri)
        editTextMensagemNutri = findViewById(R.id.editTextMensagemNutri)
        recyclerViewMensagensNutri = findViewById(R.id.recyclerViewMensagensNutri)
        nomeNutricionista = findViewById(R.id.nomeNutricionista) // TextView do nome

        // Pode configurar o nome do nutricionista se vier de um Intent, etc.
        // nomeNutricionista.text = "Nome do Nutri via Intent"

        adapter = MensagemAdapter(mensagens) // Reutilizando o mesmo adapter
        recyclerViewMensagensNutri.adapter = adapter
        recyclerViewMensagensNutri.layoutManager = LinearLayoutManager(this)

        botaoVoltarNutriChat.setOnClickListener {
            val intent = Intent(this, CentralDeInformacoes::class.java)
            startActivity(intent)
            finish()
        }

        botaoEnviarNutri.setOnClickListener {
            val texto = editTextMensagemNutri.text.toString().trim()
            if (texto.isNotEmpty()) {
                enviarMensagemUsuario(texto)
                simularRespostaNutricionista(texto) // Nutricionista responde
            }
        }

        // Mensagem inicial do Nutricionista (opcional)
        adicionarMensagemNutricionista("Olá! Sou Arthur Morgan, seu nutricionista. Como posso te ajudar hoje?")
    }

    private fun enviarMensagemUsuario(texto: String) {
        mensagens.add(Mensagem(texto, ehUsuario = true))
        adapter.notifyItemInserted(mensagens.size - 1)
        recyclerViewMensagensNutri.scrollToPosition(mensagens.size - 1)
        editTextMensagemNutri.text.clear()
    }

    private fun simularRespostaNutricionista(textoUsuario: String) {
        val mensagemDigitando = "Arthur está digitando..."
        mensagens.add(Mensagem(mensagemDigitando, ehUsuario = false))
        val digitandoPosicao = mensagens.size - 1
        adapter.notifyItemInserted(digitandoPosicao)
        recyclerViewMensagensNutri.scrollToPosition(digitandoPosicao)

        lifecycleScope.launch {
            delay(1500) // Simula o tempo de digitação

            // Remove a mensagem "digitando"
            if (mensagens.isNotEmpty() && mensagens[digitandoPosicao].texto == mensagemDigitando) {
                mensagens.removeAt(digitandoPosicao)
                adapter.notifyItemRemoved(digitandoPosicao)
                // Se a mensagem "digitando" ainda existir e for a última, notifique o adapter
                if (digitandoPosicao > 0 && digitandoPosicao == mensagens.size) {
                    adapter.notifyItemChanged(digitandoPosicao -1) // Atualiza o item anterior se necessário
                } else if (digitandoPosicao == 0 && mensagens.isEmpty()){
                    // Não há mais itens
                } else if (digitandoPosicao < mensagens.size) {
                    adapter.notifyItemChanged(digitandoPosicao) // Atualiza o item na posição se outro foi adicionado
                }
            }


            // Adiciona a resposta real (simulada)
            val resposta = when {
                textoUsuario.contains("dieta", ignoreCase = true) -> "Claro, podemos ajustar sua dieta. Qual seu objetivo principal no momento (perder peso, ganhar massa, etc.)?"
                textoUsuario.contains("suplemento", ignoreCase = true) -> "Sobre suplementos, é importante avaliar caso a caso. Você está usando algum atualmente ou tem algum em mente?"
                textoUsuario.contains("horário", ignoreCase = true) -> "Os horários das refeições são flexíveis, mas precisamos garantir que você esteja nutrido ao longo do dia. Como está sua rotina?"
                else -> "Entendido! Sobre '$textoUsuario', me diga mais detalhes para que eu possa te ajudar melhor."
            }
            adicionarMensagemNutricionista(resposta)
        }
    }

    private fun adicionarMensagemNutricionista(texto: String) {
        mensagens.add(Mensagem(texto, ehUsuario = false)) // ehUsuario = false para o nutricionista
        adapter.notifyItemInserted(mensagens.size - 1)
        recyclerViewMensagensNutri.scrollToPosition(mensagens.size - 1)
    }
}