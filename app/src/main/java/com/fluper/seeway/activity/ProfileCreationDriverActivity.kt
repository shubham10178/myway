package com.fluper.seeway.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ClipData
import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Typeface
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.Window
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fluper.seeway.R
import com.fluper.seeway.adapter.DriverVehicleInfoAdapter
import com.fluper.seeway.adapter.UploadImagesAdapter
import com.fluper.seeway.fragment.AddVehicleFragment
import com.fluper.seeway.fragment.ChosseSecurityFragment
import com.fluper.seeway.fragment.DriverInsuranceFragment
import com.fluper.seeway.model.ImageUploadModel
import com.fluper.seeway.model.VehicleInfoModel
import com.rilixtech.CountryCodePicker
import kotlinx.android.synthetic.main.activity_profile_creation_driver.*
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ProfileCreationDriverActivity : AppCompatActivity(), View.OnClickListener,
    RadioGroup.OnCheckedChangeListener {

    private var vn_name: String? = null
    private var vmn_model_number: String? = null

    private var IMAGE_PICK_CODE = 1000
    private val PERMISSION_CODE1 = 1000
    private var IMAGE_CAPTURE_CODE = 1005
    private val IMAGE_CAPTURE_PROFILE = 1012
    var image_uri: Uri? = null
    private val PERMISSION_CODE = 1001
    var myBitmap: Bitmap? = null
    var picUri: Uri? = null
    val users = ArrayList<ImageUploadModel>()
    val udli_arrayList = ArrayList<ImageUploadModel>()
    val vehicleList = ArrayList<VehicleInfoModel>()

    val up_arrayList = ArrayList<ImageUploadModel>()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_creation_driver)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        try {
            var pref: SharedPreferences = getBaseContext().getSharedPreferences("User_info", 0)
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
             vn_name = intent.getStringExtra("vn_name")
             vmn_model_number = intent.getStringExtra("vmn_model_number")

            vehicle_info(vn_name,vmn_model_number)
        }



        rg_type_per.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                when (checkedId) {
                    R.id.radio_want_tobe_emp -> img_driver_tobeEmp.visibility = View.VISIBLE
                    R.id.radio_nopermission ->  img_driver_tobeEmp.visibility = View.INVISIBLE
                    R.id.radio_havepermission ->  img_driver_tobeEmp.visibility = View.INVISIBLE
                }
            }
        })

        upload_recyclerview()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun upload_recyclerview() {
        val type = Typeface.createFromAsset(assets, "font/avenir_black.ttf")
        (ccp_driver as CountryCodePicker).setTypeFace(type)

        driving_license_rec.setLayoutManager(
            LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                true
            )
        )

        upload_permission_rec.setLayoutManager(
            LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                true
            )
        )



        img_upload.setOnClickListener(this)

        permission_img_upload.setOnClickListener(this)



        btn_save.setOnClickListener {

            if(vehicleList.isEmpty()){
                alert_vehicle_not_selected()
            }else {
                if (img_driver_tobeEmp.isVisible) {
                    setFragment(DriverInsuranceFragment())
                } else {
                    alert_submit()
                }
            }

        }

        profile_driver_img.setOnClickListener {

            profile_image_pick()

        }

        img_cam_driver.setOnClickListener {
            profile_image_pick()

        }



        rl_driver_Card.setOnClickListener {  val calendar = Calendar.getInstance()

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


        ll_add_vechicle.setOnClickListener {

            var edt_driver_name :String = edt_driver_name.getText().toString()
            var edt_driver_email :String = edt_driver_email.getText().toString()
            var edt_driver_phone_num :String = edt_driver_phone_num.getText().toString()
           var pref : SharedPreferences = getBaseContext().getSharedPreferences("User_info", 0)
            var editor :SharedPreferences.Editor = pref.edit()
            editor.putString("NAME", edt_driver_name)
            editor.putString("EMAIL", edt_driver_email)
            editor.putString("PHONE", edt_driver_phone_num)
            editor.commit();

            setFragment(AddVehicleFragment())
        }


    }

    private fun pickImageFromGallery() {

        //Intent to pick image
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
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
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
                profile_driver_img.setImageURI(data?.data)
            }
        }

        if (resultCode == Activity.RESULT_OK && requestCode == 2009) {
            if(image_uri !=(null)) {
                profile_driver_img.setImageURI(image_uri)
            }
        }

        if (resultCode == Activity.RESULT_OK && requestCode == 1001) {
            try {
                val clipData: ClipData? = data!!.clipData
                if (clipData != null) {
                    for (i in 0 until clipData.getItemCount()) {
                        val imageUri: Uri = clipData.getItemAt(i).getUri()
                        //val pickedImage = data!!.data

                        val filePath = arrayOf(MediaStore.Images.Media.DATA)
                        val cursor: Cursor? =
                            contentResolver.query(imageUri!!, filePath, null, null, null)
                        cursor?.moveToFirst()
                        val imagePath: String? =
                            cursor?.getColumnIndex(filePath[0])?.let { cursor.getString(it) }

                        val options: BitmapFactory.Options = BitmapFactory.Options()
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888
                        val bitmap: Bitmap = BitmapFactory.decodeFile(imagePath, options)



                        udli_arrayList.add(ImageUploadModel(bitmap))

                        cursor?.close()

                        val uploadImageAdapter = UploadImagesAdapter(udli_arrayList, this)
                        driving_license_rec.adapter = uploadImageAdapter
                    }
                } else {
                    val uri = data.data
                    //val pickedImage = data!!.data

                    val filePath = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor: Cursor? =
                        contentResolver.query(uri!!, filePath, null, null, null)
                    cursor?.moveToFirst()
                    val imagePath: String? =
                        cursor?.getColumnIndex(filePath[0])?.let { cursor.getString(it) }

                    val options: BitmapFactory.Options = BitmapFactory.Options()
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888
                    val bitmap: Bitmap = BitmapFactory.decodeFile(imagePath, options)



                    udli_arrayList.add(ImageUploadModel(bitmap))

                    cursor?.close()

                    val uploadImageAdapter = UploadImagesAdapter(udli_arrayList, this)
                    driving_license_rec.adapter = uploadImageAdapter
                }
            } catch (e: Exception) {

            }
        }

        if(resultCode == Activity.RESULT_OK && requestCode == 2001){


            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, image_uri)

            udli_arrayList.add(ImageUploadModel(bitmap))



            val uploadImageAdapter = UploadImagesAdapter(udli_arrayList, this)
            driving_license_rec.adapter = uploadImageAdapter
        }

        if (resultCode == Activity.RESULT_OK && requestCode == 1004) {

            val clipData: ClipData? = data!!.clipData
            if (clipData != null) {
                for (i in 0 until clipData.getItemCount()) {
                    val imageUri: Uri = clipData.getItemAt(i).getUri()
                    //val pickedImage = data!!.data

                    val filePath = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor: Cursor? =
                        contentResolver.query(imageUri!!, filePath, null, null, null)
                    cursor?.moveToFirst()
                    val imagePath: String? =
                        cursor?.getColumnIndex(filePath[0])?.let { cursor.getString(it) }

                    val options: BitmapFactory.Options = BitmapFactory.Options()
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888
                    val bitmap: Bitmap = BitmapFactory.decodeFile(imagePath, options)



                    up_arrayList.add(ImageUploadModel(bitmap))

                    cursor?.close()

                    val uploadImageAdapter  = UploadImagesAdapter(up_arrayList, this)
                    upload_permission_rec.adapter = uploadImageAdapter
                }
            } else {
                val uri = data.data
                //val pickedImage = data!!.data

                val filePath = arrayOf(MediaStore.Images.Media.DATA)
                val cursor: Cursor? =
                    contentResolver.query(uri!!, filePath, null, null, null)
                cursor?.moveToFirst()
                val imagePath: String? =
                    cursor?.getColumnIndex(filePath[0])?.let { cursor.getString(it) }

                val options: BitmapFactory.Options = BitmapFactory.Options()
                options.inPreferredConfig = Bitmap.Config.ARGB_8888
                val bitmap: Bitmap = BitmapFactory.decodeFile(imagePath, options)



                up_arrayList.add(ImageUploadModel(bitmap))

                cursor?.close()

                val uploadImageAdapter  = UploadImagesAdapter(up_arrayList, this)
                upload_permission_rec.adapter = uploadImageAdapter
            }

        }
        if(resultCode == Activity.RESULT_OK && requestCode == 2004){
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, image_uri)

            up_arrayList.add(ImageUploadModel(bitmap))


            val uploadImageAdapter = UploadImagesAdapter(up_arrayList, this)
            upload_permission_rec.adapter = uploadImageAdapter
        }
    }

    @Throws(IOException::class)
    private fun rotateImageIfRequired(img: Bitmap, selectedImage: Uri): Bitmap? {
        val ei = ExifInterface(selectedImage.path)
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
    fun upload_img(driving_license_rec : RecyclerView) {
        val dialog = this.let { it1 -> Dialog(it1) }
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

    fun show_alert_submit() {
        val dialog = this.let { it1 -> Dialog(it1) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_marble32)

        val txt_msg = dialog.findViewById<View>(R.id.txt_msg) as TextView

        txt_msg.setOnClickListener {
            val i  = Intent(this,NewDriverNavActivity::class.java)
            startActivity(i)
            dialog.dismiss()
        }


        dialog.dismiss()
        dialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun profile_image_pick() {
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

    protected fun setFragment(fragment: Fragment?) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        if (fragment != null) {
            val args = Bundle()
            args.putString("profile", "driver")
            fragment.arguments = args
            fragmentTransaction.add(android.R.id.content, fragment)
        }
        fragmentTransaction
            .addToBackStack(null).commit()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.img_upload -> {
                upload_img(driving_license_rec)
                IMAGE_PICK_CODE = 1001
                IMAGE_CAPTURE_CODE = 2001
                dummy_img_dl.visibility = View.GONE
                driving_license_rec.visibility = View.VISIBLE
            }

            R.id.permission_img_upload -> {
                upload_img(upload_permission_rec)
                IMAGE_PICK_CODE = 1004
                IMAGE_CAPTURE_CODE = 2004
                dummy_img_upi.visibility = View.GONE
                upload_permission_rec.visibility = View.VISIBLE
            }


        }
    }


    private fun vehicle_info(vn_name: String?, vmn_model_number: String?){


        vehicle_info_rec.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        vehicleList.add(VehicleInfoModel(vn_name!!, vmn_model_number!!))

        var  adapter = DriverVehicleInfoAdapter(vehicleList, this)
        vehicle_info_rec.adapter = adapter

    }

    fun alert_vehicle_not_selected(){
        val dialog = this?.let { it1 -> Dialog(it1) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.driver_profile_alert)
        val txt_msg = dialog?.findViewById<View>(R.id.txt_msg_alert) as TextView
        val btn_ok = dialog?.findViewById<View>(R.id.btn_ok) as Button
        txt_msg.setText(R.string.vehicle_not_selected)
        btn_ok.setOnClickListener { dialog?.dismiss() }
        dialog?.dismiss()
        dialog?.show()
    }

    fun alert_submit(){
        val dialog = this?.let { it1 -> Dialog(it1) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.driver_profile_approval_alert1)
        val txt_msg = dialog?.findViewById<View>(R.id.txt_msg) as TextView
        val btn_no = dialog?.findViewById<View>(R.id.btn_no) as Button
        val btn_yes = dialog?.findViewById<View>(R.id.btn_yes) as Button
        txt_msg.setText(R.string.alert_driver_profile1)
        btn_no.setOnClickListener {

            dialog?.dismiss() }

        btn_yes.setOnClickListener {

            show_alert_Yes()
            dialog?.dismiss() }
        dialog?.dismiss()
        dialog?.show()
    }
    fun show_alert_Yes(){
        val dialog = this?.let { it1 -> Dialog(it1) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.driver_profile_approval_alert1)

        val txt_msg = dialog?.findViewById<View>(R.id.txt_msg) as TextView
        val btn_no = dialog?.findViewById<View>(R.id.btn_no) as Button
        val btn_yes = dialog?.findViewById<View>(R.id.btn_yes) as Button
        txt_msg.setText(R.string.alert_driver_profile2)

        btn_no.setOnClickListener {

            dialog?.dismiss() }

        btn_yes.setOnClickListener {
            show_alert_submit()

            dialog?.dismiss() }
        dialog?.dismiss()
        dialog?.show()
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
}