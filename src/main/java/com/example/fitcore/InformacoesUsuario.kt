package com.example.fitcore
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class InformacoesUsuario : AppCompatActivity() {
    lateinit var VoltarUsuario: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informacoes_usuario)

        VoltarUsuario = findViewById(R.id.botaoVoltarUsuario)

    }

    override fun onStart() {
        super.onStart()
        VoltarUsuario.setOnClickListener {
                finish()
        }
    }


}
