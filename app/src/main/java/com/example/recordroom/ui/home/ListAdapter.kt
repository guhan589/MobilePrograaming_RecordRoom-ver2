package com.example.recordroom.ui.home

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.recordroom.R
import com.example.recordroom.model.SharedUserData
import com.example.recordroom.ui.commom.RoomRecord
import com.google.firebase.firestore.FirebaseFirestore

class ListAdapter(context:Context, list:ArrayList<RoomRecord>, document:ArrayList<String>, activity:Activity) : BaseAdapter(){
    var db : FirebaseFirestore? = null
    val context = context
    val list = list
    val docu = document
    val homeActivity = activity
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view:View?=null
        var titleView:TextView?=null
        var addressView:TextView?=null
        var deleteBtn:Button?=null

        if(convertView == null){
             view = LayoutInflater.from(context).inflate(R.layout.item_list,null)
             titleView = view.findViewById<TextView>(R.id.listTextview) //제목
             addressView = view.findViewById<TextView>(R.id.addressTextview) //제목
             //deleteBtn = view.findViewById<Button>(R.id.deleteBtn) //삭제버튼
             //addBtn = view.findViewById<Button>(R.id.addBtn) //추가하기 버튼
        }



        val room = list[position]
        val userId = SharedUserData(context as Activity).getUser_id()
        titleView?.text = room.roomName
        addressView?.text = room.address
        db = FirebaseFirestore.getInstance()

        Log.d("TAG", "position: "+position)
        /*deleteBtn?.setOnClickListener{
            Log.d("ListAdapter", "deleteBtn: $position")
            Log.d("ListAdapter", "docu.get(position):" + docu.get(position))
            db!!.collection(userId!!).document(docu.get(position)).delete().addOnCompleteListener{
                if(it.isSuccessful)
                    Toast.makeText(context,"삭제 성공",Toast.LENGTH_SHORT).show()
            }


        }*/

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

    fun onDeleteClick(){

    }

}