package com.example.fitcore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns // Para validação de e-mail
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore // Importação correta
import com.google.firebase.ktx.Firebase // Importação correta

class Cadastro : AppCompatActivity() {

    private lateinit var btnRegister: Button
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etNomeCompleto: EditText
    private lateinit var etCpf: EditText

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        db = Firebase.firestore

        btnRegister = findViewById(R.id.btnRegister)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etNomeCompleto = findViewById(R.id.etNomeCompleto)
        etCpf = findViewById(R.id.etCpf)

        btnRegister.setOnClickListener {
            registrarNovoUsuario()
        }
    }

    private fun registrarNovoUsuario() {
        val email = etEmail.text.toString().trim()
        val senha = etPassword.text.toString()
        val nomeCompleto = etNomeCompleto.text.toString().trim()
        val cpf = etCpf.text.toString().trim()

        if (email.isEmpty()) {
            etEmail.error = "E-mail é obrigatório."
            etEmail.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Por favor, insira um e-mail válido."
            etEmail.requestFocus()
            return
        }

        if (senha.isEmpty()) {
            etPassword.error = "Senha é obrigatória."
            etPassword.requestFocus()
            return
        }

        if (senha.length < 6) {
            etPassword.error = "A senha deve ter no mínimo 6 caracteres."
            etPassword.requestFocus()
            return
        }

        if (nomeCompleto.isEmpty()) {
            etNomeCompleto.error = "Nome completo é obrigatório."
            etNomeCompleto.requestFocus()
            return
        }

        if (cpf.isEmpty()) {
            etCpf.error = "CPF é obrigatório."
            etCpf.requestFocus()
            return
        }

        val novoUsuario = hashMapOf(
            "email" to email,
            "senha" to senha,
            "nome" to nomeCompleto,
            "cpf" to cpf,
            "tipo" to "comum",
            "contrato" to false
        )

        db.collection("Usuario")
            .add(novoUsuario)
            .addOnSuccessListener { documentReference ->
                Log.d("CadastroActivity", "Usuário cadastrado com ID: ${documentReference.id}")
                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show()

                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Log.w("CadastroActivity", "Erro ao cadastrar usuário", e)
                Toast.makeText(this, "Erro ao realizar cadastro: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}