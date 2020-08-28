package com.fluper.seeway.panels.driver

import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
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
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fluper.seeway.R
import com.fluper.seeway.base.BaseActivity
import com.fluper.seeway.onBoard.activities.ChooseSecurityActivity
import com.fluper.seeway.onBoard.adapter.DriverVehicleInfoAdapter
import com.fluper.seeway.onBoard.adapter.RemovePictures
import com.fluper.seeway.onBoard.adapter.UploadImagesAdapter
import com.fluper.seeway.onBoard.model.ImageUploadModel
import com.fluper.seeway.onBoard.model.VehicleInfoModel
import com.fluper.seeway.utilitarianFiles.Constants
import com.fluper.seeway.utilitarianFiles.statusBarFullScreenWithBackground
import com.rilixtech.CountryCodePicker
import kotlinx.android.synthetic.main.activity_profile_creation_driver.*
import kotlinx.android.synthetic.main.activity_profile_creation_driver.btnSave
import kotlinx.android.synthetic.main.activity_profile_creation_driver.etCardDate
import kotlinx.android.synthetic.main.activity_profile_creation_driver.etCardNo
import kotlinx.android.synthetic.main.activity_profile_creation_driver.ivCamera
import kotlinx.android.synthetic.main.activity_profile_creation_driver.ivProfileImage
import kotlinx.android.synthetic.main.activity_profile_creation_driver.ll_business
import kotlinx.android.synthetic.main.activity_profile_creation_driver.tvBusinessDropDown
import java.io.IOException
import kotlin.collections.ArrayList

