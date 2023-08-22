package com.gas.sisteminformasisj.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gas.sisteminformasisj.R

class ListAdapterUser(userModelArrayList: ArrayList<SearchUser>): RecyclerView.Adapter<ListAdapterUser.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback
    // creating a variable for array list and context.
    private var userModelArrayList: ArrayList<SearchUser>

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    // method for filtering our recyclerview items.
    fun filterList(filterList: ArrayList<SearchUser>) {
        // below line is to add our filtered
        // list in our course array list.
        userModelArrayList = filterList
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged()
    }

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var userName : TextView = itemView.findViewById(R.id.tv_item_Username)
        var namaUser : TextView = itemView.findViewById(R.id.tv_item_namaUser)
        var roleUser : TextView = itemView.findViewById(R.id.tv_item_roleUser)
        var EditUser : ImageButton = itemView.findViewById(R.id.imageButton_EditUser)
        var HapusUser : ImageButton = itemView.findViewById(R.id.imageButton_DeleteUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_user_admin,parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val userData  = userModelArrayList[position]
        holder.userName.text = userData.getuserName()
        holder.namaUser.text = userData.getNama()
        when (userData.getRole()){
            1 ->{
                holder.roleUser.text = "Admin"
            }
            2 ->{
                holder.roleUser.text = "Pengelola"
            }
            3->{
                holder.roleUser.text = "Karyawan"
            }

        }
        holder.EditUser.setOnClickListener {
            onItemClickCallback.onItemClicked(userModelArrayList[holder.adapterPosition])
        }
        holder.HapusUser.setOnClickListener {
            onItemClickCallback.onItemClicked2(userModelArrayList[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return userModelArrayList.size
    }

    interface OnItemClickCallback{
        fun onItemClicked(data: SearchUser)
        fun onItemClicked2(data: SearchUser)
    }

    init {
        this.userModelArrayList = userModelArrayList
    }
}