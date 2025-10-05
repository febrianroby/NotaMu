package com.alung.notamu.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alung.notamu.data.dao.BarangDao
import com.alung.notamu.data.dao.KonsumenDao
import com.alung.notamu.data.dao.NotaDao
import com.alung.notamu.data.entity.Barang
import com.alung.notamu.data.entity.Konsumen
import com.alung.notamu.data.entity.NotaHeader
import com.alung.notamu.data.entity.NotaItem

@Database(
    entities = [Barang::class, Konsumen::class, NotaHeader::class, NotaItem::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun barangDao(): BarangDao
    abstract fun konsumenDao(): KonsumenDao
    abstract fun notaDao(): NotaDao
}
