package com.gas.sisteminformasisj.kasir

class TambahBarang (private var idbarang: Int, private var code: String, private var namaBarang: String, private var stok: Int, private var harga: Int, private var jumlah: Int) {
    // creating getter and setter methods.
    fun getidbarang(): Int{
        return idbarang
    }
    fun setidbarang(idBarang: Int){
        this.idbarang = idBarang
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
    fun setnamaBarang(NamaBarang: String){
        this.namaBarang = NamaBarang
    }
    fun getStok(): Int{
        return stok
    }
    fun setStok(Stok: Int){
        this.stok = Stok
    }
    fun getHarga(): Int{
        return harga
    }
    fun setHarga(Harga: Int){
        this.harga = Harga
    }
    fun getJumlah(): Int{
        return jumlah
    }
    fun setJumlah(Jumlah: Int){
        this.jumlah = Jumlah
    }
}