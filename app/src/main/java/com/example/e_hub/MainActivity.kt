package com.example.e_hub
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.e_hub.departments.DepartmentsActivity
import kotlinx.coroutines.launch
import androidx.appcompat.app.AlertDialog
import java.io.File

private var pendingApkFile: File?=null//it remembers the downloaded APK while the user  visits android settings

class MainActivity : AppCompatActivity() {

    override fun onCreate (savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            try {
                val latestVersion = SupabaseClient.getLatestApkVersion()
                val installedVersion = androidx.core.content.pm.PackageInfoCompat
                    .getLongVersionCode(packageManager.getPackageInfo(packageName,0))
                
                if (latestVersion.version_code>installedVersion){
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle("Nέα έκδοση διαθέσιμη")
                        .setMessage(
                            "Υπάρχει διαθέσιμη έκδοση ${latestVersion.version_name} του E-HUB.n\n"
                            + "${latestVersion.release_notes?:"Βελτιώσεις και διορθώσεις σφαλμάτων"}"
                        )
                        .setPositiveButton("Ενημέρωση"){
                            dialog,which->// DONWLOAD ACTION
                        }
                        .setNegativeButton("Αργότερα",null)
                        .show()
                }else{
                    Toast.makeText(
                        this@MainActivity,
                        "To E-HUB ειναι στη τελευταία version",
                        Toast.LENGTH_LONG
                    ).show()
                }

                Toast.makeText(
                    this@MainActivity,
                    "Latest version: ${latestVersion.version_name}",
                    Toast.LENGTH_LONG
                ).show()

            } catch (e: Exception) {
                Toast.makeText(
                    this@MainActivity,
                    "Version check failed: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }



        // ----- OPEN EMPLOYEE REGISTRATION -----
        val registerText = findViewById<TextView>(R.id.registerText)
        registerText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // ----- OPEN DEPARTMENT MENU IN TEST MODE -----
        val testModeButton = findViewById<Button>(R.id.testModeButton)

        testModeButton.setOnClickListener {
            val intent = Intent(this, DepartmentsActivity::class.java)
            startActivity(intent)
        }
    }
}
