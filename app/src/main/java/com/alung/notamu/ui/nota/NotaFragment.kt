package com.alung.notamu.ui.nota

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment

class NotaFragment : Fragment() {
    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        return TextView(requireContext()).apply { text = "Nota" }
    }
}
