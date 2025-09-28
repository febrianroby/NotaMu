package com.alung.notamu.data.repo

import androidx.room.withTransaction
import com.alung.notamu.data.AppDatabase
import com.alung.notamu.data.entity.Barang
import com.alung.notamu.data.entity.NotaHeader
import com.alung.notamu.data.entity.NotaItem
import com.alung.notamu.data.dao.BarangDao
import com.alung.notamu.data.dao.NotaDao
import com.alung.notamu.data.dao.KonsumenDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotaRepository @Inject constructor(
    private val db: AppDatabase,
    private val notaDao: NotaDao,
    private val barangDao: BarangDao,
    private val konsumenDao: `KonsumenDao`
) {
    /**
     * Simpan nota:
     * - hitung total
     * - insert header & items
     * - kurangi stok per barang
     * - update saldo konsumen (bon)
     */
    suspend fun createNota(
        tanggalMillis: Long,
        konsumenId: Int,
        notaNo: String,
        retur: Double,
        diskon: Double,
        bayar: Double,
        itemsInput: List<Pair<Barang, Double>> // (barang, qty)
    ): Long = db.withTransaction {
        val bon = konsumenDao.getSaldo(konsumenId) ?: 0.0

        val mapped = itemsInput.map { (barang, qty) ->
            val jumlah = qty * barang.harga
            Triple(barang, qty, jumlah)
        }
        val totalBarang = mapped.sumOf { it.third }
        val totalAkhir = bon + totalBarang - retur - diskon

        val header = NotaHeader(
            tanggal = tanggalMillis,
            konsumenId = konsumenId,
            notaNo = notaNo,
            bon = bon,
            retur = retur,
            diskon = diskon,
            totalBarang = totalBarang,
            totalAkhir = totalAkhir,
            bayar = bayar
        )
        val notaId = notaDao.insertHeader(header).toInt()

        // Kurangi stok dan buat item
        val items = mapped.map { (barang, qty, jumlah) ->
            val ok = barangDao.decreaseStock(barang.id, qty.toInt())
            if (ok == 0) error("Stok tidak cukup: ${barang.nama}")
            NotaItem(
                notaId = notaId,
                barangId = barang.id,
                qty = qty,
                harga = barang.harga,
                jumlah = jumlah
            )
        }
        notaDao.insertItems(items)

        // Update saldo konsumen (sisa piutang)
        val sisa = totalAkhir - bayar
        konsumenDao.setSaldo(konsumenId, sisa)

        notaId.toLong()
    }
}
