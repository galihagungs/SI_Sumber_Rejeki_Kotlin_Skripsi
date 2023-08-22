package com.gas.sisteminformasisj.api

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserModelApi (
    var id_user: Int,
    var username: String,
    var password: String,
    var nama: String,
    var roles: Int,
    var isLogin: Int
):Parcelable