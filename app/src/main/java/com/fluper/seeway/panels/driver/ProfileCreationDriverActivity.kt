package com.fluper.seeway.panels.driver

import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Typeface
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fluper.seeway.R
import com.fluper.seeway.base.BaseActivity
import com.fluper.seeway.onBoard.activities.ChooseSecurityActivity
import com.fluper.seeway.onBoard.adapter.DriverVehicleInfoAdapter
import com.fluper.seeway.onBoard.adapter.RemovePictures
import com.fluper.seeway.onBoard.adapter.RemoveVehicle
import com.fluper.seeway.onBoard.adapter.UploadImagesAdapter
import com.fluper.seeway.panels.driver.model.AddVehicleResponseModel
import com.fluper.seeway.panels.driver.model.GetVehicleTypesResponseModel
import com.fluper.seeway.utilitarianFiles.*
import com.rilixtech.CountryCodePicker
import kotlinx.android.synthetic.main.activity_profile_creation_driver.*
import java.io.IOException

class ProfileCreationDriverActivity : BaseActivity(), View.OnClickListener,
    RadioGroup.OnCheckedChangeListener {
    private var imageUri1: Uri? = null
    private var IMAGE_PICK_CODE = 1000
    private val PERMISSION_CODE1 = 1000
    private var IMAGE_CAPTURE_CODE = 1005
    private var imageUri: Uri? = null
    private val PERMISSION_CODE = 1001
    private val udliArraylist = ArrayList<Bitmap>()
    private val upArraylist = ArrayList<Bitmap>()
    private val vehicleList = ArrayList<AddVehicleResponseModel.Response?>()
    private var driverVehicleInfoAdapter: DriverVehicleInfoAdapter? = null
    private lateinit var driverViewModel: DriverViewModel
    private var vehicleTypes = ArrayList<GetVehicleTypesResponseModel.Response?>()
    private var gender = "Male"
    private var smoker = "Smoker"
    private var city = ""
    private var vehicleTypeId = ""
    private var permissionCategory = ""

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_creation_driver)
        statusBarFullScreenWithBackground()
        driverViewModel = ViewModelProvider(this).get(DriverViewModel::class.java)
        val type = Typeface.createFromAsset(assets, "font/avenir_black.ttf")
        (ccp_driver as CountryCodePicker).typeFace = type
        myObserver()
        driverViewModel.getVehicleType()
        vehicleTypes.clear()
        udliArraylist.clear()
        upArraylist.clear()
        vehicleList.clear()
        cardNumber()
        expiryDateFormat()
        initClickListener()
        if (sharedPreference.userEmailId.isNotEmpty()) {
            edt_driver_email.setText(sharedPreference.userEmailId)
            edt_driver_email.isEnabled = false
        }
        if (sharedPreference.userCountryCode.isNotEmpty()) {
            ccp_driver.setDefaultCountryUsingNameCode(sharedPreference.userCountryCode)
            ccp_driver.resetToDefaultCountry()
        }
        if (sharedPreference.userMobile.isNotEmpty()) {
            edt_driver_phone_num.setText(sharedPreference.userMobile)
            edt_driver_phone_num.isEnabled = false
        }
        rg_type_per.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_want_tobe_emp -> {
                    img_driver_tobeEmp.visibility = View.VISIBLE
                    permissionCategory = "1"
                }
                R.id.radio_nopermission -> {
                    img_driver_tobeEmp.visibility = View.INVISIBLE
                    permissionCategory = "2"
                }
                R.id.radio_havepermission -> {
                    img_driver_tobeEmp.visibility = View.INVISIBLE
                    permissionCategory = "3"
                }
            }
        }
        radioSex.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioMale -> gender = "Male"
                R.id.radioFemale -> gender = "Female"
            }
        }
        radioSmoking.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioSmoker -> smoker = "Smoker"
                R.id.radioNonSmoker -> smoker = "Non-Smoker"
            }
        }
        etCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                city = etCity.selectedItem.toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //
            }
        }
        spVehicleTypes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                vehicleTypeId = vehicleTypes.find {
                    it?.name?.trim().equals(spVehicleTypes.selectedItem.toString().trim())
                }?._id!!
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //
            }
        }
        uploadRecyclerview()
    }

    private fun myObserver() {
        driverViewModel.getVehicleTypes.observe(this, Observer { it ->
            if (!it.response.isNullOrEmpty()) {
                vehicleTypes.clear()
                vehicleTypes = it?.response
                SpinnerUtil.setSpinner(
                    spVehicleTypes,
                    it.response.map { it?.name } as ArrayList<String>,
                    this)
            } else
                showToast("Vehicle types not available")
        })

        driverViewModel.deleteVehicles.observe(this, Observer {
            showToast(it.message!!)
            if (!it.response.isNullOrEmpty()) {
                vehicleList.clear()
                vehicleList.addAll(it.response)
                ll_add_vechicle.visibility = View.VISIBLE
                tvAddVehicle.text = "Add More Vehicle"
            } else {
                vehicleList.clear()
                tvAddVehicle.text = "Add Vehicle"
            }
            vehicle_info_rec.adapter = driverVehicleInfoAdapter
        })

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
            startActivity(Intent(this, ChooseSecurityActivity::class.java).apply {
                putExtra(Constants.UserType, sharedPreference.userType)
                this@ProfileCreationDriverActivity.finish()
            })
        })

        driverViewModel.throwable.observe(this, Observer {
            ProgressBarUtils.getInstance().hideProgress()
            ErrorUtils.handlerGeneralError(this, it)
        })
    }

    private fun initClickListener() {
        ivProfileImage.setOnClickListener(this)
        ivCamera.setOnClickListener(this)
        tvBusinessDropDown.setOnClickListener(this)
        img_upload.setOnClickListener(this)
        ll_add_vechicle.setOnClickListener(this)
        permission_img_upload.setOnClickListener(this)
        btnSave.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivProfileImage, R.id.ivCamera -> {
                profileImagePick()
            }
            R.id.tvBusinessDropDown -> {
                if (ll_business.visibility == View.VISIBLE) {
                    ll_business.visibility = View.GONE
                } else {
                    ll_business.visibility = View.VISIBLE
                }
            }
            R.id.img_upload -> {
                if (udliArraylist.size <= 0) {
                    uploadImg(driving_license_rec)
                    IMAGE_PICK_CODE = 1001
                    IMAGE_CAPTURE_CODE = 2001
                    dummy_img_dl.visibility = View.GONE
                    driving_license_rec.visibility = View.VISIBLE
                } else
                    showToast("You can add only a license")
            }
            R.id.ll_add_vechicle -> {
                if (vehicleList.size <= 3) {
                    ll_add_vechicle.visibility = View.VISIBLE
                    startActivityForResult(Intent(this, AddVehicleActivity::class.java), 111)
                } else {
                    ll_add_vechicle.visibility = View.GONE
                    showToast("You can add only four vehicles")
                }
            }
            R.id.permission_img_upload -> {
                if (upArraylist.size <= 0) {
                    uploadImg(upload_permission_rec)
                    IMAGE_PICK_CODE = 1004
                    IMAGE_CAPTURE_CODE = 2004
                    dummy_img_upi.visibility = View.GONE
                    upload_permission_rec.visibility = View.VISIBLE
                } else
                    showToast("You can add only a permission")
            }
            R.id.btnSave -> {
                if (sharedPreference.userType.equals(Constants.Driver)) {
                    if (isProfileInputsValid()) {
                        alertSubmit()
                    }
                } else {
                    alertSubmit()
                }
                /*if (img_driver_tobeEmp.isVisible) {
                    startActivity(Intent(this, DriverInsuranceActivity::class.java))
                } else {
                    alertSubmit()
                }*/
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun uploadRecyclerview() {
        val type = Typeface.createFromAsset(assets, "font/avenir_black.ttf")
        (ccp_driver as CountryCodePicker).typeFace = type

        driving_license_rec.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            true
        )

        upload_permission_rec.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            true
        )
    }

    private fun pickImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_PICK_CODE)
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000

        //Permission code
        private val PERMISSION_CODE = 1001
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
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
                if (grantResults.isNotEmpty() && grantResults[0] ==
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


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
        if (resultCode == Activity.RESULT_OK && requestCode == 1009) {
            if (data != (null)) {
                imageUri = data.data
                ivProfileImage.setImageURI(data.data)
            }
        }
        if (resultCode == Activity.RESULT_OK && requestCode == 2009) {
            if (imageUri != (null)) {
                ivProfileImage.setImageURI(imageUri)
            }
        }
        if (resultCode == Activity.RESULT_OK && requestCode == 1001) {
            if (data!!.clipData != null) {
                val count = data.clipData!!
                    .itemCount
                for (i in 0 until count) {
                    imageUri1 = data.clipData!!.getItemAt(i).uri
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri1)
                    udliArraylist.add(bitmap)
                }
                val uploadImageAdapter =
                    UploadImagesAdapter(udliArraylist, this, object : RemovePictures {
                        override fun removePictureId(picsCount: Int) {
                            if (picsCount == 0) {
                                dummy_img_dl.visibility = View.VISIBLE
                                driving_license_rec.visibility = View.GONE
                            }
                        }
                    })
                driving_license_rec.adapter = uploadImageAdapter
            } else {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, data.data)
                udliArraylist.add(bitmap)
                val uploadImageAdapter =
                    UploadImagesAdapter(udliArraylist, this, object : RemovePictures {
                        override fun removePictureId(picsCount: Int) {
                            if (picsCount == 0) {
                                dummy_img_dl.visibility = View.VISIBLE
                                driving_license_rec.visibility = View.GONE
                            }
                        }
                    })
                driving_license_rec.adapter = uploadImageAdapter
            }
        }

        if (resultCode == Activity.RESULT_OK && requestCode == 2001) {
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            udliArraylist.add(bitmap)
            val uploadImageAdapter =
                UploadImagesAdapter(udliArraylist, this, object : RemovePictures {
                    override fun removePictureId(picsCount: Int) {
                        if (picsCount == 0) {
                            dummy_img_dl.visibility = View.VISIBLE
                            driving_license_rec.visibility = View.GONE
                        }
                    }
                })
            driving_license_rec.adapter = uploadImageAdapter
        }
        if (resultCode == Activity.RESULT_OK && requestCode == 1004) {
            if (data!!.clipData != null) {
                val count = data.clipData!!
                    .itemCount
                for (i in 0 until count) {
                    imageUri1 = data.clipData!!.getItemAt(i).uri
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri1)
                    upArraylist.add(bitmap)
                }
                val uploadImageAdapter =
                    UploadImagesAdapter(upArraylist, this, object : RemovePictures {
                        override fun removePictureId(picsCount: Int) {
                            if (picsCount == 0) {
                                dummy_img_upi.visibility = View.VISIBLE
                                upload_permission_rec.visibility = View.GONE
                            }
                        }
                    })
                upload_permission_rec.adapter = uploadImageAdapter
            } else {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, data.data)
                upArraylist.add(bitmap)
                val uploadImageAdapter =
                    UploadImagesAdapter(upArraylist, this, object : RemovePictures {
                        override fun removePictureId(picsCount: Int) {
                            if (picsCount == 0) {
                                dummy_img_upi.visibility = View.VISIBLE
                                upload_permission_rec.visibility = View.GONE
                            }
                        }
                    })
                upload_permission_rec.adapter = uploadImageAdapter
            }
        }
        if (resultCode == Activity.RESULT_OK && requestCode == 2004) {
            rl_upload_per.isFocusableInTouchMode = true
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            upArraylist.add(bitmap)
            val uploadImageAdapter =
                UploadImagesAdapter(upArraylist, this, object : RemovePictures {
                    override fun removePictureId(picsCount: Int) {
                        if (picsCount == 0) {
                            dummy_img_upi.visibility = View.VISIBLE
                            upload_permission_rec.visibility = View.GONE
                        }
                    }
                })
            upload_permission_rec.adapter = uploadImageAdapter
        }
        if (resultCode == 111 && requestCode == 111 && data != null && data.hasExtra(Constants.Driver)) {
            vehicleInfo(data)
            showToast("Vehicle added")
        }
    }

    private fun vehicleInfo(data: Intent?) {
        data?.let {
            vehicleList.clear()
            vehicleList.addAll(
                it.getParcelableArrayListExtra<AddVehicleResponseModel.Response>(
                    Constants.Driver
                )!!
            )
            if (vehicleList.size <= 3) {
                tvAddVehicle.text = "Add More Vehicle"
                ll_add_vechicle.visibility = View.VISIBLE
            } else
                ll_add_vechicle.visibility = View.GONE
        }
        driverVehicleInfoAdapter =
            DriverVehicleInfoAdapter(vehicleList, this, object : RemoveVehicle {
                override fun removeVehicleId(vehicleId: String?, userId: String?) {
                    if (!vehicleId.isNullOrEmpty() && !userId.isNullOrEmpty())
                        driverViewModel.deleteVehicle(vehicleId, userId)
                }
            })
        vehicle_info_rec.adapter = driverVehicleInfoAdapter
    }

    @Throws(IOException::class)
    private fun rotateImageIfRequired(img: Bitmap, selectedImage: Uri): Bitmap? {
        val ei = ExifInterface(selectedImage.path!!)
        val orientation: Int =
            ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270)
            else -> img
        }
    }

    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 0) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    private fun rotateImage(img: Bitmap, degree: Int): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedImg =
            Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
        img.recycle()
        return rotatedImg
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun uploadImg(driving_license_rec: RecyclerView) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
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

    private fun showAlertSubmit() {
        val dialog = this.let { it1 -> Dialog(it1) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_marble32)

        val txt_msg = dialog.findViewById<View>(R.id.txt_msg) as TextView

        txt_msg.setOnClickListener {
            if (sharedPreference.userType.equals(Constants.Driver)) {
                if (NetworkUtils.isInternetAvailable(this)) {
                    ProgressBarUtils.getInstance().showProgress(this, false)
                    var firstName = ""
                    var lastName = ""
                    if (edt_driver_name.getString().contains(" ")) {
                        val arrayList = ArrayList(edt_driver_name.getString().split(" "))
                        firstName = arrayList[0]
                        arrayList.removeAt(0)
                        repeat(arrayList.size) {
                            lastName = lastName + " " + arrayList[it]
                        }
                    } else
                        firstName = edt_driver_name.getString()
                    driverViewModel.profile(
                        access_token = sharedPreference.accessToken!!,
                        user_type = getRequestBody(Constants.UserValueDriver),
                        first_name = getRequestBody(firstName),
                        last_name = getRequestBody(lastName),
                        city = getRequestBody(city),
                        id_proof = null,
                        account_holdar_name = getRequestBody(tvAccountHolderName.getString()),
                        account_number = getRequestBody(tvAccountNumber.getString()),
                        ifsc_code = getRequestBody(tvIfscCode.getString()),
                        branch_name = getRequestBody(tvBranchName.getString()),
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
                        smoking_status = getRequestBody(smoker),
                        vehicle_type_id = getRequestBody(vehicleTypeId),
                        driving_licence = getMultipartBody(udliArraylist[0], "driving_licence"),
                        user_id = getRequestBody(sharedPreference.userId),
                        user_permission = getRequestBody(permissionCategory),
                        upload_permission = getMultipartBody(upArraylist[0], "upload_permission"),
                        profile_image = getMultipartBody(
                            MediaStore.Images.Media.getBitmap(
                                this.contentResolver,
                                imageUri
                            ), "profile_image"
                        )
                    )
                } else
                    showToast("Poor connection")
            }else{
                startActivity(Intent(this, ChooseSecurityActivity::class.java).apply {
                    putExtra(Constants.UserType, sharedPreference.userType)
                    this@ProfileCreationDriverActivity.finish()
                })
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun isProfileInputsValid(): Boolean {
        return when {
            imageUri == null -> {
                showToast("Please upload profile pic")
                false
            }
            !edt_driver_name.isValidName() -> {
                false
            }
            !edt_driver_email.isValidEmail() -> {
                false
            }
            edt_driver_phone_num.getString().isEmpty() -> {
                showToast("Please enter mobile number")
                false
            }
            !edt_driver_phone_num.getString().isValidMobile -> {
                showToast("Please enter valid mobile number")
                false
            }
            gender.isEmpty() -> {
                showToast("Please select gender")
                false
            }
            smoker.isEmpty() -> {
                showToast("Please select smoking status")
                false
            }
            city.isEmpty() -> {
                showToast("Please select city")
                false
            }
            vehicleTypeId.isEmpty() -> {
                showToast("Please select vehicle type")
                false
            }
            udliArraylist.isNullOrEmpty() -> {
                showToast("Please upload driving license image")
                false
            }
            vehicleList.isNullOrEmpty() -> {
                showToast("Please add at least one vehicle")
                alertVehicleNotSelected()
                false
            }
            permissionCategory.isEmpty() -> {
                showToast("Please choose permission category")
                false
            }
            upArraylist.isNullOrEmpty() -> {
                showToast("Please upload permission documents")
                false
            }
            tvAccountNumber.getString().isEmpty() -> {
                showToast("Please enter account number")
                false
            }
            !tvAccountNumber.getString().isDigitsOnly() -> {
                showToast("Please enter valid account number")
                false
            }
            tvAccountHolderName.getString().isEmpty() -> {
                showToast("Please enter account holder name")
                false
            }
            tvBranchName.getString().isEmpty() -> {
                showToast("Please enter branch name")
                false
            }
            tvIfscCode.getString().isEmpty() -> {
                showToast("Please enter IFSC code")
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
    }

    private fun isCardDetails(): Boolean {
        return etCardNo.getString().isNotEmpty()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun profileImagePick() {
        val dialog = Dialog(this)
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
                IMAGE_PICK_CODE = 1009
                pickImage()
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
                    IMAGE_CAPTURE_CODE = 2009
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

    private fun alertVehicleNotSelected() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.driver_profile_alert)
        val txt_msg = dialog.findViewById<View>(R.id.txt_msg_alert) as TextView
        val btn_ok = dialog.findViewById<View>(R.id.btn_ok) as Button
        txt_msg.setText(R.string.vehicle_not_selected)
        btn_ok.setOnClickListener {
            startActivityForResult(Intent(this, AddVehicleActivity::class.java), 111)
            dialog.dismiss()
        }
        dialog.dismiss()
        dialog.show()
    }

    private fun alertSubmit() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.driver_profile_approval_alert1)
        val txt_msg = dialog.findViewById<View>(R.id.txt_msg) as TextView
        val btn_no = dialog.findViewById<View>(R.id.btn_no) as Button
        val btn_yes = dialog.findViewById<View>(R.id.btn_yes) as Button
        txt_msg.setText(R.string.alert_driver_profile1)
        btn_no.setOnClickListener {

            dialog.dismiss()
        }

        btn_yes.setOnClickListener {

            showAlertYes()
            dialog.dismiss()
        }
        dialog.dismiss()
        dialog.show()
    }

    private fun showAlertYes() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.driver_profile_approval_alert1)

        val txt_msg = dialog.findViewById<View>(R.id.txt_msg) as TextView
        val btn_no = dialog.findViewById<View>(R.id.btn_no) as Button
        val btn_yes = dialog.findViewById<View>(R.id.btn_yes) as Button
        txt_msg.setText(R.string.alert_driver_profile2)

        btn_no.setOnClickListener {

            dialog.dismiss()
        }

        btn_yes.setOnClickListener {
            showAlertSubmit()

            dialog.dismiss()
        }
        dialog.dismiss()
        dialog.show()
    }


    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        if (group is RadioButton) {
            // Is the button now checked?
            val checked = group.isChecked

        }
    }

    private fun pickImage() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
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
}