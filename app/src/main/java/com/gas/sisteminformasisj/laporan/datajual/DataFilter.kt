package com.gas.sisteminformasisj.laporan.datajual

import java.util.*

class DataFilter(private var idtransaksi: Int, private var tanggaldiplay: String, private var tanggal: String) {
    // creating getter and setter methods.
    fun getidtransaksi(): Int{
        return idtransaksi
    }
    fun setidtransaksi(idTransaksi: Int){
        this.idtransaksi = idTransaksi
    }
    fun getTanggaldiplay(): String {
        return tanggaldiplay
    }
    fun setTanggaldis(Tanggaldis: String){
        this.tanggaldiplay = Tanggaldis
    }
    fun getTanggal(): String {
        return tanggal
    }
    fun setTanggal(Tanggal: String){
        this.tanggal = Tanggal
    }
}