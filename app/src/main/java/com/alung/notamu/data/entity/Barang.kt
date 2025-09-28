package com.alung.notamu.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(tableName = "barang", indices = [Index(value = ["nama"])])
data class Barang(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var nama: String,
    var stok: Int,
    var satuan: String,
    var harga: Double
)