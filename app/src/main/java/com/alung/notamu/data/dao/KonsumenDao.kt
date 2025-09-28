package com.alung.notamu.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.alung.notamu.data.entity.Konsumen

@Dao
interface KonsumenDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(k: Konsumen): Long
    @Update suspend fun update(k: Konsumen)
    @Delete suspend fun delete(k: Konsumen)

    @Query("SELECT * FROM konsumen ORDER BY nama")
    fun getAll(): LiveData<List<Konsumen>>

    @Query("SELECT saldo FROM konsumen WHERE id = :id")
    suspend fun getSaldo(id: Int): Double?

    @Query("UPDATE konsumen SET saldo = :saldo WHERE id = :id")
    suspend fun setSaldo(id: Int, saldo: Double)
}