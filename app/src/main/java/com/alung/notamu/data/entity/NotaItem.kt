package com.alung.notamu.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "nota_item",
    indices = [Index(value = ["notaId"]), Index(value = ["barangId"])]
)
data class NotaItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val notaId: Int,
    val barangId: Int,
    var qty: Double,
    var harga: Double,
    var jumlah: Double
)
