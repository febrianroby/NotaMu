package com.alung.notamu.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "nota",
    indices = [
        Index(value = ["konsumenId"]),
        Index(value = ["notaNo"], unique = true)
    ]
)
data class NotaHeader(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tanggal: Long = System.currentTimeMillis(),
    val konsumenId: Int,
    // Nullable supaya UNIQUE tidak bentrok kalau banyak yang kosong (NULL boleh duplikat di SQLite)
    val notaNo: String? = null,

    // Field opsional dengan default 0.0 → kamu bebas isi belakangan
    var bon: Long = 0L,
    var retur: Long = 0L,
    var diskon: Long = 0L,
    var totalBarang: Long = 0L,
    var totalAkhir: Long = 0L,
    var bayar: Long = 0L
)
