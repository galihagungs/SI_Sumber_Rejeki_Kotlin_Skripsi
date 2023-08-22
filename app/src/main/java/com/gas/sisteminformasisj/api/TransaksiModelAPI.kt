package com.gas.sisteminformasisj.api

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class TransaksiModelAPI(
    var id_transaksi: Int,
    var date: String,
): Parcelable