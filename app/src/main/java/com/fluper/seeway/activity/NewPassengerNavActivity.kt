package com.fluper.seeway.activity

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fluper.seeway.R
import com.fluper.seeway.adapter.HomeAddressAdapter
import com.fluper.seeway.fragment.HomePassengerFragment
import com.fluper.seeway.fragment.NotificationPassengerFragment
import com.fluper.seeway.model.AddressModel
import com.google.android.material.navigation.NavigationView


class NewPassengerNavActivity : AppCompatActivity() {
    private var dl: DrawerLayout? = null
    private var t: ActionBarDrawerToggle? = null
    private var nv: NavigationView? = null
    private val STORAGE_PERMISSION_CODE = 23
    val MULTIPLE_PERMISSIONS = 10 // code you want.

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
        setContentView(R.layout.activity_new_passenger_nav)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        nv = findViewById<View>(R.id.nv) as NavigationView
        dl = findViewById<View>(R.id.nav_dra) as DrawerLayout

        t = ActionBarDrawerToggle(this,dl,toolbar,0,0)

        dl!!.addDrawerListener(t!!)
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
                      val i = Intent(this@NewPassengerNavActivity,LoginActivity::class.java)
                        startActivity(i)
                    }

                    else -> return true
                }
                return true
            }
        })
        initView()
        if (isReadStorageAllowed()) {

            return;
        } else {
            requestStoragePermission();
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.home_screen_nav_passenger, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_settings -> {
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

    private fun initView(){

        val recyclerView = findViewById(R.id.home_recycler) as RecyclerView


        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)


        val users = ArrayList<AddressModel>()


        users.add(AddressModel("Home", "383 joriseen st sunnyside Pretora", R.drawable.home_file))
        users.add(AddressModel("Work", "580 Paul grugre St. pretoria",R.drawable.work_file))
        users.add(AddressModel("Rosandra", "580 Paul grugre St. pretoria",R.drawable.star_file))




        var  adapter = HomeAddressAdapter(users, this)
        recyclerView.adapter = adapter
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