package com.example.recordroom.UI

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.recordroom.R
import com.example.recordroom.function.Permission
import kotlinx.android.synthetic.main.item_list.view.*

class ListAdapter(context:Context, list:ArrayList<RoomRecord>) : BaseAdapter(){

    val context = context
    val list = list


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view:View?=null
        var titleView:TextView?=null
        var deleteBtn:Button?=null
        var addBtn:Button?=null
        if(convertView == null){
             view = LayoutInflater.from(context).inflate(R.layout.item_list,null)
             titleView = view.findViewById<TextView>(R.id.list_text)
             deleteBtn = view.findViewById<Button>(R.id.deleteBtn)
             addBtn = view.findViewById<Button>(R.id.addBtn)
        }


        if(position==0){
            view?.row_layout?.visibility = View.GONE
            view?.addBtn?.visibility = View.VISIBLE
        }
        val room = list[position]
        titleView?.text = room.title

        deleteBtn?.setOnClickListener{
            Toast.makeText(context,"리스트 삭제",Toast.LENGTH_SHORT).show()
        }
        addBtn?.setOnClickListener{

            Toast.makeText(context,"추가하기 ",Toast.LENGTH_SHORT).show()
            val intent = Intent(context,AddRoomActivity::class.java);
            context.startActivity(intent)
        }
        return view
    }

    override fun getItem(position: Int): Any {
        return list.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int { //getCount가 0일시 getView가 호출되지 않음
        return list.size
    }


}