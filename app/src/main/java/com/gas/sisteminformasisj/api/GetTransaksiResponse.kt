package com.gas.sisteminformasisj.api

import com.google.gson.annotations.SerializedName

data class GetTransaksiResponse(

	@field:SerializedName("values")
	val values: List<Transaksi>,

	@field:SerializedName("status")
	val status: Int
)

data class Transaksi(

	@field:SerializedName("id_transaksi")
	val idTransaksi: Int,

	@field:SerializedName("tanggal")
	val tanggal: String
)
