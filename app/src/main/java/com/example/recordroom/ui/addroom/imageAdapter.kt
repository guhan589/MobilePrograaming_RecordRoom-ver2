package com.example.recordroom.ui.addroom

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.recordroom.R
import kotlinx.android.synthetic.main.roomimage.view.*

class imageAdapter(private val items: ArrayList<Bitmap>) : RecyclerView.Adapter<imageAdapter.ViewHolder>() {

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: imageAdapter.ViewHolder, position: Int) {
        val item = items[position]

        val listener = View.OnClickListener {
            it->
            Toast.makeText(it.context,"Clicked",Toast.LENGTH_SHORT).show()
        }
        holder.apply {
            bind(listener,item)
            itemView.tag = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): imageAdapter.ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.roomimage,parent,false)
        return imageAdapter.ViewHolder(inflatedView)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view:View=v
        fun bind(listener: View.OnClickListener, item: Bitmap) {
           view.roomimageView.setImageBitmap(item)
        }
    }
}