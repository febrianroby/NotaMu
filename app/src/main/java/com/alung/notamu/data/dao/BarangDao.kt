package com.alung.notamu.data.dao
import androidx.lifecycle.LiveData
import androidx.room.*
import com.alung.notamu.data.entity.Barang

@Dao
interface BarangDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(b: Barang): Long

    @Update
    suspend fun update(b: Barang)

    @Delete
    suspend fun delete(b: Barang)

    @Query("SELECT * FROM barang ORDER BY nama")
    fun getAll(): LiveData<List<Barang>>

    @Query("SELECT * FROM barang WHERE id = :id")
    suspend fun byId(id: Int): Barang?

    // Catatan: qty Int menyesuaikan stok Int di entity Barang
    @Query("UPDATE barang SET stok = stok - :qty WHERE id = :barangId AND stok >= :qty")
    suspend fun decreaseStock(barangId: Int, qty: Int): Int
}
