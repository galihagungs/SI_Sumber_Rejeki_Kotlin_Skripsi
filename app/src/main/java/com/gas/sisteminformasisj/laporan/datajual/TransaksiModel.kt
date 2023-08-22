package com.gas.sisteminformasisj.laporan.datajual

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransaksiModel (
    var id_penjualan: Int,
    var tanggal: String,
    var namaBarang: String,
    var total: Int
): Parcelable