class ProfileCreationDriverActivity : BaseActivity(), View.OnClickListener,
    RadioGroup.OnCheckedChangeListener {
    private var imageUri1: Uri? = null
    private var vnName: String? = null
    private var vmnModelNumber: String? = null
    private var IMAGE_PICK_CODE = 1000
    private val PERMISSION_CODE1 = 1000
    private var IMAGE_CAPTURE_CODE = 1005
    private var imageUri: Uri? = null
    private val PERMISSION_CODE = 1001
    private val udliArraylist = ArrayList<ImageUploadModel>()
    private val vehicleList = ArrayList<VehicleInfoModel>()
    private val upArraylist = ArrayList<ImageUploadModel>()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_creation_driver)
        statusBarFullScreenWithBackground()
        val type = Typeface.createFromAsset(assets, "font/avenir_black.ttf")
        (ccp_driver as CountryCodePicker).typeFace = type
        cardNumber()
        expiryDateFormat()
        initClickListener()
        try {
            var pref: SharedPreferences = baseContext.getSharedPreferences("User_info", 0)
            val nameStr: String = pref.getString("NAME", "").toString()
            val emailStr: String = pref.getString("EMAIL", "").toString()
            val phoneStr: String = pref.getString("PHONE", "").toString()
            edt_driver_name.setText(nameStr)
            edt_driver_email.setText(emailStr)
            edt_driver_phone_num.setText(phoneStr)
        }catch (e: Exception){
           e.printStackTrace()
        }
        if(!intent.getStringExtra("vn_name").isNullOrEmpty() && !intent.getStringExtra("vmn_model_number").isNullOrEmpty()){
             vnName = intent.getStringExtra("vn_name")
             vmnModelNumber = intent.getStringExtra("vmn_model_number")
            vehicleInfo(vnName,vmnModelNumber)
        }
        rg_type_per.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_want_tobe_emp -> img_driver_tobeEmp.visibility = View.VISIBLE
                R.id.radio_nopermission -> img_driver_tobeEmp.visibility = View.INVISIBLE
                R.id.radio_havepermission -> img_driver_tobeEmp.visibility = View.INVISIBLE
            }
        }
        uploadRecyclerview()
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
            R.id.ivProfileImage,R.id.ivCamera->{ profileImagePick() }
            R.id.tvBusinessDropDown->{
                if (ll_business.visibility == View.VISIBLE) {
                    ll_business.visibility = View.GONE
                } else {
                    ll_business.visibility = View.VISIBLE
                }
            }
            R.id.img_upload -> {
                uploadImg(driving_license_rec)
                IMAGE_PICK_CODE = 1001
                IMAGE_CAPTURE_CODE = 2001
                dummy_img_dl.visibility = View.GONE
                driving_license_rec.visibility = View.VISIBLE
            }
            R.id.ll_add_vechicle->{
                val edt_driver_name :String = edt_driver_name.getText().toString()
                val edt_driver_email :String = edt_driver_email.getText().toString()
                val edt_driver_phone_num :String = edt_driver_phone_num.getText().toString()
                val pref : SharedPreferences = getBaseContext().getSharedPreferences("User_info", 0)
                val editor :SharedPreferences.Editor = pref.edit()
                editor.putString("NAME", edt_driver_name)
                editor.putString("EMAIL", edt_driver_email)
                editor.putString("PHONE", edt_driver_phone_num)
                editor.commit()
                startActivity(Intent(this,AddVehicleActivity::class.java))
            }
            R.id.permission_img_upload -> {
                uploadImg(upload_permission_rec)
                IMAGE_PICK_CODE = 1004
                IMAGE_CAPTURE_CODE = 2004
                dummy_img_upi.visibility = View.GONE
                upload_permission_rec.visibility = View.VISIBLE
            }
            R.id.btnSave->{
                if(vehicleList.isEmpty()){
                    alertVehicleNotSelected()
                }else {
                    if (img_driver_tobeEmp.isVisible) {
                        startActivity(Intent(this,DriverInsuranceActivity::class.java))
                    } else {
                        alertSubmit()
                    }
                }
            }
        }
    }
    
    @RequiresApi(Build.VERSION_CODES.M)
    fun uploadRecyclerview() {
        val type = Typeface.createFromAsset(assets, "font/avenir_black.ttf")
        (ccp_driver as CountryCodePicker).setTypeFace(type)

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
            else->super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
        if (resultCode == Activity.RESULT_OK && requestCode == 1009) {
            if(data !=(null)) {
                ivProfileImage.setImageURI(data?.data)
            }
        }

        if (resultCode == Activity.RESULT_OK && requestCode == 2009) {
            if(imageUri !=(null)) {
                ivProfileImage.setImageURI(imageUri)
            }
        }

        if (resultCode == Activity.RESULT_OK && requestCode == 1001) {
            if (data!!.clipData != null) {
                val count = data!!.clipData!!
                    .itemCount
                for (i in 0 until count) {
                    imageUri1 = data!!.clipData!!.getItemAt(i).uri

                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri1)

                    udliArraylist.add(ImageUploadModel(bitmap))
                }
                val uploadImageAdapter = UploadImagesAdapter(udliArraylist, this,object :RemovePictures{
                    override fun removePictureId(picsCount: Int) {
                        if (picsCount==0){
                            dummy_img_dl.visibility = View.VISIBLE
                            driving_license_rec.visibility = View.GONE
                        }
                    }
                })
                driving_license_rec.adapter = uploadImageAdapter
            }
            else{
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, data?.data)

                udliArraylist.add(ImageUploadModel(bitmap))
                val uploadImageAdapter = UploadImagesAdapter(udliArraylist, this,object :RemovePictures{
                    override fun removePictureId(picsCount: Int) {
                        if (picsCount==0){
                            dummy_img_dl.visibility = View.VISIBLE
                            driving_license_rec.visibility = View.GONE
                        }
                    }
                })
                driving_license_rec.adapter = uploadImageAdapter
            }
        }

        if(resultCode == Activity.RESULT_OK && requestCode == 2001){

            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)

            udliArraylist.add(ImageUploadModel(bitmap))



            val uploadImageAdapter = UploadImagesAdapter(udliArraylist, this,object :RemovePictures{
                override fun removePictureId(picsCount: Int) {
                    if (picsCount==0){
                        dummy_img_dl.visibility = View.VISIBLE
                        driving_license_rec.visibility = View.GONE
                    }
                }
            })
            driving_license_rec.adapter = uploadImageAdapter
        }

        if (resultCode == Activity.RESULT_OK && requestCode == 1004) {
            if (data!!.clipData != null) {
                val count = data!!.clipData!!
                    .itemCount
                for (i in 0 until count) {
                    imageUri1 = data!!.clipData!!.getItemAt(i).uri

                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri1)

                    upArraylist.add(ImageUploadModel(bitmap))
                }
                val uploadImageAdapter = UploadImagesAdapter(upArraylist, this,object :RemovePictures{
                    override fun removePictureId(picsCount: Int) {
                        if (picsCount==0){
                            dummy_img_upi.visibility = View.VISIBLE
                            upload_permission_rec.visibility = View.GONE
                        }
                    }
                })
                upload_permission_rec.adapter = uploadImageAdapter
            }
            else {

                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, data?.data)

                upArraylist.add(ImageUploadModel(bitmap))
                val uploadImageAdapter = UploadImagesAdapter(upArraylist, this,object :RemovePictures{
                    override fun removePictureId(picsCount: Int) {
                        if (picsCount==0){
                            dummy_img_upi.visibility = View.VISIBLE
                            upload_permission_rec.visibility = View.GONE
                        }
                    }
                })
                upload_permission_rec.adapter = uploadImageAdapter
            }

        }
        if(resultCode == Activity.RESULT_OK && requestCode == 2004){
            rl_upload_per.isFocusableInTouchMode = true
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)

            upArraylist.add(ImageUploadModel(bitmap))


            val uploadImageAdapter = UploadImagesAdapter(upArraylist, this,object :RemovePictures{
                override fun removePictureId(picsCount: Int) {
                    if (picsCount==0){
                        dummy_img_upi.visibility = View.VISIBLE
                        upload_permission_rec.visibility = View.GONE
                    }
                }
            })
            upload_permission_rec.adapter = uploadImageAdapter
        }
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
    fun uploadImg(driving_license_rec : RecyclerView) {
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
            startActivity(Intent(this, ChooseSecurityActivity::class.java).apply {
                putExtra(Constants.UserType, Constants.Driver)
            })
            dialog.dismiss()
        }
        dialog.show()
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
    
    private fun vehicleInfo(vn_name: String?, vmn_model_number: String?){
        vehicle_info_rec.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        vehicleList.add(VehicleInfoModel(vn_name!!, vmn_model_number!!))

        val  adapter = DriverVehicleInfoAdapter(vehicleList, this)
        vehicle_info_rec.adapter = adapter

    }

    private fun alertVehicleNotSelected(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.driver_profile_alert)
        val txt_msg = dialog.findViewById<View>(R.id.txt_msg_alert) as TextView
        val btn_ok = dialog.findViewById<View>(R.id.btn_ok) as Button
        txt_msg.setText(R.string.vehicle_not_selected)
        btn_ok.setOnClickListener { dialog.dismiss() }
        dialog.dismiss()
        dialog.show()
    }

    private fun alertSubmit(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.driver_profile_approval_alert1)
        val txt_msg = dialog.findViewById<View>(R.id.txt_msg) as TextView
        val btn_no = dialog.findViewById<View>(R.id.btn_no) as Button
        val btn_yes = dialog.findViewById<View>(R.id.btn_yes) as Button
        txt_msg.setText(R.string.alert_driver_profile1)
        btn_no.setOnClickListener {

            dialog.dismiss() }

        btn_yes.setOnClickListener {

            showAlertYes()
            dialog.dismiss() }
        dialog.dismiss()
        dialog.show()
    }
    private fun showAlertYes(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.driver_profile_approval_alert1)

        val txt_msg = dialog.findViewById<View>(R.id.txt_msg) as TextView
        val btn_no = dialog.findViewById<View>(R.id.btn_no) as Button
        val btn_yes = dialog.findViewById<View>(R.id.btn_yes) as Button
        txt_msg.setText(R.string.alert_driver_profile2)

        btn_no.setOnClickListener {

            dialog.dismiss() }

        btn_yes.setOnClickListener {
            showAlertSubmit()

            dialog.dismiss() }
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
}