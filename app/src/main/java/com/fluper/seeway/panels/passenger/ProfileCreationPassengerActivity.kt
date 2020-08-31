package com.fluper.seeway.panels.passenger

import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.fluper.seeway.R
import com.fluper.seeway.base.BaseActivity
import com.fluper.seeway.onBoard.activities.ChooseSecurityActivity
import com.fluper.seeway.utilitarianFiles.Constants
import com.fluper.seeway.utilitarianFiles.statusBarFullScreenWithBackground
import com.rilixtech.CountryCodePicker
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard
import kotlinx.android.synthetic.main.activity_profile_creation_passenger.*

class ProfileCreationPassengerActivity : BaseActivity(), View.OnClickListener {
    private val REQUEST_SCAN: Int = 100
    private val IMAGE_PICK_CODE = 1000
    private val PERMISSION_CODE1 = 1000
    private val IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null
    private val PERMISSION_CODE = 1001

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_creation_passenger)
        statusBarFullScreenWithBackground()
        val type = Typeface.createFromAsset(assets, "font/avenir_black.ttf")
        (ccp as CountryCodePicker).typeFace = type
        cardNumber()
        expiryDateFormat()
        initClickListener()
    }

    private fun initClickListener() {
        btnSkip.setOnClickListener(this)
        ivProfileImage.setOnClickListener(this)
        ivCamera.setOnClickListener(this)
        tvBusinessDropDown.setOnClickListener(this)
        //rlCard.setOnClickListener(this)
        btnSave.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSkip->{ showProfileSkipDialog() }
            R.id.ivProfileImage,R.id.ivCamera->{ showImagePickerDialog() }
            R.id.tvBusinessDropDown -> {
                if (ll_business.visibility == View.VISIBLE) {
                    ll_business.visibility = View.GONE
                } else {
                    ll_business.visibility = View.VISIBLE
                }
            }
            /*R.id.rlCard->{
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
                datePickerDialog.show()}*/
            R.id.btnSave->{
                startActivity(Intent(this, ChooseSecurityActivity::class.java).apply {
                    putExtra(Constants.UserType,sharedPreference.userType)
                    this@ProfileCreationPassengerActivity.finish()
                })
            }
        }
    }
    private fun showProfileSkipDialog(){
        val dialog = this.let { it1 -> Dialog(it1) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_profile)
        val btn_skip = dialog.findViewById<Button>(R.id.btn_skip) as TextView
        val btn_create_profile = dialog.findViewById<View>(R.id.btn_create_profile) as Button
        btn_create_profile.setOnClickListener { dialog.dismiss() }
        btn_skip.setOnClickListener {
            startActivity(Intent(this, ChooseSecurityActivity::class.java).apply {
                putExtra(Constants.UserType,sharedPreference.userType)
            })
            dialog.dismiss()
        }
        dialog.show()
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun showImagePickerDialog(){
        val dialog = this.let { it1 -> Dialog(it1) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.open_cemera)

        val btn_cemera = dialog.findViewById<Button>(R.id.btn_cemera) as TextView

        val btn_gellery = dialog.findViewById<View>(R.id.btn_gellery) as Button
        btn_gellery.setOnClickListener {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED
            ) {
                //permission denied
                val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                //show popup to request runtime permission
                requestPermissions(permissions, PERMISSION_CODE)
                dialog.dismiss()
            } else {
                //permission already granted
                pickImageFromGallery()
                dialog.dismiss()
            }

            dialog.dismiss()
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
                    dialog.dismiss()
                } else {
                    //permission already granted
                    openCamera()
                    dialog.dismiss()
                }
            } else {
                //system os is < marshmallow
                openCamera()
                dialog.dismiss()
            }
            dialog.dismiss()

        }
        dialog.show()
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
    private fun cardNumber() {
        var x = 1
        var add = 0
        etCardNo.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (etCardNo.text.toString().isEmpty()) {
                    x = 1
                    add = 0
                }
                if (etCardNo.text!!.length == (4 * x) + add) {
                    if (x < 4)
                        etCardNo.setText(etCardNo.text.toString() + " ")
                    x++
                    add++
                    etCardNo.setSelection(etCardNo.text.toString().length)
                }
                when (etCardNo.text.toString().length) {
                    4 -> {
                        x = 1
                        add = 0
                    }
                    9 -> {
                        x = 2
                        add = 1
                    }
                    14 -> {
                        x = 3
                        add = 2
                    }
                    19 -> {
                        x = 4
                        add = 3
                    }
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }
    private fun expiryDateFormat() {
        etCardDate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {//
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {//
            }

            override fun onTextChanged(p0: CharSequence?, start: Int, removed: Int, added: Int) {
                var current = p0.toString()
                if (current.length == 2 && start == 1) {
                    if (current.trim('/').toInt() <= 12) {
                        etCardDate.setText(current + "/");
                        etCardDate.setSelection(current.length + 1)
                    } else {
                        if (current.length == 2 && start == 1) {
                            var shiftedCharInMonth =
                                current.removeSuffix(current[current.length - 1].toString())
                            var shiftedCharInYear = current[current.length - 1].toString()
                            etCardDate.setText("0" + shiftedCharInMonth + "/" + shiftedCharInYear);
                            etCardDate.setSelection(current.length + 2)
                        }
                    }
                } else if (current.length == 2 && removed == 1) {
                    current = current.substring(0, 1)
                    etCardDate.setText(current)
                    etCardDate.setSelection(current.length)
                }
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
            PERMISSION_CODE1 -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    openCamera()
                } else {
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            ivProfileImage.setImageURI(data?.data)
        }

        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_CAPTURE_CODE) {

            ivProfileImage.setImageURI(image_uri)
        }
        if (requestCode === REQUEST_SCAN) {
            var resultDisplayStr: String
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                val scanResult: CreditCard =
                    data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT)!!

                // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                resultDisplayStr = """
                    Card Number: ${scanResult.redactedCardNumber}
                    
                    """.trimIndent()

                // Do something with the raw number, e.g.:
                // myService.setCardNumber( scanResult.cardNumber )
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
}



