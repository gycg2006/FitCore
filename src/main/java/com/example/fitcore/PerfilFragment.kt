package com.example.fitcore

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment

class PerfilFragment : Fragment(R.layout.fragment_perfil) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val botaoVoltar = view.findViewById<ImageButton>(R.id.botaoVoltarUsuario)
        botaoVoltar.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        var nomeTexto = view.findViewById<TextView>(R.id.textView7)
        var email = view.findViewById<TextView>(R.id.textView8)
        var cpf = view.findViewById<TextView>(R.id.textView9)

        nomeTexto.text = "Olá, " + Login.LoginCredentials.nome + "!"
        email.text = Login.LoginCredentials.emailLogado
        cpf.text = Login.LoginCredentials.cpf

    }
}