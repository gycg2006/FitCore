package com.example.fitcore // Ensure this is your correct package

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings // For checking overlay permission directly
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fitcore.services.ColorFilterService // Import the service

// TipoDaltonismo enum should be in its own file or accessible globally
// For this example, assuming TipoDaltonismo.kt exists in com.example.fitcore

class ConfiguracoesFragment : Fragment(R.layout.fragment_configuracoes) {

    private var filtroAtualSelecionado: TipoDaltonismo = TipoDaltonismo.NORMAL
    private val todosOsTiposDeFiltro: Array<TipoDaltonismo> = TipoDaltonismo.values()
    private var indiceDoFiltroAtual: Int = 0

    // SharedPreferences constants should ideally match those in the Service
    // or be defined in a shared constants file.
    private val PREFS_NAME = "FilterPrefs"
    private val PREF_CURRENT_FILTER_ORDINAL = "currentFilterOrdinal"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Restore current filter index from SharedPreferences
        val prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        indiceDoFiltroAtual = prefs.getInt(PREF_CURRENT_FILTER_ORDINAL, TipoDaltonismo.NORMAL.ordinal)
        filtroAtualSelecionado = todosOsTiposDeFiltro[indiceDoFiltroAtual]

        val botaoVoltarConfig = view.findViewById<ImageButton>(R.id.botaoVoltarConfigC)
        val botaoRedefSenha = view.findViewById<Button>(R.id.botaoRedefSenhaC)
        val botaoDeslogar = view.findViewById<Button>(R.id.botaoDeslogarC)
        val botaoTermos = view.findViewById<Button>(R.id.botaoTermosC)
        val botaoAlterarFiltroCor = view.findViewById<Button>(R.id.botaoTermos2) // Botão do filtro

        updateFilterButtonText(botaoAlterarFiltroCor)

        botaoVoltarConfig.setOnClickListener {
            // No local filter to remove here
            requireActivity().supportFragmentManager.popBackStack()
        }

        botaoRedefSenha.setOnClickListener {
            val intent = Intent(requireContext(), RedefinicaoDeSenha::class.java) // Assume these classes exist
            startActivity(intent)
        }

        botaoDeslogar.setOnClickListener {
            // Reset filter to NORMAL on logout
            filtroAtualSelecionado = TipoDaltonismo.NORMAL
            indiceDoFiltroAtual = filtroAtualSelecionado.ordinal
            aplicarFiltroGlobalmente() // Apply the reset

            val intent = Intent(requireContext(), Login::class.java) // Assume Login class exists
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        botaoTermos.setOnClickListener {
            val intent = Intent(requireContext(), TermosPoliticaPrivacidade::class.java) // Assume this class exists
            startActivity(intent)
        }

        botaoAlterarFiltroCor.setOnClickListener {
            // Cycle to the next filter
            indiceDoFiltroAtual = (indiceDoFiltroAtual + 1) % todosOsTiposDeFiltro.size
            filtroAtualSelecionado = todosOsTiposDeFiltro[indiceDoFiltroAtual]
            aplicarFiltroGlobalmente()
            updateFilterButtonText(it as Button)
        }
        // No initial local filter application needed
    }

    private fun aplicarFiltroGlobalmente() {
        if (ColorFilterService.checkAndRequestOverlayPermission(requireActivity())) {
            // Permission granted or not needed
            ColorFilterService.setFilter(requireContext(), filtroAtualSelecionado)
            Toast.makeText(
                requireContext(),
                if (filtroAtualSelecionado == TipoDaltonismo.NORMAL) "Filtro removido."
                else "Filtro aplicado: ${filtroAtualSelecionado.name}",
                Toast.LENGTH_SHORT
            ).show()

            // Save preference (also done by service, but good for immediate UI reflection if needed)
            val prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit().putInt(PREF_CURRENT_FILTER_ORDINAL, indiceDoFiltroAtual).apply()
        } else {
            Toast.makeText(requireContext(), "Por favor, conceda a permissão de sobreposição para aplicar o filtro.", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateFilterButtonText(button: Button) {
        button.text = "Filtro de Cor: ${filtroAtualSelecionado.name}"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ColorFilterService.OVERLAY_PERMISSION_REQUEST_CODE) {
            // Check if permission was granted after returning from settings
            if (Settings.canDrawOverlays(requireContext())) {
                Toast.makeText(requireContext(), "Permissão concedida. Tente aplicar o filtro novamente.", Toast.LENGTH_SHORT).show()
                // You could automatically apply it here, or let the user click again.
                // For simplicity, we'll let the user click again or it will be applied if they change it.
                // If you want to re-apply immediately:
                // aplicarFiltroGlobalmente()
                // updateFilterButtonText(requireView().findViewById<Button>(R.id.botaoTermos2))
            } else {
                Toast.makeText(requireContext(), "Permissão de sobreposição não concedida.", Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("indiceFiltroVisualNoFragment", indiceDoFiltroAtual) // Different key to avoid confusion
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Restore the visual index for the fragment if it was saved,
        // but the actual filter state comes from SharedPreferences via service.
        savedInstanceState?.let {
            indiceDoFiltroAtual = it.getInt("indiceFiltroVisualNoFragment", TipoDaltonismo.NORMAL.ordinal)
            // Note: filtroAtualSelecionado will be set in onViewCreated from SharedPreferences
        }
    }
}