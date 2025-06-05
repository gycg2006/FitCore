package com.example.fitcore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AlunoAdapter(
    private val alunos: List<Aluno>,
    private val onEditClick: (Aluno) -> Unit // Função lambda para o clique
) : RecyclerView.Adapter<AlunoAdapter.AlunoViewHolder>() {

    inner class AlunoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nomeTextView: TextView = itemView.findViewById(R.id.textViewNomeAluno)
        private val fotoImageView: ImageView = itemView.findViewById(R.id.imageViewAluno)
        private val editarButton: Button = itemView.findViewById(R.id.buttonEditarFicha)

        fun bind(aluno: Aluno) {
            nomeTextView.text = aluno.nome

            // Usa Glide para carregar a imagem da URL
            Glide.with(itemView.context)
                .load(aluno.fotoUrl)
                .placeholder(R.drawable.avatar_placeholder) // Imagem padrão enquanto carrega
                .error(R.drawable.avatar_placeholder) // Imagem padrão em caso de erro
                .circleCrop() // Deixa a imagem redonda
                .into(fotoImageView)

            // Configura o clique no botão de editar
            editarButton.setOnClickListener {
                onEditClick(aluno)
            }
        }
    }

    // Cria um novo ViewHolder quando o RecyclerView precisa
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlunoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_aluno, parent, false)
        return AlunoViewHolder(view)
    }

    // Vincula os dados de um aluno a um ViewHolder
    override fun onBindViewHolder(holder: AlunoViewHolder, position: Int) {
        holder.bind(alunos[position])
    }

    // Retorna o número total de itens na lista
    override fun getItemCount(): Int = alunos.size
}