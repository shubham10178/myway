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
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fluper.seeway.R
import com.fluper.seeway.base.BaseActivity
import com.fluper.seeway.onBoard.activities.ChooseSecurityActivity
import com.fluper.seeway.onBoard.activities.OtpVerificationActivity
import com.fluper.seeway.panels.driver.DriverViewModel
import com.fluper.seeway.utilitarianFiles.*
import com.rilixtech.CountryCodePicker
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard
import kotlinx.android.synthetic.main.activity_profile_creation_passenger.*

class ProfileCreationPassengerActivity : BaseActivity(), View.OnClickListener {
    private val REQUEST_SCAN: Int = 100
    private val PERMISSION_CODE1 = 1000
    private val IMAGE_PICK_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null
    private val PERMISSION_CODE = 1001
    private lateinit var driverViewModel: DriverViewModel
    private var gender = "Male"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_creation_passenger)
        statusBarFullScreenWithBackground()
        driverViewModel = ViewModelProvider(this).get(DriverViewModel::class.java)
        val type = Typeface.createFromAsset(assets, "font/avenir_black.ttf")
        (ccpPassenger as CountryCodePicker).typeFace = type
        myObserver()
        cardNumber()
        expiryDateFormat()
        initClickListener()


        Log.e("Bharat", sharedPreference.loginWith)
        when (sharedPreference.loginWith) {
            Constants.LoginWithEmail -> {
                ccpPassenger.isEnabled = true

            }
            Constants.LoginWithMobile -> {
                ccpPassenger.isEnabled = false
            }
        }


        if (sharedPreference.userEmailId.isNotEmpty()) {
            tvPassengerEmail.setText(sharedPreference.userEmailId)
            tvPassengerEmail.isEnabled = false
        }
        if (sharedPreference.userCountryCode.isNotEmpty()) {
            ccpPassenger.setDefaultCountryUsingNameCode(sharedPreference.userCountryCode)
            ccpPassenger.resetToDefaultCountry()
        }
        if (sharedPreference.userMobile.isNotEmpty()) {
            tvPassengerMobile.setText(sharedPreference.userMobile)
            tvPassengerMobile.isEnabled = false
        }
        radioSex.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioMale -> gender = "Male"
                R.id.radioFemale -> gender = "Female"
            }
        }
    }

    private fun myObserver() {
        driverViewModel.profileCreation.observe(this, Observer {
            ProgressBarUtils.getInstance().hideProgress()
            showToast(it.message!!)
            if (!it.response?._id.isNullOrEmpty())
                sharedPreference.userId = it.response?._id!!
            else
                sharedPreference.userId = ""
            if (!it.response?.mobile_number.isNullOrEmpty())
                sharedPreference.userMobile = it.response?.mobile_number!!
            else
                sharedPreference.userMobile = ""
            if (!it.response?.country_code.isNullOrEmpty())
                sharedPreference.userCountryCode = it.response?.country_code!!
            else
                sharedPreference.userCountryCode = ""
            if (!it.response?.access_token.isNullOrEmpty())
                sharedPreference.accessToken = it.response?.access_token!!
            else
                sharedPreference.accessToken = ""
            if (!it.response?.profile_image.isNullOrEmpty())
                sharedPreference.profileImage = it.response?.profile_image!!
            else
                sharedPreference.profileImage = ""
            if (!it.response?.email.isNullOrEmpty())
                sharedPreference.userEmailId = it.response?.email!!
            else
                sharedPreference.userEmailId = ""
            if (!it.response?.first_name.isNullOrEmpty())
                sharedPreference.userFirstName = it.response?.first_name!!
            else
                sharedPreference.userFirstName = ""
            if (!it.response?.last_name.isNullOrEmpty())
                sharedPreference.userLastName = it.response?.last_name!!
            else
                sharedPreference.userLastName = ""


            /*  if ((it.response?.is_mobile_verified?.trim()
                      ?.toInt() == 0) || (it.response?.is_email_verified?.trim()?.toInt() == 0)
              ) {
                  startActivity(Intent(this, OtpVerificationActivity::class.java).apply {
                      putExtra(Constants.CameFrom, Constants.SignUp)
                      this@ProfileCreationPassengerActivity.finish()
                  })
              } */

            if (it.response?.is_mobile_verified?.trim()
                    ?.toInt() == 1
            ) {
                startActivity(Intent(this, ChooseSecurityActivity::class.java).apply {
                    putExtra(Constants.UserType, sharedPreference.userType)
                    this@ProfileCreationPassengerActivity.finish()
                })
            }

            if ((it.response?.is_email_verified?.trim()?.toInt() == 1)) {
                startActivity(Intent(this, ChooseSecurityActivity::class.java).apply {
                    putExtra(Constants.UserType, sharedPreference.userType)
                    this@ProfileCreationPassengerActivity.finish()
                })
            } /*else {
                startActivity(Intent(this, ChooseSecurityActivity::class.java).apply {
                    putExtra(Constants.UserType, sharedPreference.userType)
                    this@ProfileCreationPassengerActivity.finish()
                })
            }*/
        })



        driverViewModel.throwable.observe(this, Observer {
            ProgressBarUtils.getInstance().hideProgress()
            ErrorUtils.handlerGeneralError(this, it)
        })
    }

    private fun initClickListener() {
        btnSkip.setOnClickListener(this)
        ivProfileImage.setOnClickListener(this)
        ivCamera.setOnClickListener(this)
        tvBusinessDropDown.setOnClickListener(this)
        btnSave.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSkip -> {
                showProfileSkipDialog()
            }
            R.id.ivProfileImage, R.id.ivCamera -> {
                showImagePickerDialog()
            }
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
            R.id.btnSave -> {
                if (isProfileInputsValid()) {
                    if (NetworkUtils.isInternetAvailable(this)) {
                        ProgressBarUtils.getInstance().showProgress(this, false)
                        var firstName = ""
                        var lastName = ""
                        if (tvPassengerName.getString().contains(" ")) {
                            val arrayList = ArrayList(tvPassengerName.getString().split(" "))
                            firstName = arrayList[0]
                            arrayList.removeAt(0)
                            repeat(arrayList.size) {
                                lastName = lastName + " " + arrayList[it]
                            }
                        } else
                            firstName = tvPassengerName.getString()
                        driverViewModel.profile(
                            access_token = sharedPreference.accessToken!!,
                            user_type = getRequestBody(Constants.UserValuePassenger),
                            first_name = getRequestBody(firstName),
                            last_name = getRequestBody(lastName),
                            city = getRequestBody(""),
                            id_proof = null,
                            account_holdar_name = getRequestBody(""),
                            account_number = getRequestBody(""),
                            ifsc_code = getRequestBody(""),
                            branch_name = getRequestBody(""),
                            business_name = getRequestBody(tvBusinessName.getString()),
                            business_address1 = getRequestBody(tvBusinessAddress1.getString()),
                            business_address2 = getRequestBody(tvBusinessAddress2.getString()),
                            business_city = getRequestBody(tvBusinessCity.getString()),
                            business_country = getRequestBody(tvBusinessCountry.getString()),
                            vat_number = getRequestBody(tvVatNumber.getString()),
                            card_number = getRequestBody(etCardNo.getString()),
                            expiry_date = getRequestBody(etCardDate.getString()),
                            cvv = getRequestBody(tvCvv.getString()),
                            gexpay_account = getRequestBody(tvGexpayAccount.getString()),
                            gender = getRequestBody(gender),
                            smoking_status = getRequestBody(""),
                            vehicle_type_id = null,
                            driving_licence = null,
                            user_id = getRequestBody(sharedPreference.userId),
                            user_permission = getRequestBody(""),
                            upload_permission = null,
                            profile_image = when (image_uri) {
                                null -> {
                                    null
                                }
                                else -> {
                                    getMultipartBody(
                                        MediaStore.Images.Media.getBitmap(
                                            this.contentResolver,
                                            image_uri
                                        ), "profile_image"
                                    )
                                }
                            },
                            email = getRequestBody(tvPassengerEmail.getString()),
                            country_code = getRequestBody(ccpPassenger.selectedCountryCodeWithPlus),
                            mobile_number = getRequestBody(tvPassengerMobile.getString())
                        )
                    } else
                        showToast("Poor connection")
                }
            }
        }
    }

    private fun showProfileSkipDialog() {
        val dialog = this.let { it1 -> Dialog(it1) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_profile)
        val btn_skip = dialog.findViewById<Button>(R.id.btn_skip) as TextView
        val btn_create_profile = dialog.findViewById<View>(R.id.btn_create_profile) as Button
        btn_create_profile.setOnClickListener { dialog.dismiss() }
        btn_skip.setOnClickListener {
            startActivity(Intent(this, ChooseSecurityActivity::class.java).apply {
                putExtra(Constants.UserType, sharedPreference.userType)
            })
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun isProfileInputsValid(): Boolean {
        return when {
            /*image_uri == null -> {
                showToast("Please upload profile pic")
                false
            }*/
            !tvPassengerName.isValidName() -> {
                false
            }
            tvPassengerName.getString()
                .isNotEmpty() && (tvPassengerName.getString().length < 3 || tvPassengerName.getString().length > 35) -> {
                showToast("Name should not exceed 35 characters")
                false
            }
            !tvPassengerEmail.isValidEmail() -> {
                false
            }
            tvPassengerMobile.getString().isEmpty() -> {
                showToast("Please enter mobile number")
                false
            }
            !tvPassengerMobile.getString().isValidMobile -> {
                showToast("Please enter valid mobile number")
                false
            }
            gender.isEmpty() -> {
                showToast("Please select gender")
                false
            }
            isBusinessName() && tvBusinessName.getString().isEmpty() -> {
                showToast("Please enter business or company name")
                false
            }
            isBusinessName() && (tvBusinessAddress1.getString()
                .isEmpty() && tvBusinessAddress2.getString().isEmpty()) -> {
                showToast("Please enter business address")
                false
            }
            isBusinessName() && tvBusinessCity.getString().isEmpty() -> {
                showToast("Please enter business city")
                false
            }
            isBusinessName() && tvBusinessCountry.getString().isEmpty() -> {
                showToast("Please enter business country")
                false
            }
            isBusinessName() && tvVatNumber.getString().isEmpty() -> {
                showToast("Please enter VAT number")
                false
            }
            isCardDetails() && etCardNo.text.isNullOrEmpty() -> {
                showToast("Please enter card number")
                return false
            }
            isCardDetails() && etCardNo.text.toString().length < 19 -> {
                showToast("Please enter valid card number")
                return false
            }
            isCardDetails() && etCardDate.text.isNullOrEmpty() -> {
                showToast("Please enter expiry date")
                return false
            }
            isCardDetails() && etCardDate.text.toString().length < 5 -> {
                showToast("Please enter valid expiry date")
                return false
            }
            isCardDetails() && tvCvv.text.isNullOrEmpty() -> {
                showToast("Please enter cvv no")
                return false
            }
            isCardDetails() && tvCvv.text.toString().length < 3 -> {
                showToast("Please enter valid cvv no")
                return false
            }
            tvGexpayAccount.getString().isEmpty() -> {
                showToast("Please enter GexPay account")
                return false
            }
            else -> true
        }
    }

    private fun isBusinessName(): Boolean {
        return tvBusinessName.getString().isNotEmpty()
                || tvBusinessAddress1.getString().isNotEmpty()
                || tvBusinessAddress2.getString().isNotEmpty()
                || tvBusinessCity.getString().isNotEmpty()
                || tvBusinessCountry.getString().isNotEmpty()
                || tvVatNumber.getString().isNotEmpty()
    }

    private fun isCardDetails(): Boolean {
        return etCardNo.getString().isNotEmpty()
                || etCardDate.getString().isNotEmpty()
                || tvCvv.getString().isNotEmpty()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showImagePickerDialog() {
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
                        etCardDate.setText(current + "/")
                        etCardDate.setSelection(current.length + 1)
                    } else {
                        if (current.length == 2 && start == 1) {
                            var shiftedCharInMonth =
                                current.removeSuffix(current[current.length - 1].toString())
                            var shiftedCharInYear = current[current.length - 1].toString()
                            etCardDate.setText("0" + shiftedCharInMonth + "/" + shiftedCharInYear)
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
            image_uri = data?.data
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



