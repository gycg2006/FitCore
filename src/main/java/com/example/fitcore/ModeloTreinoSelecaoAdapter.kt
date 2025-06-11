package com.example.fitcore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ModeloTreinoSelecaoAdapter(
    private var modelos: MutableList<ModeloTreino>,
    private val onIniciarClick: (ModeloTreino) -> Unit
) : RecyclerView.Adapter<ModeloTreinoSelecaoAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nome: TextView = itemView.findViewById(R.id.textViewModeloNome)
        val descricao: TextView = itemView.findViewById(R.id.textViewModeloDescricao)
        val imagem: ImageView = itemView.findViewById(R.id.imageViewModeloIcon) // Assumindo que ModeloTreino terá um campo para imagem
        val informacoes: TextView = itemView.findViewById(R.id.textView6)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_modelo_treino_selecao, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val modelo = modelos[position]
        holder.nome.text = modelo.nome
        holder.descricao.text = modelo.descricao ?: "3/4 Séries"
//        holder.informacoes.text = modelo.series ?: "Sem informações."

        holder.imagem.setImageResource(R.drawable.benchpress800) // Ou outra que você tenha


    }

    override fun getItemCount(): Int = modelos.size

    fun updateData(newModelos: List<ModeloTreino>) {
        modelos.clear()
        modelos.addAll(newModelos)
        notifyDataSetChanged()
    }
}