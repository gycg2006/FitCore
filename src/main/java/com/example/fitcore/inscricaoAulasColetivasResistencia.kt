package com.example.fitcore

import android.os.Bundle

class inscricaoAulasColetivasResistencia : BaseInscricaoActivity() {

    override val layoutRes: Int
        get() = R.layout.activity_inscricao_aulas_coletivas_resistencia

    override val aulaInscritosKey: String
        get() = AulasColetivas.AppConstants.KEY_INSCRITOS_RESISTENCIA

    override val idBotaoVoltar: Int
        get() = R.id.voltarresistencia // ID do XML específico desta tela

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}