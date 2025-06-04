package com.example.fitcore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var buttonAcessarAulas: Button
    private lateinit var buttonAcessarAssistente: Button
    private lateinit var buttonAcessarNutri: Button

    private lateinit var db: FirebaseFirestore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonAcessarAulas = view.findViewById(R.id.btnAulasI)
        buttonAcessarAssistente = view.findViewById(R.id.btnAssistenteI)
        buttonAcessarNutri = view.findViewById(R.id.btnNutriI)

        db = Firebase.firestore

        buttonAcessarAulas.setOnClickListener {
            val intent = Intent(requireContext(), AulasColetivas::class.java)
            startActivity(intent)
        }

        buttonAcessarAssistente.setOnClickListener {
            val intent = Intent(requireContext(), AssistenteVirtual::class.java)
            startActivity(intent)
        }

        buttonAcessarNutri.setOnClickListener {
            db.collection("Usuario")
                .whereEqualTo("email", Login.LoginCredentials.emailLogado)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        Toast.makeText(baseContext, "E-mail ou senha inválidos.", Toast.LENGTH_LONG)
                            .show()
                        Log.d(
                            "LoginFirestore",
                            "Nenhum documento encontrado para o email: ${Login.LoginCredentials.emailLogado}"
                        )
                    } else {
                        val userDocument = documents.documents[0]
                        val contratoBool = userDocument.getString("contrato")
                        if (contratoBool.equalsTo("true")) {
                            val intent = Intent(requireContext(), Nutricionista_chat::class.java)
                            startActivity(intent)
                        } else {
                            val intent = Intent(requireContext(), ContratoNutricionista::class.java)
                            startActivity(intent)
                        }
                    }
                }
        }
    }
}