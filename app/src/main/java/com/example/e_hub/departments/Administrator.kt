package com.example.e_hub.departments

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.e_hub.R
import okio.Buffer

class Administrator  : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.administrator)

        val inviteEmployee = findViewById<Button>(R.id.inviteEmployee)

        inviteEmployee.setOnClickListener {

        }




    }


}