package com.alung.notamu.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "nota",
    indices = [Index(value = ["konsumenId"]), Index(value = ["notaNo"], unique = true)]
)
data class NotaHeader(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tanggal: Long,
    val konsumenId: Int,
    val notaNo: String,
    var bon: Double = 0.0,
    var retur: Double = 0.0,
    var diskon: Double = 0.0,
    var totalBarang: Double = 0.0,
    var totalAkhir: Double = 0.0,
    var bayar: Double = 0.0
)
