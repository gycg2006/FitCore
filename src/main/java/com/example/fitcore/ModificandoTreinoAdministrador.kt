package com.example.fitcore

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ModificandoTreinoAdministrador : AppCompatActivity() {

    private lateinit var recyclerViewFicha: RecyclerView
    private lateinit var adapter: FichaTreinoAdapter
    private lateinit var buttonAdicionarExercicio: Button
    private lateinit var buttonConcluir: Button
    private lateinit var textViewNomeAlunoFicha: TextView


    private val db = Firebase.firestore
    private var alunoId: String? = null
    private var alunoNome: String? = null // Para exibir o nome do aluno
    private val exerciciosList = mutableListOf<FichaExercicio>()
    private var fichaListener: ListenerRegistration? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // O nome do layout é activity_modificando_ficha_do_aluno.xml
        setContentView(R.layout.activity_modificando_ficha_do_aluno)

        alunoId = intent.getStringExtra("ALUNO_ID")
        alunoNome = intent.getStringExtra("ALUNO_NOME") // Pegar o nome do aluno também

        if (alunoId == null) {
            Toast.makeText(this, "ID do aluno não encontrado.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        textViewNomeAlunoFicha = findViewById(R.id.textViewNomeAlunoFicha)
        textViewNomeAlunoFicha.text = alunoNome ?: "Aluno sem nome"


        recyclerViewFicha = findViewById(R.id.recyclerViewFichaExercicios)
        buttonAdicionarExercicio = findViewById(R.id.buttonAdicionarExercicioFicha)
        buttonConcluir = findViewById(R.id.buttonConcluirFicha)

        setupRecyclerView()
        loadFichaTreino()

        buttonAdicionarExercicio.setOnClickListener {
            abrirDialogAdicionarEditarExercicio(null) // null para novo exercício
        }

        buttonConcluir.setOnClickListener {
            finish() // Apenas fecha a tela, as alterações são salvas em tempo real
        }
    }

    private fun setupRecyclerView() {
        adapter = FichaTreinoAdapter(
            exerciciosList,
            onEditClick = { exercicio ->
                abrirDialogAdicionarEditarExercicio(exercicio)
            },
            onRemoveClick = { exercicio ->
                removerExercicioDaFicha(exercicio)
            }
        )
        recyclerViewFicha.layoutManager = LinearLayoutManager(this)
        recyclerViewFicha.adapter = adapter
    }

    private fun loadFichaTreino() {
        alunoId?.let { id ->
            fichaListener = db.collection("Alunos").document(id)
                .collection("FichaTreino")
                .orderBy("ordem", Query.Direction.ASCENDING) // Ordena pela ordem
                .addSnapshotListener { snapshots, e ->
                    if (e != null) {
                        Log.w("Firestore", "Erro ao ouvir a ficha de treino.", e)
                        Toast.makeText(this, "Erro ao carregar ficha.", Toast.LENGTH_SHORT).show()
                        return@addSnapshotListener
                    }

                    val novaLista = mutableListOf<FichaExercicio>()
                    for (doc in snapshots!!) {
                        val exercicio = doc.toObject(FichaExercicio::class.java)
                        // exercicio.id = doc.id // O @DocumentId já faz isso
                        novaLista.add(exercicio)
                    }
                    exerciciosList.clear()
                    exerciciosList.addAll(novaLista)
                    adapter.notifyDataSetChanged() // Ou usar DiffUtil para melhor performance
                    Log.d("Firestore", "Ficha carregada: ${exerciciosList.size} exercícios.")
                }
        }
    }

    private fun abrirDialogAdicionarEditarExercicio(exercicioExistente: FichaExercicio?) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit_exercicio_ficha, null)
        val editTextNomeExercicioDialog = dialogView.findViewById<EditText>(R.id.editTextNomeExercicioDialog)
        val editTextMidiaUrlDialog = dialogView.findViewById<EditText>(R.id.editTextMidiaUrlDialog)
        val editTextSeriesDialog = dialogView.findViewById<EditText>(R.id.editTextSeriesDialog)
        val editTextRepeticoesDialog = dialogView.findViewById<EditText>(R.id.editTextRepeticoesDialog)
        val editTextObservacoesDialog = dialogView.findViewById<EditText>(R.id.editTextObservacoesDialog)
        // Opcional: EditText para ordem se você quiser permitir edição manual da ordem

        val dialogTitle = if (exercicioExistente == null) "Adicionar Exercício à Ficha" else "Editar Exercício da Ficha"

        exercicioExistente?.let {
            editTextNomeExercicioDialog.setText(it.exercicioNome)
            editTextMidiaUrlDialog.setText(it.exercicioMidiaUrl)
            editTextSeriesDialog.setText(it.series)
            editTextRepeticoesDialog.setText(it.repeticoes)
            editTextObservacoesDialog.setText(it.observacoes)
        }

        AlertDialog.Builder(this)
            .setTitle(dialogTitle)
            .setView(dialogView)
            .setPositiveButton(if (exercicioExistente == null) "Adicionar" else "Salvar") { dialog, _ ->
                val nome = editTextNomeExercicioDialog.text.toString().trim()
                val midiaUrl = editTextMidiaUrlDialog.text.toString().trim() // Idealmente, selecionar de uma lista
                val series = editTextSeriesDialog.text.toString().trim()
                val repeticoes = editTextRepeticoesDialog.text.toString().trim()
                val observacoes = editTextObservacoesDialog.text.toString().trim()

                if (nome.isEmpty() || series.isEmpty() || repeticoes.isEmpty()) {
                    Toast.makeText(this, "Nome, séries e repetições são obrigatórios.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val ordemAtual = if (exercicioExistente != null) exercicioExistente.ordem else (exerciciosList.maxOfOrNull { it.ordem } ?: 0) + 1


                val exercicioParaSalvar = FichaExercicio(
                    id = exercicioExistente?.id ?: "", // Vazio para novo, preenchido para edição
                    exercicioNome = nome,
                    exercicioMidiaUrl = midiaUrl, // Usar uma URL de imagem padrão se vazia ou permitir upload
                    series = series,
                    repeticoes = repeticoes,
                    observacoes = observacoes.ifEmpty { null },
                    ordem = ordemAtual
                )

                salvarExercicioNaFicha(exercicioParaSalvar, exercicioExistente == null)
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }
            .create()
            .show()
    }

    private fun salvarExercicioNaFicha(exercicio: FichaExercicio, isNew: Boolean) {
        alunoId?.let { aid ->
            val fichaCollectionRef = db.collection("Alunos").document(aid).collection("FichaTreino")

            if (isNew) {
                // Adicionar novo exercício
                fichaCollectionRef.add(exercicio)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Exercício adicionado!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Erro ao adicionar: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                // Atualizar exercício existente
                // O ID do exercício na ficha é crucial aqui e já deve estar no objeto 'exercicio'
                if (exercicio.id.isNotEmpty()) {
                    fichaCollectionRef.document(exercicio.id).set(exercicio) // set() sobrescreve, update() atualiza campos
                        .addOnSuccessListener {
                            Toast.makeText(this, "Exercício atualizado!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Erro ao atualizar: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Erro: ID do exercício não encontrado para atualização.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun removerExercicioDaFicha(exercicio: FichaExercicio) {
        alunoId?.let { aid ->
            if (exercicio.id.isNotEmpty()) {
                AlertDialog.Builder(this)
                    .setTitle("Remover Exercício")
                    .setMessage("Tem certeza que deseja remover '${exercicio.exercicioNome}' da ficha?")
                    .setPositiveButton("Remover") { _, _ ->
                        db.collection("Alunos").document(aid)
                            .collection("FichaTreino").document(exercicio.id)
                            .delete()
                            .addOnSuccessListener {
                                Toast.makeText(this, "${exercicio.exercicioNome} removido.", Toast.LENGTH_SHORT).show()
                                // A lista será atualizada automaticamente pelo SnapshotListener
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Erro ao remover: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            } else {
                Toast.makeText(this, "Erro: ID do exercício não encontrado para remoção.", Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        fichaListener?.remove() // Importante para evitar memory leaks
    }
}