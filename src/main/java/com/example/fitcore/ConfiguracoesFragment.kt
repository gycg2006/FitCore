package com.example.fitcore

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment

class ConfiguracoesFragment : Fragment(R.layout.fragment_configuracoes) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val botaoVoltarConfig = view.findViewById<ImageButton>(R.id.botaoVoltarConfigC)
        val botaoRedefSenha = view.findViewById<Button>(R.id.botaoRedefSenhaC)
        val botaoDeslogar = view.findViewById<Button>(R.id.botaoDeslogarC)
        val botaoTermos = view.findViewById<Button>(R.id.botaoTermosC)

        botaoVoltarConfig.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        botaoRedefSenha.setOnClickListener {
            val intent = Intent(requireContext(), RedefinicaoDeSenha::class.java)
            startActivity(intent)
        }

        botaoDeslogar.setOnClickListener {
            val intent = Intent(requireContext(), Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        botaoTermos.setOnClickListener {
            val intent = Intent(requireContext(), TermosPoliticaPrivacidade::class.java)
            startActivity(intent)
        }
    }
}
