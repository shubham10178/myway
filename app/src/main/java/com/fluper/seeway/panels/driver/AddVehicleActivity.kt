package com.fluper.seeway.panels.driver

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fluper.seeway.R
import com.fluper.seeway.base.BaseActivity
import com.fluper.seeway.onBoard.adapter.RemovePictures
import com.fluper.seeway.onBoard.adapter.UploadImagesAdapter
import com.fluper.seeway.utilitarianFiles.*
import kotlinx.android.synthetic.main.fragment_add_vehicle.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddVehicleActivity : BaseActivity(), View.OnClickListener {

    private var IMAGE_PICK_CODE = 100
    private var IMAGE_CAPTURE_CODE = 200
    private val PERMISSION_CODE = 300
    private val PERMISSION_CODE1 = 400
    private var image_uri: Uri? = null
    private var imageUri1: Uri? = null
    private val uvi_arrayList = ArrayList<Bitmap>()
    private val ucd_arrayList = ArrayList<Bitmap>()
    private var relationWithVehicle = ""
    private lateinit var driverViewModel: DriverViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_add_vehicle)
        statusBarFullScreenWithBackground()
        driverViewModel = ViewModelProvider(this).get(DriverViewModel::class.java)
        myObserver()
        initClickListener()
        imgUploadRec()
    }

    private fun myObserver() {
        driverViewModel.addVehicles.observe(this, androidx.lifecycle.Observer {
            ProgressBarUtils.getInstance().hideProgress()
            val intent = Intent()
            /*intent.putExtra(
                Constants.Driver, AddVehicleInfoModel(
                    edt_vn_driver.getString(),
                    edt_vmn_driver.getString(),
                    uvi_arrayList,
                    tvVehicleColor.getString(),
                    etCardDate_driver.getString(),
                    tvAvailSheet.getString(),
                    ucd_arrayList,
                    relationWithVehicle,
                    edt_describ.getString()
                )
            )*/
            intent.putParcelableArrayListExtra(
                Constants.Driver, it.response
            )
            setResult(111, intent)
            onBackPressed()
        })
        driverViewModel.throwable.observe(this, androidx.lifecycle.Observer {
            ProgressBarUtils.getInstance().hideProgress()
            ErrorUtils.handlerGeneralError(this, it)
        })
    }

    private fun initClickListener() {
        img_back_addv.setOnClickListener(this)
        vehicle_img_upload.setOnClickListener(this)
        car_doc_img_upload.setOnClickListener(this)
        ll_vehicle_registration.setOnClickListener(this)
        btn_save_addv.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back_addv -> {
                onBackPressed()
            }
            R.id.vehicle_img_upload -> {
                if (uvi_arrayList.size <= 2) {
                    uploadImg()
                    IMAGE_PICK_CODE = 100
                    IMAGE_CAPTURE_CODE = 200
                    dummy_vehicle_img.visibility = View.GONE
                    vehicle_img_rec.visibility = View.VISIBLE
                } else
                    showToast("You can upload only 3 images")
            }

            R.id.car_doc_img_upload -> {
                if (ucd_arrayList.size <= 0) {
                    uploadImg()
                    IMAGE_PICK_CODE = 101
                    IMAGE_CAPTURE_CODE = 201
                    dummy_car_img.visibility = View.GONE
                    car_doc_rec.visibility = View.VISIBLE
                } else
                    showToast("You can upload only one document")
            }
            R.id.ll_vehicle_registration -> {
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
                        etCardDate_driver.visibility = View.VISIBLE
                        etCardDate_driver.setText(date)
                    },
                    calendar[Calendar.YEAR],
                    calendar[Calendar.MONTH],
                    calendar[Calendar.DAY_OF_MONTH]
                )
                datePickerDialog.show()
            }
            R.id.btn_save_addv -> {
                if (isInputValid()) {
                    if (NetworkUtils.isInternetAvailable(this)) {
                        ProgressBarUtils.getInstance().showProgress(this, false)
                        driverViewModel.addVehicles(
                            access_token = sharedPreference.accessToken!!,
                            vehicle_number = getRequestBody(edt_vn_driver.getString()),
                            vehicle_model = getRequestBody(edt_vmn_driver.getString()),
                            vehicle_color = getRequestBody(tvVehicleColor.getString()),
                            vehicle_imgae = getMultipartBodyArrayList(
                                uvi_arrayList,
                                "vehicle_imgae"
                            ),
                            no_of_seats = getRequestBody(tvAvailSheet.getString()),
                            car_document = getMultipartBody(ucd_arrayList[0], "car_document"),
                            certificate_date = getRequestBody(etCardDate_driver.getString()),
                            relation_with = getRequestBody(relationWithVehicle),
                            description = getRequestBody(edt_describ.getString())
                        )
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        this.finish()
        super.onBackPressed()
    }

    private fun isInputValid(): Boolean {
        return when {
            edt_vn_driver.getString().isEmpty() -> {
                showToast("Please enter vehicle number")
                false
            }
            edt_vmn_driver.getString().isEmpty() -> {
                showToast("Please enter vehicle model name")
                false
            }
            uvi_arrayList.isNullOrEmpty() -> {
                showToast("Please upload vehicle image")
                false
            }
            tvVehicleColor.getString().isEmpty() -> {
                showToast("Please enter vehicle color")
                false
            }
            etCardDate_driver.getString().isEmpty() -> {
                showToast("Please enter vehicle registration date")
                false
            }
            tvAvailSheet.getString().isEmpty() ||
                    !tvAvailSheet.getString().isDigitsOnly() -> {
                showToast("Please enter no. of available sheets in digits")
                false
            }
            ucd_arrayList.isNullOrEmpty() -> {
                showToast("Please upload car documents")
                false
            }
            relationWithVehicle.isEmpty() -> {
                showToast("Please select relationship with vehicle")
                false
            }
            relationWithVehicle.equals("Other") && edt_describ.getString().isEmpty() -> {
                showToast("Please describe relationship with vehicle")
                false
            }
            else -> true
        }
    }

    private fun imgUploadRec() {
        vehicle_img_rec.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            true
        )
        car_doc_rec.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            true
        )

        rg_relation_vehicle.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioOwner -> {
                    relationWithVehicle = "Owner"
                    edt_describ.visibility = View.GONE
                }
                R.id.radioTenant -> {
                    relationWithVehicle = "Tenant"
                    edt_describ.visibility = View.GONE
                }
                R.id.radioLease -> {
                    relationWithVehicle = "Lease"
                    edt_describ.visibility = View.GONE
                }
                R.id.radioOther -> {
                    relationWithVehicle = "Other"
                    edt_describ.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_PICK_CODE)
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri =
            this.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == 0 && data == null) {
            if (uvi_arrayList.isNullOrEmpty()) {
                dummy_vehicle_img.visibility = View.VISIBLE
                vehicle_img_rec.visibility = View.GONE
            }
            if (ucd_arrayList.isNullOrEmpty()) {
                dummy_car_img.visibility = View.VISIBLE
                car_doc_rec.visibility = View.GONE
            }
        }
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            if (data!!.clipData != null) {
                val count = data.clipData!!
                    .itemCount
                for (i in 0 until count) {
                    imageUri1 = data.clipData!!.getItemAt(i).uri

                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri1)

                    uvi_arrayList.add(bitmap)
                }
                val uploadImageAdapter = UploadImagesAdapter(uvi_arrayList, this, object :
                    RemovePictures {
                    override fun removePictureId(picsCount: Int) {
                        if (picsCount == 0) {
                            dummy_vehicle_img.visibility = View.VISIBLE
                            vehicle_img_rec.visibility = View.GONE
                        }
                    }
                })
                vehicle_img_rec.adapter = uploadImageAdapter
            } else {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, data.data)

                uvi_arrayList.add(bitmap)
                val uploadImageAdapter = UploadImagesAdapter(uvi_arrayList, this, object :
                    RemovePictures {
                    override fun removePictureId(picsCount: Int) {
                        if (picsCount == 0) {
                            dummy_vehicle_img.visibility = View.VISIBLE
                            vehicle_img_rec.visibility = View.GONE
                        }
                    }
                })
                vehicle_img_rec.adapter = uploadImageAdapter
            }
        }
        if (resultCode == Activity.RESULT_OK && requestCode == 200) {

            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, image_uri)

            uvi_arrayList.add(bitmap)

            val uploadImageAdapter = UploadImagesAdapter(uvi_arrayList, this, object :
                RemovePictures {
                override fun removePictureId(picsCount: Int) {
                    if (picsCount == 0) {
                        dummy_vehicle_img.visibility = View.VISIBLE
                        vehicle_img_rec.visibility = View.GONE
                    }
                }
            })
            vehicle_img_rec.adapter = uploadImageAdapter
        }

        if (resultCode == Activity.RESULT_OK && requestCode == 101) {
            if (data!!.clipData != null) {
                val count = data.clipData!!
                    .itemCount
                for (i in 0 until count) {
                    imageUri1 = data.clipData!!.getItemAt(i).uri
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri1)
                    ucd_arrayList.add(bitmap)
                }
                val uploadImageAdapter = UploadImagesAdapter(ucd_arrayList, this, object :
                    RemovePictures {
                    override fun removePictureId(picsCount: Int) {
                        if (picsCount == 0) {
                            dummy_car_img.visibility = View.VISIBLE
                            car_doc_rec.visibility = View.GONE
                        }
                    }
                })
                car_doc_rec.adapter = uploadImageAdapter
            } else {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, data.data)

                ucd_arrayList.add(bitmap)
                val uploadImageAdapter = UploadImagesAdapter(ucd_arrayList, this, object :
                    RemovePictures {
                    override fun removePictureId(picsCount: Int) {
                        if (picsCount == 0) {
                            dummy_car_img.visibility = View.VISIBLE
                            car_doc_rec.visibility = View.GONE
                        }
                    }
                })
                car_doc_rec.adapter = uploadImageAdapter
            }
            super.onActivityResult(requestCode, resultCode, data)
        }
        if (resultCode == Activity.RESULT_OK && requestCode == 201) {
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, image_uri)
            uvi_arrayList.clear()
            ucd_arrayList.add(bitmap)


            val uploadImageAdapter = UploadImagesAdapter(ucd_arrayList, this, object :
                RemovePictures {
                override fun removePictureId(picsCount: Int) {
                    if (picsCount == 0) {
                        dummy_car_img.visibility = View.VISIBLE
                        car_doc_rec.visibility = View.GONE
                    }
                }
            })
            car_doc_rec.adapter = uploadImageAdapter
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun uploadImg() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.open_cemera)

        val btn_cemera = dialog.findViewById<Button>(R.id.btn_cemera) as TextView

        val btn_gellery = dialog.findViewById<View>(R.id.btn_gellery) as Button
        btn_gellery.setOnClickListener {
            if (this.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
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
                if (this.checkSelfPermission(android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
}