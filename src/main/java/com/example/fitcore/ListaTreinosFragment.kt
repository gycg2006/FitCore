package com.example.fitcore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ListaTreinosFragment : Fragment(R.layout.fragment_lista_treinos) {

    private lateinit var recyclerViewModelos: RecyclerView
    private lateinit var adapterModelos: ModeloTreinoSelecaoAdapter
    private val listaDeModelos = mutableListOf<ModeloTreino>()
    private val db = Firebase.firestore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewModelos = view.findViewById(R.id.recyclerViewModelosTreino)
        setupRecyclerView()
        carregarModelosDeTreino()
    }

    private fun setupRecyclerView() {
        adapterModelos = ModeloTreinoSelecaoAdapter(listaDeModelos) { modeloSelecionado ->
            // Iniciar a FichaDeTreino Activity, passando o ID e o nome do modelo
            val intent = Intent(requireContext(), FichaDeTreino::class.java).apply {
                putExtra("MODELO_TREINO_ID", modeloSelecionado.id)
                putExtra("MODELO_TREINO_NOME", modeloSelecionado.nome)
            }
            startActivity(intent)
        }
        recyclerViewModelos.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewModelos.adapter = adapterModelos
    }

    private fun carregarModelosDeTreino() {
        db.collection("Maquina")
            // .orderBy("nome") // Opcional: ordenar
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(requireContext(), "Nenhum modelo de treino encontrado.", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }
                val tempList = mutableListOf<ModeloTreino>()
                for (document in documents) {
                    val modelo = document.toObject(ModeloTreino::class.java)
                    if (modelo != null) {
                        // modelo.id = document.id // @DocumentId faz isso
                        tempList.add(modelo)
                    }
                }
                adapterModelos.updateData(tempList)
            }
            .addOnFailureListener { exception ->
                Log.e("ListaTreinos", "Erro ao carregar modelos de treino", exception)
                Toast.makeText(requireContext(), "Erro ao carregar modelos.", Toast.LENGTH_SHORT).show()
            }
    }
}