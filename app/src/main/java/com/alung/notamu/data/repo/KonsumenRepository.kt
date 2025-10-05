package com.alung.notamu.data.repo

import com.alung.notamu.data.dao.KonsumenDao
import com.alung.notamu.data.entity.Konsumen
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KonsumenRepository @Inject constructor(
    private val dao: KonsumenDao
) {
    val all = dao.getAll()
    suspend fun insert(k: Konsumen) = dao.insert(k)
    suspend fun update(k: Konsumen) = dao.update(k)
    suspend fun delete(k: Konsumen) = dao.delete(k)
}
