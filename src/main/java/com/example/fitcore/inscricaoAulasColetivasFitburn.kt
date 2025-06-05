package com.example.fitcore

import android.os.Bundle

class inscricaoAulasColetivasFitburn : BaseInscricaoActivity() {

    override val layoutRes: Int
        get() = R.layout.activity_inscricao_aulas_coletivas_fitburn

    override val aulaInscritosKey: String
        get() = AulasColetivas.AppConstants.KEY_INSCRITOS_FITBURN

    override val idBotaoVoltar: Int
        get() = R.id.voltarFitburn // ID do XML específico desta tela

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}