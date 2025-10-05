package com.alung.notamu.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "konsumen", indices = [Index("nama")])
data class Konsumen(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var nama: String,
    var alamat: String? = null,
    var telp: String? = null,
    var saldo: Double = 0.0
)
