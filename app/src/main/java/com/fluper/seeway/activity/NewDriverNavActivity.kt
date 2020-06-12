package com.fluper.seeway.activity

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.fluper.seeway.R
import com.fluper.seeway.fragment.ChooseVehicleTypeFragment
import com.fluper.seeway.fragment.NotificationPassengerFragment
import com.google.android.material.navigation.NavigationView


class NewDriverNavActivity : AppCompatActivity() {
    private var nv: NavigationView? = null
    private var dl_driver: DrawerLayout? = null
      lateinit var img_seekbar: ImageView
    lateinit var img_seekbar_sec: ImageView
    private var t: ActionBarDrawerToggle? = null
    lateinit var txt_active_status :TextView
    val MULTIPLE_PERMISSIONS = 10 // code you want.
    private val STORAGE_PERMISSION_CODE = 23
    var permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_NOTIFICATION_POLICY,
        Manifest.permission.SEND_SMS,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_driver_nav)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        nv = findViewById<View>(R.id.nv_driver) as NavigationView
        dl_driver = findViewById<View>(R.id.dl_driver) as DrawerLayout
        txt_active_status = findViewById<View>(R.id.txt_active_status) as TextView



         img_seekbar = toolbar.findViewById<View>(R.id.img_seekbar) as ImageView
        img_seekbar.setOnClickListener {

            if (img_seekbar.getDrawable().getConstantState() == getResources().getDrawable( R.drawable.toggle_off_all).getConstantState())
            {
                img_seekbar.setImageResource(R.drawable.toggle_on3x)
                txt_active_status.setTextColor(Color.GREEN)
                txt_active_status.setText("You are Online")
            }
            else
            {
                img_seekbar.setImageResource(R.drawable.toggle_off_all)
                txt_active_status.setTextColor(Color.RED)
                txt_active_status.setText("You are Offline")
            }


        }


        t = ActionBarDrawerToggle(this,dl_driver,toolbar,0,0)

        dl_driver!!.addDrawerListener(t!!)
        t!!.syncState()

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.menu)
        supportActionBar!!.setDisplayShowTitleEnabled(false)


        nv!!.setNavigationItemSelectedListener(object :
            NavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                val id: Int = item.getItemId()
                when (id) {
                    R.id.nav_logout -> {
                        val i = Intent(this@NewDriverNavActivity,LoginActivity::class.java)
                        startActivity(i)
                    }

                    else -> return true
                }
                return true
            }
        })


        if (isReadStorageAllowed()) {

            return;
        } else {
            requestStoragePermission();
        }



    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.home_screen_nav_driver, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_driver_chosse_vehicle -> {
                setFragment(ChooseVehicleTypeFragment())
                super.onOptionsItemSelected(item)

            } R.id.action_driver_notification -> {
            setFragment(NotificationPassengerFragment())
            super.onOptionsItemSelected(item)

        }
            else -> super.onOptionsItemSelected(item)
        }


        return super.onOptionsItemSelected(item)
    }

    protected fun setFragment(fragment: Fragment?) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        if (fragment != null) {
            fragmentTransaction.add(android.R.id.content, fragment)
        }
        fragmentTransaction
            .addToBackStack(null).commit()
    }

    private fun isReadStorageAllowed(): Boolean {
        var result: Int
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        for (p in permissions) {
            result = ContextCompat.checkSelfPermission(this, p)
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p)
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(),
                MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }


    //Requesting permission
    private fun requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) &&
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) &&
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.SEND_SMS
            ) &&
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) &&
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_NOTIFICATION_POLICY
            ) &&
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_CONTACTS
            ) &&
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.RECORD_AUDIO
            )
        ) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_CODE
        )
    }

    //This method will be called when the user will tap on allow or deny
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissionsList: Array<String>,
        grantResults: IntArray
    ): Unit {
        when (requestCode) {
            MULTIPLE_PERMISSIONS -> {
                if (grantResults.size > 0) {
                    var permissionsDenied = ""
                    for (per in permissionsList) {
                        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                            permissionsDenied += """
                                
                                $per
                                """.trimIndent()
                        }
                    }

                }
                return
            }
        }
    }

}