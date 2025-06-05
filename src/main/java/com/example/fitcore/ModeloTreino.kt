package com.example.fitcore

import com.google.firebase.firestore.DocumentId

data class ModeloTreino(
    @DocumentId
    var id: String = "",
    var nome: String = "",
    var descricao: String? = null
)