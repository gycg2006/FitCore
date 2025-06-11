package com.example.fitcore

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide // Para carregar imagens
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FichaTreinoAdapter(
    private var exercicios: MutableList<FichaExercicio>,
    private val onEditClick: (FichaExercicio) -> Unit,
    private val onRemoveClick: (FichaExercicio) -> Unit
) : RecyclerView.Adapter<FichaTreinoAdapter.FichaExercicioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FichaExercicioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ficha_exercicio, parent, false)
        return FichaExercicioViewHolder(view)
    }

    override fun onBindViewHolder(holder: FichaExercicioViewHolder, position: Int) {
        val exercicio = exercicios[position]
        holder.bind(exercicio)
    }

    override fun getItemCount(): Int = exercicios.size

    fun updateExercicios(newExercicios: List<FichaExercicio>) {
        exercicios.clear()
        exercicios.addAll(newExercicios)
        notifyDataSetChanged() // Pode ser otimizado com DiffUtil
    }

    inner class FichaExercicioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nomeExercicio: TextView = itemView.findViewById(R.id.textViewExercicioNome)
        private val midiaExercicio: ImageView = itemView.findViewById(R.id.imageViewExercicioMidia)
        private val seriesRepeticoes: TextView =
            itemView.findViewById(R.id.textViewSeriesRepeticoes)
        private val observacoes: TextView = itemView.findViewById(R.id.textViewObservacoes)
        private val btnEdit: Button = itemView.findViewById(R.id.buttonEditarItemFicha)
        private val btnRemove: Button = itemView.findViewById(R.id.buttonRemoverItemFicha)

        private val db = Firebase.firestore

        fun bind(fichaExercicio: FichaExercicio) {
            nomeExercicio.text = fichaExercicio.exercicioNome
            seriesRepeticoes.text =
                "Séries: ${fichaExercicio.series}, Repetições: ${fichaExercicio.repeticoes}"
            observacoes.text = fichaExercicio.observacoes ?: "Sem observações."

            val novasInformacoes = hashMapOf(
                "nome" to fichaExercicio.exercicioNome,
                "urlFoto" to "",
                "observacoes" to fichaExercicio.observacoes,
                "repeticoes" to fichaExercicio.repeticoes,
                "series" to fichaExercicio.series
            )

            db.collection("Maquina").get().addOnSuccessListener { result ->
                var cont: Int = 0
                for (doc in result) {
                    Log.d("results", doc.get("nome").toString())
                    if (doc.get("nome").toString() == fichaExercicio.exercicioNome) {
                        cont++
                    }
                }
                if (cont == 0) {
                    db.collection("Maquina")
                        .add(novasInformacoes)
                }

                Glide.with(itemView.context)
                    .load(fichaExercicio.exercicioMidiaUrl)
                    .placeholder(R.drawable.avatar_placeholder) // Adicione um placeholder
                    .error(R.drawable.avatar_placeholder)       // Adicione uma imagem de erro
                    .into(midiaExercicio)

                btnEdit.setOnClickListener { onEditClick(fichaExercicio) }
                btnRemove.setOnClickListener {
                    onRemoveClick(fichaExercicio)
                }
            }
        }
    }
}