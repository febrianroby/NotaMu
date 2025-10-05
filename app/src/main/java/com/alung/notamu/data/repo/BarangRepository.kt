package com.alung.notamu.data.repo


import com.alung.notamu.data.entity.Barang
import com.alung.notamu.data.dao.BarangDao
import javax.inject.Inject



class BarangRepository @Inject constructor(
    private val dao: BarangDao
) {
    val allBarang = dao.getAll()
    suspend fun insert(b: Barang) = dao.insert(b)
    suspend fun update(b: Barang) = dao.update(b)
    suspend fun delete(b: Barang) = dao.delete(b)
}
