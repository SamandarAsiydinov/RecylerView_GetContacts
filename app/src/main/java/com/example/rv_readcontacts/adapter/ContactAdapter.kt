package com.example.rv_readcontacts.adapter

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.rv_readcontacts.R
import com.example.rv_readcontacts.model.Contact

class ContactAdapter(val context: Context, val list: List<Contact>) :
    RecyclerView.Adapter<ContactAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_list, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.tvName.text = list[position].name
        holder.tvNumber.text = list[position].number
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class CustomViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvName: TextView = v.findViewById(R.id.tv_name)
        val tvNumber: TextView = v.findViewById(R.id.tv_phone_number)
    }
}