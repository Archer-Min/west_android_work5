package com.example.westwork5.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.westwork5.R
import android.widget.LinearLayout

import android.content.Context


class MyAdapter(private val context: Context, private val list: List<String>) :
    RecyclerView.Adapter<MyAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tvContent.text = list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvContent: TextView = itemView.findViewById(R.id.tvContent)
        val tvDelete: TextView = itemView.findViewById(R.id.tvDelete)
        val tvTop: TextView = itemView.findViewById(R.id.tvTop)
        val llLayout: LinearLayout = itemView.findViewById(R.id.llLayout)
    }
}


