package com.example.fitcore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    object LoginCredentials {
        var emailLogado = ""
        var nome = ""
        var cpf = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        db = Firebase.firestore

        val emailEditText = findViewById<EditText>(R.id.email)
        val senhaEditText = findViewById<EditText>(R.id.senha)
        val btnContinuar = findViewById<Button>(R.id.btnContinuar)
        val tvEsqueceuSenha = findViewById<TextView>(R.id.tvEsqueceuSenha)
        val tvCadastrar = findViewById<TextView>(R.id.tvCadastrar)
        val termosUso = findViewById<TextView>(R.id.termos_uso)

        tvEsqueceuSenha.setOnClickListener {
            val intent = Intent(this, RedefinicaoDeSenha::class.java)
            startActivity(intent)
        }

        tvCadastrar.setOnClickListener {
            val intent = Intent(this, Cadastro::class.java)
            startActivity(intent)
        }

        termosUso.setOnClickListener {
            val intent = Intent(this, TermosPoliticaPrivacidade::class.java)
            startActivity(intent)
        }

        btnContinuar.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val senhaDigitada = senhaEditText.text.toString()

            if (email.isEmpty()) {
                emailEditText.error = "Preencha o e-mail"
                emailEditText.requestFocus()
                return@setOnClickListener
            }

            if (senhaDigitada.isEmpty()) {
                senhaEditText.error = "Preencha a senha"
                senhaEditText.requestFocus()
                return@setOnClickListener
            }

            db.collection("Usuario")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        Toast.makeText(baseContext, "E-mail ou senha inválidos.", Toast.LENGTH_LONG).show()
                        Log.d("LoginFirestore", "Nenhum documento encontrado para o email: $email")
                    } else {
                        val userDocument = documents.documents[0]
                        val senhaDoBanco = userDocument.getString("senha")
                        val tipoUsuario = userDocument.getString("tipo")


                        if (senhaDoBanco == senhaDigitada) {
                            Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                            Log.d("LoginFirestore", "Login bem-sucedido para: $email, tipo: $tipoUsuario")

                            if (tipoUsuario == "admin") {
                                val intent = Intent(this, CentralAdministrador::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                LoginCredentials.emailLogado = userDocument.getString("email")!!
                                LoginCredentials.nome = userDocument.getString("nome")!!
                                LoginCredentials.cpf = userDocument.getString("cpf")!!
                                val intent = Intent(this, CentralDeInformacoes::class.java)
                                startActivity(intent)
                                finish()
                            }
                        } else {
                            Toast.makeText(baseContext, "E-mail ou senha inválidos.", Toast.LENGTH_LONG).show()
                            Log.d("LoginFirestore", "Senha incorreta para o email: $email")
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("LoginFirestore", "Erro ao buscar usuário: ", exception)
                    Toast.makeText(baseContext, "Erro ao tentar fazer login. Tente novamente.", Toast.LENGTH_LONG).show()
                }
        }
    }
}