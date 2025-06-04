package com.example.fitcore

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView


class MensagemAdapter(private val mensagens: List<Mensagem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TIPO_USUARIO = 0
        private const val TIPO_IA = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (mensagens[position].ehUsuario) TIPO_USUARIO else TIPO_IA
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TIPO_USUARIO) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mensagem_usuario, parent, false)
            UsuarioViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mensagem_ia, parent, false)
            IaViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mensagem = mensagens[position]
        when (holder) {
            is UsuarioViewHolder -> holder.mensagemTextView.text = mensagem.texto
            is IaViewHolder -> holder.mensagemTextView.text = mensagem.texto
        }
    }

    override fun getItemCount(): Int = mensagens.size

    class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mensagemTextView: TextView = itemView.findViewById(R.id.textoMensagemUsuario)
    }

    class IaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mensagemTextView: TextView = itemView.findViewById(R.id.textoMensagemIA)
    }
}