package com.example.fitcore

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class CentralDeInformacoes : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_central_de_informacoes)

        // Configurações de navegação
        val navHome = findViewById<LinearLayout>(R.id.nav_home)
        val navTreinos = findViewById<LinearLayout>(R.id.nav_treinos)
        val navMapeamento = findViewById<LinearLayout>(R.id.nav_mapeamento)
        val navPerfil = findViewById<LinearLayout>(R.id.nav_perfil)
        val navConfig = findViewById<LinearLayout>(R.id.nav_config)

        navHome.setOnClickListener { onNavigationItemSelected(R.id.nav_home) }
        navTreinos.setOnClickListener { onNavigationItemSelected(R.id.nav_treinos) }
        navMapeamento.setOnClickListener { onNavigationItemSelected(R.id.nav_mapeamento) }
        navPerfil.setOnClickListener { onNavigationItemSelected(R.id.nav_perfil) }
        navConfig.setOnClickListener { onNavigationItemSelected(R.id.nav_config) }

        // Início como selecionado
        onNavigationItemSelected(R.id.nav_home)

        // Listener para atualizar os ícones quando voltar
        supportFragmentManager.addOnBackStackChangedListener {
            updateNavigationHighlight()
        }
    }

    private fun highlightSelected(selectedId: Int) {
        val navItems = listOf(
            R.id.nav_home to Pair(R.id.icon_home, R.id.bg_home),
            R.id.nav_treinos to Pair(R.id.icon_treinos, R.id.bg_treinos),
            R.id.nav_mapeamento to Pair(R.id.icon_mapeamento, R.id.bg_mapeamento),
            R.id.nav_perfil to Pair(R.id.icon_perfil, R.id.bg_perfil),
            R.id.nav_config to Pair(R.id.icon_config, R.id.bg_config)
        )

        for ((layoutId, ids) in navItems) {
            val icon = findViewById<ImageView>(ids.first)
            val bg = findViewById<View>(ids.second)

            if (layoutId == selectedId) {
                bg.visibility = View.VISIBLE
                icon.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)
            } else {
                bg.visibility = View.GONE
                icon.setColorFilter(ContextCompat.getColor(this, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN)
            }
        }
    }

    private fun onNavigationItemSelected(itemId: Int) {
        val fragmentToShow: Fragment = when (itemId) {
            R.id.nav_home -> HomeFragment()
            R.id.nav_treinos -> ListaTreinosFragment()
            R.id.nav_mapeamento -> MapeamentoAcademiaUsuarioFragment()
            R.id.nav_perfil -> PerfilFragment()
            R.id.nav_config -> ConfiguracoesFragment()
            else -> return
        }

        val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_container)
        if (currentFragment != null && currentFragment::class == fragmentToShow::class) {
            return // Não faz nada se já está no fragmento atual
        }

        highlightSelected(itemId)
        replaceFragment(fragmentToShow)
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun updateNavigationHighlight() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_container)
        when (currentFragment) {
            is HomeFragment -> highlightSelected(R.id.nav_home)
            is PerfilFragment -> highlightSelected(R.id.nav_perfil)
            is ConfiguracoesFragment -> highlightSelected(R.id.nav_config)
            is ListaTreinosFragment -> highlightSelected(R.id.nav_treinos)
            is MapeamentoAcademiaUsuarioFragment -> highlightSelected(R.id.nav_mapeamento)
        }
    }
}