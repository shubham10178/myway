package com.fluper.seeway.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.fluper.seeway.R
import com.fluper.seeway.fragment.ChosseSecurityFragment
import com.rilixtech.CountryCodePicker
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard
import kotlinx.android.synthetic.main.activity_profile_creation_passenger.*
import java.text.SimpleDateFormat
import java.util.*

class ProfileCreationPassengerActivity : AppCompatActivity(),View.OnClickListener {
    private val REQUEST_AUTOTEST: Any? = 1
    private val REQUEST_SCAN: Int = 100
    private var yeaR: Int = 0
    private var month: Int = 0
    private var day: Int = 0
    private lateinit var cal: Calendar
    private val IMAGE_PICK_CODE = 1000;
    private val PERMISSION_CODE1 = 1000;
    private val IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null
    private val PERMISSION_CODE = 1001;
    var dateSelected: Calendar = Calendar.getInstance()
    private var datePickerDialog: DatePickerDialog? = null


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_creation_passenger)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        etCardDate.isEnabled = false

        val type = Typeface.createFromAsset(getAssets(),"font/avenir_black.ttf");
        (ccp as CountryCodePicker).setTypeFace(type)

        iv_business_dropdown.setOnClickListener(this)

        //yha kro ok
        btn_skip_profile.setOnClickListener {

            val dialog = this?.let { it1 -> Dialog(it1) }
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setCancelable(false)
            dialog?.setContentView(R.layout.alert_profile)

            val btn_skip = dialog?.findViewById<Button>(R.id.btn_skip) as TextView

            val btn_create_profile = dialog.findViewById<View>(R.id.btn_create_profile) as Button
            btn_create_profile.setOnClickListener { dialog.dismiss() }


            btn_skip.setOnClickListener {
                setFragment(ChosseSecurityFragment())

                dialog?.dismiss()
            }

            dialog?.dismiss()
            dialog.show()
        }

        btn_save.setOnClickListener {
            val i = Intent(this, NewPassengerNavActivity::class.java)
            startActivity(i)

        }

        rlCard.setOnClickListener {


            val calendar = Calendar.getInstance()

            val datePickerDialog = DatePickerDialog(
                this,
                R.style.DatePickerDialogTheme,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val newDate = Calendar.getInstance()
                    newDate[year, monthOfYear] = dayOfMonth
                    val simpleDateFormat =
                        SimpleDateFormat("dd-MM-yyyy")
                    val date = simpleDateFormat.format(newDate.time)
                    etCardDate.visibility = View.VISIBLE
                    etCardDate.setText(date)
                },
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
            )

            datePickerDialog.show()
        }

        img_cam.setOnClickListener {    val dialog = this?.let { it1 -> Dialog(it1) }
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setCancelable(true)
            dialog?.setContentView(R.layout.open_cemera)

            val btn_cemera = dialog?.findViewById<Button>(R.id.btn_cemera) as TextView

            val btn_gellery = dialog.findViewById<View>(R.id.btn_gellery) as Button
            btn_gellery.setOnClickListener {
                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    //permission denied
                    val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE)
                    dialog?.dismiss()
                } else {
                    //permission already granted
                    pickImageFromGallery();
                    dialog?.dismiss()
                }

                dialog?.dismiss()
            }

            btn_cemera.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED ||
                        checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED
                    ) {
                        //permission was not enabled
                        val permission = arrayOf(
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                        //show popup to request permission
                        requestPermissions(permission, PERMISSION_CODE)
                        dialog?.dismiss()
                    } else {
                        //permission already granted
                        openCamera()
                        dialog?.dismiss()
                    }
                } else {
                    //system os is < marshmallow
                    openCamera()
                    dialog?.dismiss()
                }
                dialog?.dismiss()

            }
            dialog.show()
        }

        profile_image.setOnClickListener {
            val dialog = this?.let { it1 -> Dialog(it1) }
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setCancelable(true)
            dialog?.setContentView(R.layout.open_cemera)

            val btn_cemera = dialog?.findViewById<Button>(R.id.btn_cemera) as TextView

            val btn_gellery = dialog.findViewById<View>(R.id.btn_gellery) as Button
            btn_gellery.setOnClickListener {
                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    //permission denied
                    val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE)
                    dialog?.dismiss()
                } else {
                    //permission already granted
                    pickImageFromGallery();
                    dialog?.dismiss()
                }

                dialog?.dismiss()
            }

            btn_cemera.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED ||
                        checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED
                    ) {
                        //permission was not enabled
                        val permission = arrayOf(
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                        //show popup to request permission
                        requestPermissions(permission, PERMISSION_CODE)
                        dialog?.dismiss()
                    } else {
                        //permission already granted
                        openCamera()
                        dialog?.dismiss()
                    }
                } else {
                    //system os is < marshmallow
                    openCamera()
                    dialog?.dismiss()
                }
                dialog?.dismiss()

            }
            dialog.show()
        }


    }

    protected fun setFragment(fragment: Fragment?) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        if (fragment != null) {
            val args = Bundle()
            args.putString("profile", "passenger")
            fragment.setArguments(args)
            fragmentTransaction.add(android.R.id.content, fragment)
        }
        fragmentTransaction
            .addToBackStack(null).commit()
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
            PERMISSION_CODE1 -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    openCamera()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            profile_image.setImageURI(data?.data)
        }

        if (resultCode == Activity.RESULT_OK&& requestCode == IMAGE_CAPTURE_CODE){

            profile_image.setImageURI(image_uri)
        }
        if (requestCode === REQUEST_SCAN) {
            var resultDisplayStr: String
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                val scanResult: CreditCard =
                    data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT)

                // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                resultDisplayStr = """
                    Card Number: ${scanResult.redactedCardNumber}
                    
                    """.trimIndent()

                // Do something with the raw number, e.g.:
                // myService.setCardNumber( scanResult.cardNumber );
                if (scanResult.isExpiryValid) {
                    resultDisplayStr += """
                        Expiration Date: ${scanResult.expiryMonth}/${scanResult.expiryYear}
                        
                        """.trimIndent()
                }
                if (scanResult.cvv != null) {
                    // Never log or display a CVV
                    resultDisplayStr += """CVV has ${scanResult.cvv.length} digits.
"""
                }
                if (scanResult.postalCode != null) {
                    resultDisplayStr += """
                        Postal Code: ${scanResult.postalCode}
                        
                        """.trimIndent()
                }
            } else {
                resultDisplayStr = "Scan was canceled."
            }
            // do something with resultDisplayStr, maybe display it in a textView
            // resultTextView.setText(resultDisplayStr);
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
R.id.iv_business_dropdown->{
    if(ll_business.visibility == View.VISIBLE){
        ll_business.visibility = View.GONE
    }else{
        ll_business.visibility = View.VISIBLE
    }

}
           /* R.id.iv_business_dropdown -> {
                val scanIntent = Intent(this, CardIOActivity::class.java)

                // customize these values to suit your needs.

                // customize these values to suit your needs.
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true) // default: false

                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false) // default: false

                scanIntent.putExtra(
                    CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE,
                    false
                ) // default: false


                // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.

                // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
                startActivityForResult(scanIntent, REQUEST_SCAN)
            }*/
            }
        }

}



