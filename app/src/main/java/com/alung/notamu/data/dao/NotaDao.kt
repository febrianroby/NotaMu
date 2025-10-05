package com.alung.notamu.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.alung.notamu.data.NotaWithItems            // relasi di .data
import com.alung.notamu.data.entity.NotaHeader       // entity di .data.entity
import com.alung.notamu.data.entity.NotaItem

@Dao
interface NotaDao {
    @Insert
    suspend fun insertHeader(n: NotaHeader): Long

    @Insert
    suspend fun insertItems(items: List<NotaItem>)

    @Transaction
    @Query("SELECT * FROM nota WHERE id = :id")
    suspend fun byIdWithItems(id: Int): NotaWithItems?

    // === Query khusus tampilan RecyclerView ===
    @Query("""
        SELECT ni.barangId,
               b.nama AS nama,
               ni.qty AS qty,
               ni.harga AS harga,
               CAST(ROUND(ni.qty * ni.harga) AS INTEGER) AS jumlah
        FROM nota_item ni
        JOIN barang b ON b.id = ni.barangId
        WHERE ni.notaId = :notaId
        ORDER BY b.nama
    """)
    fun getItemsUI(notaId: Int): kotlinx.coroutines.flow.Flow<List<NotaItemUI>>
}
