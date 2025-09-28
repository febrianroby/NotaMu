package com.alung.notamu.data

import androidx.room.Embedded
import androidx.room.Relation
import com.alung.notamu.data.entity.NotaHeader
import com.alung.notamu.data.entity.NotaItem

data class NotaWithItems(
    @Embedded val nota: NotaHeader,
    @Relation(
        parentColumn = "id",
        entityColumn = "notaId",
        entity = NotaItem::class
    )
    val items: List<NotaItem>
)
