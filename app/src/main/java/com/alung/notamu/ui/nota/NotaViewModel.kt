package com.alung.notamu.ui.nota

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.alung.notamu.data.entity.Barang
import com.alung.notamu.data.repo.BarangRepository
import com.alung.notamu.data.repo.KonsumenRepository
import com.alung.notamu.data.repo.NotaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotaRowUi(
    val barangId: Int,
    val nama: String,
    val harga: Long,  // <- Double (menyesuaikan Barang.harga)
    val qty: Double     // <- Double (bisa pecahan)
) {
    val jumlah: Double get() = harga.toDouble() * qty
}

@HiltViewModel
class NotaViewModel @Inject constructor(
    private val notaRepo: NotaRepository,
    barangRepo: BarangRepository,
    konsumenRepo: KonsumenRepository
) : ViewModel() {

    val barangList = barangRepo.allBarang         // LiveData<List<Barang>>
    val konsumenList = konsumenRepo.all           // LiveData<List<Konsumen>>

    private val _rows = MutableLiveData<List<NotaRowUi>>(emptyList())
    val rows: LiveData<List<NotaRowUi>> = _rows

    val subtotal: LiveData<Double> = _rows.map { list ->
        list.sumOf { it.jumlah }
    }

    fun addRow(b: Barang, qty: Double) {
        val cur = _rows.value.orEmpty().toMutableList()
        cur.add(NotaRowUi(b.id, b.nama, b.harga, qty))
        _rows.value = cur
    }
    fun addRow(b: Barang, qty: Int) = addRow(b, qty.toDouble())

    fun removeAt(index: Int) {
        val cur = _rows.value.orEmpty().toMutableList()
        if (index in cur.indices) {
            cur.removeAt(index)
            _rows.value = cur
        }
    }

    fun simpan(
        tanggalMillis: Long,
        konsumenId: Int,
        nomor: String?,
        bon: Double,
        retur: Double,
        diskon: Double,
        bayar: Double,
        onDone: (Long) -> Unit,
        onError: (Throwable) -> Unit
    ) = viewModelScope.launch {
        try {
            // items: List<Pair<Int, Double>>
            val items = rows.value.orEmpty().map { it.barangId to it.qty }
            val id = notaRepo.simpanNota(
                tanggal = tanggalMillis,
                konsumenId = konsumenId,
                nomor = nomor,
                bon = bon,
                retur = retur,
                diskon = diskon,
                bayar = bayar,
                items = items
            )
            onDone(id)
        } catch (t: Throwable) {
            onError(t)
        }
    }
}
