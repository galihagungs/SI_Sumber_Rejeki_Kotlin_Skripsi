package com.gas.sisteminformasisj.kasir

import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.gas.sisteminformasisj.R
import com.gas.sisteminformasisj.viewmodel.CartViewModel

class ListAdapterBarang (barangModelArrayList: ArrayList<SearchBarang>): RecyclerView.Adapter<ListAdapterBarang.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    private var barangModelArrayList: ArrayList<SearchBarang>

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    // method for filtering our recyclerview items.
    fun filterList(filterList: ArrayList<SearchBarang>) {
        // below line is to add our filtered
        // list in our course array list.
        barangModelArrayList = filterList
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged()
    }

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var namaBarang: TextView = itemView.findViewById(R.id.namaBarang)
        var stokBarang: TextView = itemView.findViewById(R.id.jumlahStok)
        var hargaBarang: TextView = itemView.findViewById(R.id.hargaBarangRP)
        var warning: TextView = itemView.findViewById(R.id.warning)
        var QTY: TextView = itemView.findViewById(R.id.qty)
        var btnTambah: ImageButton = itemView.findViewById(R.id.tambahQTY)
        var btnKurang: ImageButton = itemView.findViewById(R.id.kurangQTY)
        var totalHarga: TextView = itemView.findViewById(R.id.hargaTotal)
        var btnDel: ImageButton= itemView.findViewById(R.id.deleteItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_barang_kasir_tambah,parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val barang = barangModelArrayList[position]
        var qty = 0
        var pos = position
        holder.namaBarang.text = barang.getnamaBarang()
        holder.stokBarang.text = "Tersedia: ${barang.getStok().toString()}"
        holder.hargaBarang.text = "Rp. ${barang.getHarga()}"
        holder.totalHarga.text = "Rp.${barang.getHarga()}"
        holder.QTY.text= qty.toString()
        holder.btnTambah.setOnClickListener {
            if ( qty < barang.getStok()){
                qty++
                holder.QTY.text = qty.toString()
                var jumlah = qty*barang.getHarga()

                holder.totalHarga.text = "Rp. $jumlah"

                if (qty == 0){
                    holder.warning.text =" Masukan Jumlah Barang"
                }else{
                    holder.warning.visibility = View.INVISIBLE;
                    onItemClickCallback.onItemClicked(qty,pos,barang.getCode())
                }
            }
        }
        holder.btnKurang.setOnClickListener() {
            if ( qty != 1){
                qty--
                holder.QTY.text = qty.toString()
                var jumlah = qty*barang.getHarga()
                holder.totalHarga.text = "Rp. $jumlah"
//                barangModelArrayList.find { it.getCode() == barang.getCode()}?.setJumlah(qty)
                onItemClickCallback.onItemClicked(qty,pos,barang.getCode())
                if (qty == 0){
                    holder.warning.text =" Masukan Jumlah Barang"
                }else{
                    holder.warning.visibility = View.INVISIBLE;
                    onItemClickCallback.onItemClicked(qty,pos,barang.getCode())
                }
            }
        }
        holder.btnDel.setOnClickListener{
            removeItem(position)
        }

    }
    private fun removeItem(position: Int) {
        barangModelArrayList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return barangModelArrayList.size
    }

    interface OnItemClickCallback{
        fun onItemClicked(Jumlah: Int, Pos: Int, code: String)
    }
    init {
        this.barangModelArrayList = barangModelArrayList
    }
}