package com.gas.sisteminformasisj.admin

class SearchUser(private var iduser: Int, private var userName: String, private var naMa: String, private var Role: Int) {
    // creating getter and setter methods.
    fun getidUser(): Int{
        return iduser
    }
    fun setidUser(idUser: Int){
        this.iduser = idUser
    }
    fun getuserName(): String{
        return userName
    }
    fun setuserName(userName: String){
        this.userName = userName
    }
    fun getNama(): String{
        return naMa
    }
    fun setNama(Nama: String){
        this.naMa = Nama
    }
    fun getRole(): Int{
        return Role
    }
    fun setRole(RoLe: Int){
        this.Role = RoLe
    }
}

