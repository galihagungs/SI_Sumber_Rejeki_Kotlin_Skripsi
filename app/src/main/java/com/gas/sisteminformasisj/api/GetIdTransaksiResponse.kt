package com.gas.sisteminformasisj.api

import com.google.gson.annotations.SerializedName

data class GetIdTransaksiResponse(

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("id_transaksi")
	val idTransaksi: Int
)
