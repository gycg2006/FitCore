package com.example.fitcore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RedefinicaoDeSenha : AppCompatActivity() {

    private lateinit var etEmailSolicitacao: EditText
    private lateinit var btnEnviarSolicitacao: Button
    private lateinit var btnCancelarSolicitacao: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_redefinicao_de_senha)

        etEmailSolicitacao = findViewById(R.id.etEmailSolicitacao)
        btnEnviarSolicitacao = findViewById(R.id.btnEnviarSolicitacao)
        btnCancelarSolicitacao = findViewById(R.id.btnCancelarSolicitacao)

        auth = Firebase.auth

        btnEnviarSolicitacao.setOnClickListener {
            enviarEmailRedefinicaoSenha()
        }

        btnCancelarSolicitacao.setOnClickListener {
            finish()
        }
    }

    private fun enviarEmailRedefinicaoSenha() {
        val email = etEmailSolicitacao.text.toString().trim()

        if (email.isEmpty()) {
            etEmailSolicitacao.error = "E-mail é obrigatório."
            etEmailSolicitacao.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmailSolicitacao.error = "Formato de e-mail inválido."
            etEmailSolicitacao.requestFocus()
            return
        }

        btnEnviarSolicitacao.isEnabled = false

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("SolicitarRedefinicao", "E-mail de redefinição enviado para $email.")
                    Toast.makeText(this, "Se o e-mail estiver cadastrado, você receberá um link para redefinir sua senha.", Toast.LENGTH_LONG).show()
                    navegarParaTelaDeConfirmacao()
                } else {
                    Log.w("SolicitarRedefinicao", "Falha ao enviar e-mail de redefinição para $email", task.exception)
                    Toast.makeText(this, "Falha ao enviar e-mail de redefinição. Verifique o e-mail ou tente novamente.", Toast.LENGTH_LONG).show()
                }
                btnEnviarSolicitacao.isEnabled = true
            }
    }

    private fun navegarParaTelaDeConfirmacao() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }
}