package com.alung.notamu.ui.barang

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alung.notamu.data.entity.Barang
import com.alung.notamu.data.repo.BarangRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BarangViewModel @Inject constructor(
    private val repo: BarangRepository
) : ViewModel() {

    val items = repo.allBarang

    fun addBarang(nama: String, stok: Double, satuan: String, harga: Long) {
        viewModelScope.launch {
            repo.insert(Barang(nama = nama, stok = stok.toDouble(), satuan = satuan, harga = harga.toLong()))
        }
    }
    fun updateBarang(b: Barang) = viewModelScope.launch { repo.update(b) }
    fun deleteBarang(b: Barang) = viewModelScope.launch { repo.delete(b) }

}
