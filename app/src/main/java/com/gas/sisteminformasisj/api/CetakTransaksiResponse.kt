package com.gas.sisteminformasisj.api

import com.google.gson.annotations.SerializedName

data class CetakTransaksiResponse(

	@field:SerializedName("values")
	val values: List<Transkasi>,

	@field:SerializedName("status")
	val status: Int
)

data class Transkasi(

	@field:SerializedName("id_transaksi")
	val idTransaksi: Int,

	@field:SerializedName("tanggal")
	val tanggal: String
)
