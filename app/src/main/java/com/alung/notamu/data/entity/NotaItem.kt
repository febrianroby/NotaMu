package com.alung.notamu.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "nota_item",
    foreignKeys = [
        ForeignKey(
            entity = NotaHeader::class,
            parentColumns = ["id"],
            childColumns = ["notaId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Barang::class,
            parentColumns = ["id"],
            childColumns = ["barangId"],
            onDelete = ForeignKey.RESTRICT // barang tak boleh hilang kalau masih dipakai nota
        )
    ],
    indices = [
        Index(value = ["notaId"]),
        Index(value = ["barangId"]),
        Index(value = ["notaId","barangId"], unique = true) // penting!
    ]
)
data class NotaItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val notaId: Int,
    val barangId: Int,
    var qty: Double,     // bisa pecahan (Kg)
    var harga: Long      // rupiah per satuan (snapshot saat transaksi)
    // jumlah tidak disimpan — dihitung di query: qty * harga
)
