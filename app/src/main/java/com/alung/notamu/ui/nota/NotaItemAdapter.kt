package com.alung.notamu.ui.nota

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alung.notamu.databinding.ItemNotaRowBinding
import java.text.NumberFormat
import java.util.Locale

class NotaItemsAdapter(
    private val onDeleteAt: (Int) -> Unit
) : ListAdapter<NotaRowUi, NotaItemsAdapter.VH>(DIFF) {

    private val rupiah = NumberFormat.getCurrencyInstance(Locale("id","ID"))

    object DIFF : DiffUtil.ItemCallback<NotaRowUi>() {
        override fun areItemsTheSame(o: NotaRowUi, n: NotaRowUi) = o.barangId == n.barangId && o.nama == n.nama && o.harga == n.harga && o.qty == n.qty
        override fun areContentsTheSame(o: NotaRowUi, n: NotaRowUi) = o == n
    }

    inner class VH(val b: ItemNotaRowBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inf = LayoutInflater.from(parent.context)
        return VH(ItemNotaRowBinding.inflate(inf, parent, false))
    }

    override fun onBindViewHolder(h: VH, position: Int) {
        val row = getItem(position)
        h.b.tvQty.text = row.qty.toString()
        h.b.tvNama.text = row.nama
        h.b.tvHarga.text = rupiah.format(row.harga)
        h.b.tvJumlah.text = rupiah.format(row.jumlah)
        h.b.btnHapus.setOnClickListener { onDeleteAt(h.bindingAdapterPosition) }
    }
}
