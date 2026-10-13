package com.example.e_hub
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.e_hub.departments.DepartmentsActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate (savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val registerText = findViewById<TextView>(R.id.registerText)
        registerText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // ----------------TEST    BUTTON---------------------------------------------------
        val testModeButton = findViewById<Button>(R.id.testModeButton)

        testModeButton.setOnClickListener {
            val intent = Intent(this, DepartmentsActivity::class.java)
            startActivity(intent)
        //-----------------------------------------------------------------------------------
        }
    }
}
