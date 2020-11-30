package com.example.recordroom.UI

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast

import com.example.recordroom.R
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.bottomsheet_dialog.*


class RatingBottomDialogFragment() : BottomSheetDialogFragment() {
    fun getInstance(): RatingBottomDialogFragment? {
        return RatingBottomDialogFragment()
    }

    private var msgLo: LinearLayout? = null
    private var emailLo: LinearLayout? = null
    private var cloudLo: LinearLayout? = null
    private var bluetoothLo: LinearLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.bottomsheet_dialog, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addRoomBtn.setOnClickListener{
            Toast.makeText(context,"gggggggggg",Toast.LENGTH_SHORT).show()
            Log.d("context", "gggggg: ")


        }
    }


    fun make(){
        val db = FirebaseFirestore.getInstance()
        val user: MutableMap<String, Any> =
            HashMap()
        user["first"] = "Ada"
        user["last"] = "Lovelace"
        user["born"] = 1815
        db.collection("user_id").add(user)
            .addOnSuccessListener(OnSuccessListener<DocumentReference> { documentReference ->
                Log.d(
                    "TAG",
                    "onSuccess: " + documentReference.id
                )
            })
            .addOnFailureListener(OnFailureListener {

            })
    }
   /* override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val d = dialog as BottomSheetDialog
        val bottomSheetInternal =
            d.findViewById<View>(R.id.design_bottom_sheet)
        bottomSheetInternal?.minimumHeight=
            Resources.getSystem().getDisplayMetrics().heightPixels
        super.onViewCreated(view, savedInstanceState)
    }*/

    fun onClick(view: View) {
        when (view.id) {
        }
        dismiss()
    }

}