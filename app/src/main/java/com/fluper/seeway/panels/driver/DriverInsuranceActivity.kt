package com.fluper.seeway.panels.driver

import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.fluper.seeway.R
import com.fluper.seeway.base.BaseActivity
import com.fluper.seeway.onBoard.adapter.RemovePictures
import com.fluper.seeway.onBoard.adapter.UploadImagesAdapter
import com.fluper.seeway.onBoard.model.ImageUploadModel
import com.fluper.seeway.utilitarianFiles.statusBarFullScreenWithBackground
import kotlinx.android.synthetic.main.fragment_driver_insurance.*

class DriverInsuranceActivity : BaseActivity(), View.OnClickListener {

    private var IMAGE_PICK_CODE = 300
    private var IMAGE_CAPTURE_CODE = 400
    private val PERMISSION_CODE = 1
    private val PERMISSION_CODE1 = 2
    private var imageUri: Uri? = null
    private var imageUri1: Uri? = null
    private val idorpassportArraylist = ArrayList<ImageUploadModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBarFullScreenWithBackground()
        setContentView(R.layout.fragment_driver_insurance)
        initClickListener()
        imgUploadRec()
    }

    private fun initClickListener() {
        img_upload_passport.setOnClickListener(this)
        btn_save_insur.setOnClickListener(this)
        img_back_insurence.setOnClickListener(this)
    }

    private fun imgUploadRec() {
        passport_rec.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            true
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_upload_passport -> {
                uploadImg()
                IMAGE_PICK_CODE = 100
                IMAGE_CAPTURE_CODE = 200
                dummy_img_pass.visibility = View.GONE
                passport_rec.visibility = View.VISIBLE
            }
            R.id.btn_save_insur->{
                this.onBackPressed()
            }
            R.id.img_back_insurence->{
                this.onBackPressed()
            }
        }
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
        imageUri =
            this.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
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
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            if (data!!.clipData != null) {
                val count = data.clipData!!
                    .itemCount
                for (i in 0 until count) {
                    imageUri1 = data.clipData!!.getItemAt(i).uri

                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri1)

                    idorpassportArraylist.add(ImageUploadModel(bitmap))
                }
                val uploadImageAdapter = UploadImagesAdapter(idorpassportArraylist, this, object :
                    RemovePictures {
                    override fun removePictureId(picsCount: Int) {
                        if (picsCount == 0) {
                            dummy_img_pass.visibility = View.VISIBLE
                            passport_rec.visibility = View.GONE
                        }
                    }
                })
                passport_rec.adapter = uploadImageAdapter
            } else {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, data.data)

                idorpassportArraylist.add(ImageUploadModel(bitmap))
                val uploadImageAdapter = UploadImagesAdapter(idorpassportArraylist, this, object :
                    RemovePictures {
                    override fun removePictureId(picsCount: Int) {
                        if (picsCount == 0) {
                            dummy_img_pass.visibility = View.VISIBLE
                            passport_rec.visibility = View.GONE
                        }
                    }
                })
                passport_rec.adapter = uploadImageAdapter
            }

        }
        if (resultCode == Activity.RESULT_OK && requestCode == 200) {


            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)

            idorpassportArraylist.add(ImageUploadModel(bitmap))


            val uploadImageAdapter = UploadImagesAdapter(idorpassportArraylist, this, object :
                RemovePictures {
                override fun removePictureId(picsCount: Int) {
                    if (picsCount == 0) {
                        dummy_img_pass.visibility = View.VISIBLE
                        passport_rec.visibility = View.GONE
                    }
                }
            })
            passport_rec.adapter = uploadImageAdapter
        } else {
            super.onActivityResult(requestCode, resultCode, data)
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
            val i = Intent(this, DriverMainActivity::class.java)
            startActivity(i)
            dialog.dismiss()
        }
        dialog.dismiss()
        dialog.show()
    }

}