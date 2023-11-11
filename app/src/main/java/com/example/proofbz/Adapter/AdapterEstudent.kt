package com.example.proofbz.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proofbz.Models.Estudent
import com.example.proofbz.R

class AdapterEstudent (arrayList: ArrayList<Estudent>) :
    RecyclerView.Adapter<AdapterEstudent.ARViewHolder>(), View.OnClickListener  {
    var arrayList: ArrayList<Estudent>
    private var listener: View.OnClickListener? = null
    private var context: Context? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ARViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        v.setOnClickListener(this)
        val arv = ARViewHolder(v)
        context = parent.context
        return arv
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ARViewHolder, position: Int) {
        holder.nombre.text= arrayList[position].name+" "+arrayList[position].secondName
        holder.age.text= arrayList[position].age.toString()+" años"

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    fun setOnClickListener(listener: View.OnClickListener?) {
        this.listener = listener
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    fun filtros(lista: ArrayList<Estudent>) {
        println("actualiza datos")
        arrayList = lista
        notifyDataSetChanged()

    }

    override fun onClick(v: View) {
        if (listener != null) {
            listener!!.onClick(v)
        }
    }

    fun removeItem(position: Int) {
        arrayList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun restoreItem(item: Estudent, position: Int) {
        arrayList.add(position, item)
        notifyItemInserted(position)
    }

    fun getData(): ArrayList<Estudent>? {
        return arrayList
    }

    class ARViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nombre: TextView
        var age: TextView


        init {
            nombre = itemView.findViewById(R.id.textViewItemName)
            age = itemView.findViewById(R.id.textViewItemAge)

        }
    }

    init {
        this.arrayList = arrayList
    }
}
