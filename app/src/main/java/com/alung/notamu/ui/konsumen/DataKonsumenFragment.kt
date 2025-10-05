package com.alung.notamu.ui.konsumen

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alung.notamu.data.entity.Konsumen
import com.alung.notamu.databinding.FragmentKonsumenListBinding
import com.alung.notamu.ui.konsumen.adapter.KonsumenAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DataKonsumenFragment : Fragment() {

    private var _b: FragmentKonsumenListBinding? = null
    private val b get() = _b!!

    private val vm: KonsumenViewModel by viewModels()
    private lateinit var adapter: KonsumenAdapter
    private var editing: Konsumen? = null

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentKonsumenListBinding.inflate(i, c, false)
        return b.root
    }

    override fun onViewCreated(v: View, s: Bundle?) {
        super.onViewCreated(v, s)

        adapter = KonsumenAdapter { k ->
            b.etNama.setText(k.nama)
            b.etAlamat.setText(k.alamat ?: "")
            b.etTelp.setText(k.telp ?: "")
            editing = k
            b.btnSimpan.text = "Update"
        }

        b.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        b.recyclerView.adapter = adapter
        vm.items.observe(viewLifecycleOwner) { adapter.submitList(it) }

        b.btnSimpan.setOnClickListener {
            val nama = b.etNama.text?.toString()?.trim().orEmpty()
            val alamat = b.etAlamat.text?.toString()?.trim().orEmpty()
            val telp = b.etTelp.text?.toString()?.trim().orEmpty()
            if (nama.isEmpty()) {
                Toast.makeText(requireContext(), "Nama wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val cur = editing
            if (cur == null) {
                vm.add(nama, alamat.ifEmpty { null }, telp.ifEmpty { null })
            } else {
                vm.update(cur.copy(
                    nama = nama,
                    alamat = alamat.ifEmpty { null },
                    telp = telp.ifEmpty { null }
                ))
                editing = null
                b.btnSimpan.text = "Simpan"
            }
            b.etNama.setText(""); b.etAlamat.setText(""); b.etTelp.setText("")
        }
    }

    override fun onDestroyView() {
        _b = null
        super.onDestroyView()
    }
}
