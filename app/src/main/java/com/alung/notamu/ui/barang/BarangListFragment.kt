// app/src/main/java/com/alung/notamu/ui/barang/BarangListFragment.kt
package com.alung.notamu.ui.barang

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alung.notamu.data.entity.Barang
import com.alung.notamu.databinding.FragmentBarangListBinding
import com.alung.notamu.ui.barang.adapter.BarangAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BarangListFragment : Fragment() {

    private var _b: FragmentBarangListBinding? = null
    private val b get() = _b!!

    private val vm: BarangViewModel by viewModels()
    private var editing: Barang? = null
    private lateinit var adapter: BarangAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _b = FragmentBarangListBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BarangAdapter { item ->
            // klik item -> masuk mode edit (opsional)
            b.etNama.setText(item.nama)
            b.etStok.setText(item.stok.toString())
            b.etSatuan.setText(item.satuan)
            b.etHarga.setText(item.harga.toString())
            editing = item
            b.btnSimpan.text = "Update"
        }

        b.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        b.recyclerView.adapter = adapter

        vm.items.observe(viewLifecycleOwner) { list -> adapter.submitList(list) }

        b.btnSimpan.setOnClickListener {
            val nama   = b.etNama.text?.toString()?.trim().orEmpty()
            val stok   = b.etStok.text?.toString()?.toIntOrNull()
            val satuan = b.etSatuan.text?.toString()?.trim().orEmpty()
            val harga  = b.etHarga.text?.toString()?.toDoubleOrNull()

            if (nama.isEmpty() || stok == null || satuan.isEmpty() || harga == null) {
                Toast.makeText(requireContext(), "Lengkapi data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cur = editing
            if (cur == null) {
                vm.addBarang(nama, stok, satuan, harga)
            } else {
                vm.updateBarang(cur.copy(nama = nama, stok = stok, satuan = satuan, harga = harga))
                editing = null
                b.btnSimpan.text = "Simpan"
            }

            b.etNama.setText(""); b.etStok.setText("")
            b.etSatuan.setText(""); b.etHarga.setText("")
        }
    }

    override fun onDestroyView() {
        _b = null
        super.onDestroyView()
    }
}
