package com.alung.notamu.ui.barang.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alung.notamu.data.entity.Barang
import com.alung.notamu.databinding.ItemBarangBinding

class BarangAdapter(
    private val onClick: (Barang) -> Unit = {}
) : ListAdapter<Barang, BarangAdapter.VH>(DIFF) {

    private val rupiah = java.text.NumberFormat.getCurrencyInstance(java.util.Locale("id", "ID"))

    object DIFF : DiffUtil.ItemCallback<Barang>() {
        override fun areItemsTheSame(o: Barang, n: Barang) = o.id == n.id
        override fun areContentsTheSame(o: Barang, n: Barang) = o == n
    }

    inner class VH(val b: ItemBarangBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inf = LayoutInflater.from(parent.context)
        return VH(ItemBarangBinding.inflate(inf, parent, false))
    }

    override fun onBindViewHolder(h: VH, position: Int) {
        val item = getItem(position)
        h.b.tvNo.text     = (position + 1).toString()
        h.b.tvNama.text   = item.nama
        h.b.tvHarga.text  = rupiah.format(item.harga)
        h.b.tvStok.text   = item.stok.toString()
        h.b.tvSatuan.text = item.satuan
        h.b.root.setOnClickListener { onClick(item) }
    }
}
