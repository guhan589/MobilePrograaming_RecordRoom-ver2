package com.example.recordroom.Messagepop

import android.app.Activity

class Loadactivty {
    lateinit var activity:Activity
    constructor(activity:Activity){
        if(activity==null)
            this.activity = activity
    }


}