package com.alung.notamu.ui.konsumen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alung.notamu.data.entity.Konsumen
import com.alung.notamu.data.repo.KonsumenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KonsumenViewModel @Inject constructor(
    private val repo: KonsumenRepository
) : ViewModel() {
    val items = repo.all

    fun add(nama: String, alamat: String?, telp: String?) = viewModelScope.launch {
        repo.insert(Konsumen(nama = nama, alamat = alamat, telp = telp))
    }
    fun update(k: Konsumen) = viewModelScope.launch { repo.update(k) }
    fun delete(k: Konsumen) = viewModelScope.launch { repo.delete(k) }
}
