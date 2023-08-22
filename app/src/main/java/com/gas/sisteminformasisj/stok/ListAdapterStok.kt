package com.gas.sisteminformasisj.stok

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gas.sisteminformasisj.R

class ListAdapterStok(barangModelArrayList: ArrayList<SearchBarangStok>): RecyclerView.Adapter<ListAdapterStok.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    private var barangModelArrayList: ArrayList<SearchBarangStok>

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun filterList(filterList: ArrayList<SearchBarangStok>) {
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
        var editBarang: ImageButton = itemView.findViewById(R.id.imageButton_EditBarang)
        var hapusBarang: ImageButton = itemView.findViewById(R.id.imageButton_DeleteBarang)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_barang_kasir,parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = barangModelArrayList[position]
        holder.namaBarang.text = data.getnamaBarang()
        holder.stokBarang.text = data.getStok().toString()
        holder.hargaBarang.text = "Rp. ${data.getHarga()}"
        holder.editBarang.setOnClickListener {
            onItemClickCallback.onItemClicked(barangModelArrayList[holder.adapterPosition])
        }
        holder.hapusBarang.setOnClickListener {
            onItemClickCallback.onItemClicked2(data.getidBarang())
        }

    }

    override fun getItemCount(): Int {
        return barangModelArrayList.size
    }

    interface OnItemClickCallback{
        fun onItemClicked(data: SearchBarangStok)
        fun onItemClicked2(data: Int)
    }

    init {
        this.barangModelArrayList = barangModelArrayList
    }
}