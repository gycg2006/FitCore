package com.example.fitcore

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

class ListaTreinosFragment : Fragment(R.layout.fragment_lista_treinos) {

    // Declaração dos botões
    private lateinit var iniciarButton1: Button
    private lateinit var iniciarButton2: Button
    private lateinit var iniciarButton3: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Vincula os botões do layout aos objetos Kotlin
        iniciarButton1 = view.findViewById(R.id.iniciarButtonSup1)
        iniciarButton2 = view.findViewById(R.id.iniciarButtonInf1)
        iniciarButton3 = view.findViewById(R.id.iniciarButtonInf2)

        // Configuração dos listeners para os botões
        configurarListeners()
    }

    private fun configurarListeners() {
        // Quando o botão for clicado, chama a função para iniciar a ficha de treino
        iniciarButton1.setOnClickListener {
            iniciarFichaDeTreino()
        }

        iniciarButton2.setOnClickListener {
            iniciarFichaDeTreino()
        }

        iniciarButton3.setOnClickListener {
            iniciarFichaDeTreino()
        }
    }

    // Função para iniciar a FichaDeTreinoActivity
    private fun iniciarFichaDeTreino() {
        // Intent para iniciar a Activity de FichaDeTreino
        val intent = Intent(requireContext(), FichaDeTreino::class.java)
        startActivity(intent)
    }
}
