package com.gas.sisteminformasisj.laporan.datajual

class DataCetakLaporan (private var idtransaksi: Int, private var tanggal: String) {
    // creating getter and setter methods.
    fun getidtransaksi(): Int{
        return idtransaksi
    }
    fun setidtransaksi(idTransaksi: Int){
        this.idtransaksi = idTransaksi
    }
    fun getTanggal(): String {
        return tanggal
    }
    fun setTanggal(Tanggal: String){
        this.tanggal = Tanggal
    }
}