package com.gas.sisteminformasisj.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponse (
    val id_user: Int,
    val username: String,
    val password: String,
    val nama: String,
    val roles: Int,
    val islogin: Int
): Parcelable