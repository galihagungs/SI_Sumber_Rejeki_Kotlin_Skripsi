package com.gas.sisteminformasisj.kasir

class KerajangBarang (private var idbarang: Int, private var code: String, private var namaBarang: String, private var jumlah: Int, private var harga: Int) {
    // creating getter and setter methods.
    fun getidbarang(): Int{
        return idbarang
    }
    fun setidbarang(idBarang: Int){
        this.idbarang = idBarang
    }
    fun getuserCode(): String{
        return code
    }
    fun setuserCode(Code: String){
        this.code = Code
    }
    fun getnamaBarang(): String{
        return namaBarang
    }
    fun setnamaBarang(NamaBarang: String){
        this.namaBarang = NamaBarang
    }
    fun getStok(): Int{
        return jumlah
    }
    fun setStok(Jumlah: Int){
        this.jumlah = Jumlah
    }
    fun getHarga(): Int{
        return harga
    }
    fun setHarga(Harga: Int){
        this.harga = Harga
    }
}