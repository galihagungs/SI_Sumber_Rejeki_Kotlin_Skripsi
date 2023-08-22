package com.gas.sisteminformasisj.api

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class BarangModelAPI (
    var id_barang: Int,
    var code: String,
    var namabarang: String,
    var stok: Int,
    var harga: Int
): Parcelable