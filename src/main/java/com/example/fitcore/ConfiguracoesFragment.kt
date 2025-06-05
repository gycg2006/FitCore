package com.example.fitcore

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment

class ConfiguracoesFragment : Fragment(R.layout.fragment_configuracoes) {

    private var filtroAtualSelecionado: TipoDaltonismo = TipoDaltonismo.NORMAL
    private val todosOsTiposDeFiltro = TipoDaltonismo.values()
    private var indiceDoFiltroAtual = 0

    private val TAG_FRAGMENTO_FILTRO_COR = "tagLocalFiltroCor"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Restaura o índice atual ou começa do NORMAL
        indiceDoFiltroAtual = savedInstanceState?.getInt("indiceFiltro", TipoDaltonismo.NORMAL.ordinal)
            ?: TipoDaltonismo.NORMAL.ordinal
        filtroAtualSelecionado = todosOsTiposDeFiltro[indiceDoFiltroAtual]

        val botaoVoltarConfig = view.findViewById<ImageButton>(R.id.botaoVoltarConfigC)
        val botaoRedefSenha = view.findViewById<Button>(R.id.botaoRedefSenhaC)
        val botaoDeslogar = view.findViewById<Button>(R.id.botaoDeslogarC)
        val botaoTermos = view.findViewById<Button>(R.id.botaoTermosC)
        val botaoAlterarFiltroCor = view.findViewById<Button>(R.id.botaoTermos2) // Botão do filtro

        botaoVoltarConfig.setOnClickListener {
            removerFiltroSeExistir() // Boa prática remover ao sair
            requireActivity().supportFragmentManager.popBackStack()
        }

        botaoRedefSenha.setOnClickListener {
            val intent = Intent(requireContext(), RedefinicaoDeSenha::class.java)
            startActivity(intent)
        }

        botaoDeslogar.setOnClickListener {
            // Considere resetar o filtro para NORMAL ao deslogar,
            // para não persistir o estado se o usuário logar novamente.
            // filtroAtualSelecionado = TipoDaltonismo.NORMAL
            // indiceDoFiltroAtual = filtroAtualSelecionado.ordinal
            // aplicarOuRemoverFiltroLocal() // Aplica o reset

            val intent = Intent(requireContext(), Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        botaoTermos.setOnClickListener {
            val intent = Intent(requireContext(), TermosPoliticaPrivacidade::class.java)
            startActivity(intent)
        }

        botaoAlterarFiltroCor.setOnClickListener {
            // Cicla para o próximo filtro
            indiceDoFiltroAtual = (indiceDoFiltroAtual + 1) % todosOsTiposDeFiltro.size
            filtroAtualSelecionado = todosOsTiposDeFiltro[indiceDoFiltroAtual]
            aplicarOuRemoverFiltroLocal()
        }

        // Aplica o filtro inicial (ou nenhum filtro) quando a view é criada
        aplicarOuRemoverFiltroLocal(isInitialSetup = true)
    }

    private fun aplicarOuRemoverFiltroLocal(isInitialSetup: Boolean = false) {
        // Usar childFragmentManager pois estamos gerenciando um fragmento DENTRO de outro fragmento
        val fm = childFragmentManager
        removerFiltroSeExistir() // Sempre remove o anterior para evitar sobreposição

        if (filtroAtualSelecionado != TipoDaltonismo.NORMAL) {
            val fragmentoFiltro = FiltroCorFragment.newInstance(filtroAtualSelecionado)
            fm.beginTransaction()
                .replace(R.id.containerFiltroCorLocal, fragmentoFiltro, TAG_FRAGMENTO_FILTRO_COR)
                .commit()
            if (!isInitialSetup) {
                Toast.makeText(requireContext(), "Filtro aplicado: ${filtroAtualSelecionado.name}", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Se for NORMAL, o filtro já foi removido por removerFiltroSeExistir()
            if (!isInitialSetup) {
                Toast.makeText(requireContext(), "Filtro removido.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun removerFiltroSeExistir() {
        val fm = childFragmentManager
        val fragmentoExistente = fm.findFragmentByTag(TAG_FRAGMENTO_FILTRO_COR)
        if (fragmentoExistente != null) {
            fm.beginTransaction().remove(fragmentoExistente).commit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Salva o estado do filtro para o caso de recriação do fragmento (ex: rotação da tela)
        outState.putInt("indiceFiltro", indiceDoFiltroAtual)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Opcional: remover o filtro explicitamente aqui, embora o childFragmentManager
        // deva lidar com isso quando a view do ConfiguracoesFragment é destruída.
        // removerFiltroSeExistir()
    }
}