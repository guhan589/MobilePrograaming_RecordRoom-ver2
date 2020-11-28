package com.example.recordroom.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.recordroom.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class RatingBottomDialogFragment() : BottomSheetDialogFragment() {
    fun getInstance(): RatingBottomDialogFragment? {
        return RatingBottomDialogFragment()
    }

    private var msgLo: LinearLayout? = null
    private var emailLo: LinearLayout? = null
    private var cloudLo: LinearLayout? = null
    private var bluetoothLo: LinearLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View =
            inflater.inflate(R.layout.fragment_rating_bottom_dialog, container, false)

        return view
    }

    fun onClick(view: View) {
        when (view.id) {
        }
        dismiss()
    }

}