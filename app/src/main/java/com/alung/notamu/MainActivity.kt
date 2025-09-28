// app/src/main/java/com/alung/notamu/MainActivity.kt
package com.alung.notamu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alung.notamu.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.alung.notamu.ui.nota.NotaFragment
import com.alung.notamu.ui.barang.BarangListFragment
import com.alung.notamu.ui.konsumen.DataKonsumenFragment
import com.alung.notamu.ui.laporan.LaporanFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 4
            override fun createFragment(p: Int): Fragment = when (p) {
                0 -> NotaFragment()
                1 -> BarangListFragment()
                2 -> DataKonsumenFragment()
                else -> LaporanFragment()
            }
        }
        binding.pager.adapter = adapter
        TabLayoutMediator(binding.tabs, binding.pager) { tab, pos ->
            tab.text = listOf("Nota", "Barang", "Konsumen", "Laporan")[pos]
        }.attach()
    }
}
