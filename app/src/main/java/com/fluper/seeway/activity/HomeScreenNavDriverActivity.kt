package com.fluper.seeway.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.fluper.seeway.R
import com.fluper.seeway.fragment.ChooseVehicleTypeFragment
import com.fluper.seeway.fragment.NotificationPassengerFragment
import com.google.android.material.navigation.NavigationView

class HomeScreenNavDriverActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{

    lateinit var navView : NavigationView
    lateinit var drawerLayout : DrawerLayout
    private val STORAGE_PERMISSION_CODE = 23
    val MULTIPLE_PERMISSIONS = 10 // code you want.
    private lateinit var appBarConfiguration: AppBarConfiguration
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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen_driver)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val toolbar_seekbar: SeekBar = findViewById(R.id.toolbar_seekbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.menu)
        getSupportActionBar()?.setDisplayShowTitleEnabled(false)


        window.setNavigationBarColor(Color.TRANSPARENT)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)


        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)



        if(isReadStorageAllowed()){

            return;
        }else{
            requestStoragePermission();
        }

        navView.setNavigationItemSelectedListener(this)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_logout -> {
                val i = Intent(this,LoginActivity::class.java)
                startActivity(i)
            }

        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.home_screen_nav_driver, menu)

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_driver_settings -> {
                setFragment(NotificationPassengerFragment())
                super.onOptionsItemSelected(item)

            } R.id.action_driver_chosse_vehicle -> {
            setFragment(ChooseVehicleTypeFragment())
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
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)&&
            ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)&&
            ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.SEND_SMS)&&
            ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)&&
            ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_NOTIFICATION_POLICY)&&
            ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS)&&
            ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.RECORD_AUDIO)) {
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