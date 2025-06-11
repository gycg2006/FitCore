package com.example.fitcore

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout // Import FrameLayout
import androidx.fragment.app.Fragment

// Enum TipoDaltonismo (mantenha ou defina em um local acessível)
public enum class TipoDaltonismo(val overlayColor: Int) {
    NORMAL(Color.TRANSPARENT),
    PROTANOPIA(Color.argb(70, 0, 100, 100)),
    DEUTERANOPIA(Color.argb(70, 100, 0, 100)),
    TRITANOPIA(Color.argb(70, 100, 100, 0))
}

class FiltroCorFragment : Fragment() {

    private var tipoDaltonismoAtual: TipoDaltonismo = TipoDaltonismo.NORMAL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // Pega o tipo de daltonismo dos argumentos, default para NORMAL se não encontrar
            tipoDaltonismoAtual = TipoDaltonismo.values()[it.getInt(ARG_TIPO_DALTONISMO, TipoDaltonismo.NORMAL.ordinal)]
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filtro_cor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // O 'view' aqui é o FrameLayout raiz do fragment_filtro_cor.xml
        aplicarFiltro(view as FrameLayout) // Cast seguro se o layout raiz é FrameLayout
    }

    private fun aplicarFiltro(containerRaizDoFiltro: FrameLayout) {
        val corDoFiltro = when (tipoDaltonismoAtual) {
            TipoDaltonismo.PROTANOPIA -> Color.argb(70, 0, 100, 100)  // Opacidade (70 de 255) e cor
            TipoDaltonismo.DEUTERANOPIA -> Color.argb(70, 100, 0, 100)
            TipoDaltonismo.TRITANOPIA -> Color.argb(70, 100, 100, 0)
            TipoDaltonismo.NORMAL -> Color.TRANSPARENT
        }
        containerRaizDoFiltro.setBackgroundColor(corDoFiltro)
    }

    companion object {
        private const val ARG_TIPO_DALTONISMO = "tipo_daltonismo_arg"

        @JvmStatic
        fun newInstance(tipo: TipoDaltonismo) =
            FiltroCorFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_TIPO_DALTONISMO, tipo.ordinal)
                }
            }
    }
}