package com.example.e_hub
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.e_hub.departments.DepartmentsActivity
import kotlinx.coroutines.launch
import androidx.appcompat.app.AlertDialog
import java.io.File



class MainActivity : AppCompatActivity() {

    private var pendingApkFile: File? = null

    private val installPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (packageManager.canRequestPackageInstalls()) {
                pendingApkFile?.let { apkFile ->
                    ApkDownloader.installApk(this, apkFile)
                    pendingApkFile = null
                }
            }
        }

    override fun onCreate (savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ----- CHECK THE SERVER FOR A NEWER APP VERSION -----
        lifecycleScope.launch {
            try {
                val latestVersion = SupabaseClient.getLatestApkVersion()
                // --- COMPARE THE VERSION CODE FROM SUPABASE WITH THE INSTALLED APP VERSION.---
                val installedVersion = androidx.core.content.pm.PackageInfoCompat
                    .getLongVersionCode(packageManager.getPackageInfo(packageName,0))

                if (latestVersion.version_code>installedVersion){
                    // PROMPT THE USER WHEN A NEWER VERSION IS AVAILABLE.
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle("Nέα έκδοση διαθέσιμη")
                        .setMessage(
                            "Υπάρχει διαθέσιμη έκδοση ${latestVersion.version_name} του E-HUB.n\n"
                            + "${latestVersion.release_notes?:"Βελτιώσεις και διορθώσεις σφαλμάτων"}"
                        )
                        .setPositiveButton("Ενημέρωση") { _, _ ->
                            lifecycleScope.launch {
                                try {
                                    val apkUrl = SupabaseClient.getApkDownloadUrl()

                                    // ----- CHECK INSTALLATION PERMISSION AND CONTINUE THE UPDATE -----

                                    // CHECK IF E-HUB IS ALLOWED TO INSTALL APK FILES.
                                    if (packageManager.canRequestPackageInstalls()) {
                                        // PERMISSION ALREADY EXISTS, SO OPEN ANDROID'S INSTALLER.
                                        ApkDownloader.installApk(this@MainActivity, pendingApkFile!!)
                                    } else {
                                        // PERMISSION IS MISSING, SO PREPARE TO OPEN ANDROID SETTINGS.
                                        val settingsIntent = Intent(
                                            android.provider.Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES
                                        )
                                        // OPEN THE SETTINGS PAGE FOR E-HUB SPECIFICALLY.
                                        settingsIntent.data =
                                            android.net.Uri.parse("package:$packageName")
                                        // OPEN SETTINGS THROUGH OUR LAUNCHER.
                                        // THIS ALLOWS E-HUB TO CONTINUE WHEN THE EMPLOYEE RETURNS.
                                        installPermissionLauncher.launch(settingsIntent)
                                    }
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Η λήψη της ενημέρωσης ολοκληρώθηκε!", Toast.LENGTH_LONG)
                                        .show()
                                }catch (e: Exception){
                                    Toast.makeText(
                                        this@MainActivity,"Αποτυχία λήψης:${e.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                        .setNegativeButton("Αργότερα",null)
                        .show()
                } else {
                    // --- INFORM THE USER WHEN THE INSTALLED VERSION IS CURRENT.---
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



        // ----- OPEN THE EMPLOYEE REGISTRATION SCREEN -----
        val registerText = findViewById<TextView>(R.id.registerText)
        registerText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // ----- OPEN THE DEPARTMENT MENU FROM THE TEST BUTTON -----
        val testModeButton = findViewById<Button>(R.id.testModeButton)

        testModeButton.setOnClickListener {
            val intent = Intent(this, DepartmentsActivity::class.java)
            startActivity(intent)
        }
    }
}
