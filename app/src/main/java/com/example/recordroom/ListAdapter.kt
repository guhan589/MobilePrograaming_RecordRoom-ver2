package com.example.recordroom

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import androidx.core.content.res.ComplexColorCompat.inflate
import org.w3c.dom.Text

class ListAdapter(context:Context, list:ArrayList<RoomRecord>) : BaseAdapter(){

    val context = context
    val list = list


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list,null)

        val titleView = view.findViewById<TextView>(R.id.list_text)
        val deleteBtn = view.findViewById<Button>(R.id.deleteBtn)

        val room = list[position]
        titleView.text = room.title
        return view
    }

    override fun getItem(position: Int): Any {
        return list.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }


}