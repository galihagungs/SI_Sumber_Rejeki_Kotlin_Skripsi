package com.gas.sisteminformasisj.kasir

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class BarangModel (
    var id_barang: Int,
    var code: String,
    var nama_barang: String,
    var stok: Int,
    var harga: Int,
    var jumlah: Int
): Parcelable