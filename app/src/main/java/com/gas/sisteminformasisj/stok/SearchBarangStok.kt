package com.gas.sisteminformasisj.stok

class SearchBarangStok (private var idBarang: Int, private var code: String, private var namaBarang: String, private var Stok: Int, private var Harga: Int) {
    // creating getter and setter methods.
    fun getidBarang(): Int{
        return idBarang
    }
    fun setidUser(idbarang: Int){
        this.idBarang = idbarang
    }
    fun getCode(): String{
        return code
    }
    fun setCode(Code: String){
        this.code = Code
    }
    fun getnamaBarang(): String{
        return namaBarang
    }
    fun setnamaBarang(namabarang: String){
        this.namaBarang = namabarang
    }
    fun getStok(): Int{
        return Stok
    }
    fun setStok(stok: Int){
        this.Stok = stok
    }
    fun getHarga(): Int{
        return Harga
    }
    fun setHarga(harga: Int){
        this.Harga = harga
    }
}