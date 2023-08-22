package com.gas.sisteminformasisj.laporan.datajual

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class DataPDFModel (
    var id_penjualan: Int,
    var id_transaksi: Int,
    var code: Int,
    var nama_barang: String,
    var harga: Int,
    var jumlah: Int
): Parcelable