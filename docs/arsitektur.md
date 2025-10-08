[200~# NotaMu – Arsitektur Data & Alur Transaksi (v1.1)

## Tujuan
- Mendukung penjualan harian dengan stok pecahan, piutang, retur, dan biaya operasional.
- Perhitungan laba akurat dengan snapshot **HPP** (harga beli saat jual) di `NotaItem`.

## Entitas Utama (Ringkas)
- **Barang**: `id, kode, nama, stok(Double), satuan, hargaBeli(Long), hargaJual(Long)`
- **Konsumen**: `id, nama, alamat?, telp?, saldo(Double/opsional)`
- **NotaHeader**: `id, tanggal, konsumenId, notaNo?, bon, retur, diskon, totalBarang, totalAkhir, bayar`
- **NotaItem** *(snapshot)*: `id, notaId, barangId, qty(Double), hargaJual(Long), hpp(Long)`

### Entitas Baru
- **ReturHeader**: `id, tanggal, konsumenId, refNotaId?, catatan?`
- **ReturItem**: `id, returId, notaItemId, barangId, qty(Double), hargaRefund(Long=hargaJual saat beli)`
- **Biaya**: `id, tanggal, kategori(enum), nominal(Long), keterangan?, notaId?, konsumenId?`

> Catatan: simpan uang dalam **Long (rupiah)**; qty boleh **Double**. Tambahkan `@TypeConverter` untuk enum `BiayaKategori`.

## Alur Transaksi

### Penjualan
1. Insert `NotaHeader` (sementara total=0).
2. Untuk tiap item:
   - Ambil `Barang.hargaBeli` → set `NotaItem.hpp`.
   - Set `NotaItem.hargaJual` = harga saat transaksi.
   - Insert `NotaItem`, kurangi `Barang.stok -= qty`.
3. Hitung `totalBarang = Σ(qty*hargaJual)`.
4. `totalAkhir = totalBarang - diskon - retur + bon`.
5. Jika `bayar < totalAkhir` → piutang bertambah (atau catat di ledger).
6. Update `NotaHeader` final.

### Retur
1. Insert `ReturHeader` (boleh `refNotaId`).
2. Untuk tiap item retur:
   - Ambil `NotaItem` asal → `hargaRefund = NotaItem.hargaJual`.
   - Insert `ReturItem`, `Barang.stok += qty`.
3. Hitung `nilaiRefund = Σ(qty*hargaRefund)`.
4. Kurangi tagihan/piutang konsumen sebesar `nilaiRefund` (atau kembalikan tunai).
5. (Opsional) Tambah ke `NotaHeader.retur` jika retur dikaitkan nota tertentu.

### Biaya Operasional
- Insert `Biaya(catat pengeluaran operasional per kategori & tanggal.)` kapan pun ada pengeluaran (bensin, parkir, makan, gaji, lainnya).

## Laporan (Query Inti)

**Laba kotor penjualan (tanpa retur/biaya):**
**Pengurang laba karena retur** = Σ((hargaJual − hpp) × qtyRetur).
**Laba bersih** = Laba kotor − Pengurang retur − Total biaya.
