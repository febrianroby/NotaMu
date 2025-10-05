package com.alung.notamu.ui.barang

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
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

        // default "Kg" dan select-all saat fokus
        b.etSatuan.apply {
            if (text.isNullOrBlank()) setText("Kg")
            setSelectAllOnFocus(true)
        }

        // Setup RecyclerView
        adapter = BarangAdapter { item -> fillForm(item) }
        b.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        b.recyclerView.adapter = adapter
        b.recyclerView.setHasFixedSize(true)
        b.recyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )

        // Observe data barang dari ViewModel ini (bukan dari NotaViewModel)
        vm.items.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }

        // Tombol simpan / update
        b.btnSimpan.setOnClickListener {
            val nama   = b.etNama.text?.toString()?.trim().orEmpty()
            val stok   = b.etStok.text?.toString()?.toDoubleOrNull()   // stok Double
            val satuan = b.etSatuan.text?.toString()?.trim().orEmpty()
            val harga  = b.etHarga.text?.toString()?.toLongOrNull()    // harga Long (rupiah)

            if (nama.isEmpty() || stok == null || stok < 0.0 || satuan.isEmpty() || harga == null || harga < 0L) {
                Toast.makeText(requireContext(), "Lengkapi data dengan benar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cur = editing
            if (cur == null) {
                // Tambah
                vm.addBarang(nama, stok, satuan, harga)
                Toast.makeText(requireContext(), "Barang ditambahkan", Toast.LENGTH_SHORT).show()
            } else {
                // Update
                vm.updateBarang(
                    cur.copy(
                        nama = nama,
                        stok = stok,
                        satuan = satuan,
                        harga = harga
                    )
                )
                Toast.makeText(requireContext(), "Barang diupdate", Toast.LENGTH_SHORT).show()
            }
            resetForm()
        }
    }

    /** Masuk mode edit */
    private fun fillForm(item: Barang) {
        editing = item
        b.etNama.setText(item.nama)
        b.etStok.setText(item.stok.toString())
        b.etSatuan.setText(item.satuan)
        b.etHarga.setText(item.harga.toString())
        b.btnSimpan.text = "Update"
    }

    /** Kembali ke mode tambah */
    private fun resetForm() {
        editing = null
        b.btnSimpan.text = "Simpan"
        b.etNama.text?.clear()
        b.etStok.text?.clear()
        b.etHarga.text?.clear()
        b.etSatuan.setText("Kg")
        b.etNama.requestFocus()
    }

    override fun onDestroyView() {
        editing = null
        _b = null
        super.onDestroyView()
    }
}
