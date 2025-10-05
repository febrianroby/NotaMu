package com.alung.notamu.data.repo

import androidx.room.withTransaction
import com.alung.notamu.data.AppDatabase
import com.alung.notamu.data.dao.BarangDao
import com.alung.notamu.data.dao.KonsumenDao
import com.alung.notamu.data.dao.NotaDao
import com.alung.notamu.data.entity.NotaHeader
import com.alung.notamu.data.entity.NotaItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotaRepository @Inject constructor(
    private val db: AppDatabase,
    private val notaDao: NotaDao,
    private val barangDao: BarangDao,
    private val konsumenDao: KonsumenDao
) {

    /**
     * @param items list pasangan (barangId to qtyDouble)
     * @return notaId
     */
    suspend fun simpanNota(
        tanggal: Long,
        konsumenId: Int,
        nomor: String?,
        bon: Double,
        retur: Double,
        diskon: Double,
        bayar: Double,
        items: List<Pair<Int, Double>>
    ): Long = db.withTransaction {
        var subtotal = 0.0

        // 1) Header
        val notaId = notaDao.insertHeader(
            NotaHeader(
                tanggal = tanggal,
                konsumenId = konsumenId,
                notaNo = nomor
            )
        )

        // 2) Detail
        val rows: List<NotaItem> = items.map { (barangId, qty) ->
            val b = barangDao.byId(barangId) ?: error("Barang $barangId tidak ditemukan")

            // BarangDao.decreaseStock(..) versi kamu: qty:Int -> cast dulu
            val updated = barangDao.decreaseStock(barangId, qty)
            if (updated == 0) error("Stok '${b.nama}' tidak cukup")

            val jumlah = b.harga * qty
            subtotal += jumlah

            NotaItem(
                notaId = notaId.toInt(),
                barangId = barangId,
                qty = qty,        // Double
                harga = b.harga   // Double
                // tidak ada field 'jumlah' di entity -> jangan kirim
            )
        }
        notaDao.insertItems(rows)

        // 3) Hitung total & saldo konsumen
        val total = subtotal + bon - retur - diskon
        val saldoAwal = konsumenDao.getSaldo(konsumenId) ?: 0.0
        konsumenDao.setSaldo(konsumenId, saldoAwal + total - bayar)

        notaId
    }
}
