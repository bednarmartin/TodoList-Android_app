package com.example.vma_du1_todolist

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        val message = p1?.getStringExtra("MESSAGE")
        Toast.makeText(p0, message, Toast.LENGTH_LONG).show()



    }
}
