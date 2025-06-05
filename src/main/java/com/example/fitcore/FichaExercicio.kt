package com.example.fitcore

import com.google.firebase.firestore.DocumentId

data class FichaExercicio(
    @DocumentId
    var id: String = "",

    var exercicioIdMaster: String = "",
    var exercicioNome: String = "",
    var exercicioMidiaUrl: String = "",
    var series: String = "",
    var repeticoes: String = "",
    var observacoes: String? = null,
    var ordem: Long = 0
)