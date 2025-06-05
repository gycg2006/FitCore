package com.example.fitcore

import android.os.Bundle // Necessário para o onCreate

class inscricaoAulasColetivasCore : BaseInscricaoActivity() {

    override val layoutRes: Int
        get() = R.layout.activity_inscricao_aulas_coletivas_core

    override val aulaInscritosKey: String
        get() = AulasColetivas.AppConstants.KEY_INSCRITOS_CORE

    override val idBotaoVoltar: Int
        get() = R.id.voltarCore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // A maior parte da lógica agora está na BaseInscricaoActivity
        // Você pode adicionar inicializações específicas desta tela aqui, se necessário.
    }
}