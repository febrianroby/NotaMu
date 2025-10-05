package com.alung.notamu.ui.konsumen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alung.notamu.data.entity.Konsumen
import com.alung.notamu.databinding.ItemKonsumenBinding
import java.text.NumberFormat
import java.util.Locale

class KonsumenAdapter(
    private val onClick: (Konsumen) -> Unit = {}
) : ListAdapter<Konsumen, KonsumenAdapter.VH>(DIFF) {

    private val rupiah = NumberFormat.getCurrencyInstance(Locale("id","ID"))

    object DIFF : DiffUtil.ItemCallback<Konsumen>() {
        override fun areItemsTheSame(o: Konsumen, n: Konsumen) = o.id == n.id
        override fun areContentsTheSame(o: Konsumen, n: Konsumen) = o == n
    }

    inner class VH(val b: ItemKonsumenBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inf = LayoutInflater.from(parent.context)
        return VH(ItemKonsumenBinding.inflate(inf, parent, false))
    }

    override fun onBindViewHolder(h: VH, position: Int) {
        val it = getItem(position)
        h.b.tvNo.text = (position + 1).toString()
        h.b.tvNama.text = it.nama
        h.b.tvAlamat.text = it.alamat.orEmpty()
        h.b.tvTelp.text = it.telp.orEmpty()
        h.b.tvSaldo.text = rupiah.format(it.saldo)
        h.b.root.setOnClickListener {
                 val pos = h.bindingAdapterPosition
                 if (pos != RecyclerView.NO_POSITION) onClick(getItem(pos))
        }
    }
}
