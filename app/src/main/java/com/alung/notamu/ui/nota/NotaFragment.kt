package com.alung.notamu.ui.nota

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alung.notamu.databinding.FragmentNotaBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.alung.notamu.data.entity.Barang

@AndroidEntryPoint
class NotaFragment : Fragment() {

    private var _b: FragmentNotaBinding? = null
    private val b get() = _b!!

    private val vm: NotaViewModel by viewModels()
    private lateinit var adapter: NotaItemsAdapter

    // cache snapshot barang yang sudah di-observe
    private var barangSnapshot: List<Barang> = emptyList()

    private val rupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("id", "ID"))

    private var selectedKonsumenId: Int? = null
    private var tanggal: Calendar = Calendar.getInstance()

    companion object {
        private const val KEY_KONSUMEN_ID = "kons_id"
        private const val KEY_TANGGAL = "tgl_ms"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let { ss ->
            if (ss.containsKey(KEY_KONSUMEN_ID)) selectedKonsumenId = ss.getInt(KEY_KONSUMEN_ID)
            if (ss.containsKey(KEY_TANGGAL)) tanggal.timeInMillis = ss.getLong(KEY_TANGGAL)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _b = FragmentNotaBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(v: View, s: Bundle?) {
        super.onViewCreated(v, s)

        // state awal tombol
        b.btnTambahItem.isEnabled = false
        b.btnSimpan.isEnabled = false

        // Tanggal & konsumen awal
        b.tvTanggal.text = sdf.format(tanggal.time)
        b.tvKonsumen.text = selectedKonsumenId?.let { _ -> b.tvKonsumen.text } ?: "Pilih konsumen…"

        // Picker tanggal
        b.tvTanggal.apply {
            isClickable = true; isFocusable = true
            setOnClickListener { showDatePicker() }
        }

        // Picker konsumen
        b.tvKonsumen.apply {
            isClickable = true; isFocusable = true
            setOnClickListener { showKonsumenPicker() }
        }

        // Recycler
        adapter = NotaItemsAdapter { index -> vm.removeAt(index) }
        b.rvItems.layoutManager = LinearLayoutManager(requireContext())
        b.rvItems.adapter = adapter

        // ==== Observers ====
        // Barang: isi snapshot + enable tombol tambah
        vm.barangList.observe(viewLifecycleOwner) { list ->
            barangSnapshot = list
            b.btnTambahItem.isEnabled = list.isNotEmpty()
        }

        // Items di nota
        vm.rows.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            b.btnSimpan.isEnabled = list.isNotEmpty()
        }

        // Subtotal
        vm.subtotal.observe(viewLifecycleOwner) { sub ->
            b.tvSubtotal.text = "Subtotal: ${rupiah.format(sub)}"
        }

        // Konsumen: auto pilih pertama jika belum ada
        vm.konsumenList.observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty() && selectedKonsumenId == null) {
                selectedKonsumenId = list.first().id
                b.tvKonsumen.text = list.first().nama
            }
        }

        // Tambah item
        b.btnTambahItem.setOnClickListener { showTambahItemDialog() }

        // Simpan nota
        b.btnSimpan.setOnClickListener { doSimpan() }
    }

    // ==== UI helpers ====

    private fun showDatePicker() {
        val y = tanggal.get(Calendar.YEAR)
        val m = tanggal.get(Calendar.MONTH)
        val d = tanggal.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(requireContext(), { _, yy, mm, dd ->
            tanggal.set(yy, mm, dd, 0, 0, 0)
            b.tvTanggal.text = sdf.format(tanggal.time)
        }, y, m, d).show()
    }

    private fun showKonsumenPicker() {
        val list = vm.konsumenList.value.orEmpty()
        if (list.isEmpty()) {
            Toast.makeText(requireContext(), "Data konsumen kosong", Toast.LENGTH_SHORT).show()
            return
        }
        val names = list.map { it.nama }.toTypedArray()
        var idx = 0
        AlertDialog.Builder(requireContext())
            .setTitle("Pilih Konsumen")
            .setSingleChoiceItems(names, 0) { _, which -> idx = which }
            .setPositiveButton("Pilih") { d, _ ->
                selectedKonsumenId = list[idx].id
                b.tvKonsumen.text = list[idx].nama
                d.dismiss()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showTambahItemDialog() {
        val list = barangSnapshot
        if (list.isEmpty()) {
            Toast.makeText(requireContext(), "Data barang kosong", Toast.LENGTH_SHORT).show()
            return
        }

        val names = list.map { it.nama }.toTypedArray()
        var idx = 0

        val inputQty = EditText(requireContext()).apply {
            hint = "Qty"
            // angka + desimal (boleh pecahan)
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            setSelectAllOnFocus(true)
        }
        val container = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 24, 50, 0)
            addView(inputQty)
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Tambah Item")
            .setSingleChoiceItems(names, 0) { _, which -> idx = which }
            .setView(container)
            .setPositiveButton("Tambah") { dlg, _ ->
                val q = inputQty.text.toString().toDoubleOrNull() ?: 0.0
                if (q <= 0.0) {
                    Toast.makeText(requireContext(), "Qty harus > 0", Toast.LENGTH_SHORT).show()
                } else {
                    val selectedBarang = list[idx]
                    vm.addRow(selectedBarang, q)
                    dlg.dismiss()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun doSimpan() {
        val konsId = selectedKonsumenId
        if (konsId == null) {
            Toast.makeText(requireContext(), "Pilih konsumen dulu", Toast.LENGTH_SHORT).show()
            return
        }
        if (vm.rows.value.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Tambahkan minimal 1 item", Toast.LENGTH_SHORT).show()
            return
        }

        val bon   = b.etBon.text.toString().toDoubleOrNull() ?: 0.0
        val retur = b.etRetur.text.toString().toDoubleOrNull() ?: 0.0
        val diskon= b.etDiskon.text.toString().toDoubleOrNull() ?: 0.0
        val bayar = b.etBayar.text.toString().toDoubleOrNull() ?: 0.0

        // cegah double click
        b.btnSimpan.isEnabled = false

        vm.simpan(
            tanggalMillis = tanggal.timeInMillis,
            konsumenId = konsId,
            nomor = b.etNomor.text?.toString(),
            bon = bon, retur = retur, diskon = diskon, bayar = bayar,
            onDone = { id ->
                Toast.makeText(requireContext(), "Nota tersimpan #$id", Toast.LENGTH_SHORT).show()
                // reset ringan
                b.etNomor.setText("")
                b.etBon.setText(""); b.etRetur.setText(""); b.etDiskon.setText(""); b.etBayar.setText("")
                b.btnSimpan.isEnabled = vm.rows.value?.isNotEmpty() == true
            },
            onError = { t ->
                Toast.makeText(requireContext(), t.message ?: "Gagal simpan", Toast.LENGTH_LONG).show()
                b.btnSimpan.isEnabled = vm.rows.value?.isNotEmpty() == true
            }
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        selectedKonsumenId?.let { outState.putInt(KEY_KONSUMEN_ID, it) }
        outState.putLong(KEY_TANGGAL, tanggal.timeInMillis)
    }

    override fun onDestroyView() {
        _b = null
        super.onDestroyView()
    }
}
