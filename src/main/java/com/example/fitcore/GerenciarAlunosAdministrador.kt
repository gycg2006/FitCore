package com.example.fitcore

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class GerenciarAlunosAdministrador : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var alunoAdapter: AlunoAdapter
    private val listaDeAlunos = mutableListOf<Aluno>()
    private lateinit var db: FirebaseFirestore

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gerenciar_alunos_administrador)

        // Inicializa o Firestore
        db = Firebase.firestore

        // Configura o RecyclerView
        recyclerView = findViewById(R.id.recyclerViewAlunos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Cria o adapter passando a lista e a lógica de clique
        alunoAdapter = AlunoAdapter(listaDeAlunos) { aluno ->
            // Ação de clique: navegar para a tela de edição
            val intent = Intent(this, ModificandoTreinoAdministrador::class.java)
            intent.putExtra("ALUNO_ID", aluno.id) // Passa o ID do aluno para a próxima tela
            intent.putExtra("ALUNO_NOME", aluno.nome) // Opcional: passar outros dados
            startActivity(intent)
        }
        recyclerView.adapter = alunoAdapter

        // Busca os alunos do Firestore
        fetchAlunos()

        // Configura o botão de voltar
        val botaoVoltar = findViewById<Button>(R.id.buttonVoltarListaAlunos)
        botaoVoltar.setOnClickListener {
            val intent = Intent(this, CentralAdministrador::class.java)
            startActivity(intent)
        }
    }

    private fun fetchAlunos() {
        // Log para saber que a função começou
        Log.d("FirestoreDebug", "Iniciando a busca por alunos...")

        db = Firebase.firestore // Garanta que o db está inicializado
        db.collection("Usuario")
            .get()
            .addOnSuccessListener { result ->
                // Log para saber se a busca teve sucesso e quantos itens vieram
                Log.d("FirestoreDebug", "Sucesso! Documentos encontrados: ${result.size()}")

                if (result.isEmpty) {
                    Log.w("FirestoreDebug", "A consulta retornou vazia. Verifique o nome da coleção.")
                }

                listaDeAlunos.clear()
                for (document in result) {
                    try {
                        if (document.toObject(Aluno::class.java).tipo == "comum") {
                            val aluno = document.toObject(Aluno::class.java).copy(id = document.id)
                            listaDeAlunos.add(aluno)
                            // Log para cada aluno que for processado com sucesso
                            Log.d("FirestoreDebug", "Aluno mapeado com sucesso: ${aluno.nome}")
                        }
                    } catch (e: Exception) {
                        // Log de erro se o documento do Firestore não for compatível com sua classe Aluno
                        Log.e("FirestoreDebug", "Erro ao mapear o documento ${document.id}", e)
                    }
                }
                alunoAdapter.notifyDataSetChanged() // Notifica o adapter
            }
            .addOnFailureListener { exception ->
                // Log CRÍTICO para saber se houve uma falha na conexão ou permissão
                Log.e("FirestoreDebug", "FALHA ao buscar alunos.", exception)
                Toast.makeText(this, "Erro ao carregar alunos. Verifique o Logcat.", Toast.LENGTH_LONG).show()
            }
    }
}