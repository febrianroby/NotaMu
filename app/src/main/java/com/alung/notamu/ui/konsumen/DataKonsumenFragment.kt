package com.alung.notamu.ui.konsumen

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.alung.notamu.R

class DataKonsumenFragment : Fragment() {
    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        return TextView(requireContext()).apply { text = getString(R.string.title_data_konsumen) }
    }
}
