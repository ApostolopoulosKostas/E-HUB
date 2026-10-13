package com.example.e_hub.departments
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.e_hub.ProductsActivity
import com.example.e_hub.R

class DepartmentsActivity : AppCompatActivity(){

    override fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.departments)

            // ------------- BUTTON FOR THE DEPARTMENTS------------------
        val customerSupportButton = findViewById<Button>(R.id.customerSupportButton)
        customerSupportButton.setOnClickListener {
            val intent = Intent(this, CustomerSupportActivity::class.java)
            startActivity(intent)
        }
        val warehouseButton =findViewById<Button>(R.id.warehouseButton)
        warehouseButton.setOnClickListener {
            val intent = Intent(this, WarehouseActivity::class.java)
            startActivity(intent)
        }
        val groceryButton =findViewById<Button>(R.id.groceryButton)
        groceryButton.setOnClickListener {
            val intent=Intent(this, GroceryActivity::class.java)
            startActivity(intent)
        }
        val fridgeButton =findViewById<Button>(R.id.fridgeButton)
        fridgeButton.setOnClickListener {
            val intent=Intent(this, FridgeActivity::class.java)
            startActivity(intent)
        }
        val productsButton = findViewById<Button>(R.id.productsButton)
        productsButton.setOnClickListener {
            val intent=Intent(this, ProductsActivity::class.java)
            startActivity(intent)
        }

    }
}