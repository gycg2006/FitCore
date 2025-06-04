package com.example.fitcore

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment

class PerfilFragment : Fragment(R.layout.fragment_perfil) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val botaoVoltar = view.findViewById<ImageButton>(R.id.botaoVoltarUsuario)
        botaoVoltar.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}