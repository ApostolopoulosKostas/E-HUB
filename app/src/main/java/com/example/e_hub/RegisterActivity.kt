package com.example.e_hub


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity



class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // REGISTRATION LAYOUT SETUP IS CURRENTLY DISABLED.
        // setContentView(R.layout.activity_register)

        val employeeNameInput = findViewById<EditText>(R.id.employeeNameInput)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val registrationCodeInput = findViewById<EditText>(R.id.registrationCodeInput)
        val registerButton = findViewById<Button>(R.id.registerButton)

        // ----- VALIDATE REGISTRATION INPUTS -----
        registerButton.setOnClickListener {
            val employeeName = employeeNameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val registrationCode = registrationCodeInput.text.toString().trim()

            // SHOW AN ERROR AND STOP IF A REQUIRED FIELD IS EMPTY.
            if (employeeName.isBlank() || email.isBlank() || registrationCode.isBlank()) {
                Toast.makeText(this, "Συμπλήρωσε όλα τα πεδία", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }
            Toast.makeText(this, "Τα στοιχεία σας συμπληρώθηκαν", Toast.LENGTH_SHORT).show()

        }
    }
}