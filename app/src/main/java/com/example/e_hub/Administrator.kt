package com.example.e_hub

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Administrator  : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.administrator)

        val inviteEmployee = findViewById<Button>(R.id.inviteEmployee)

        inviteEmployee.setOnClickListener {

        }




    }


}