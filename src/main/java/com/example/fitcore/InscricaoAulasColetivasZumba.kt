package com.example.fitcore

import android.os.Bundle

class InscricaoAulasColetivasZumba : BaseInscricaoActivity() {

    override val layoutRes: Int
        get() = R.layout.activity_inscricao_aulas_coletivas_zumba

    override val aulaInscritosKey: String
        get() = AulasColetivas.AppConstants.KEY_INSCRITOS_ZUMBA

    override val idBotaoVoltar: Int
        get() = R.id.voltarZumba // ID do XML específico desta tela

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}