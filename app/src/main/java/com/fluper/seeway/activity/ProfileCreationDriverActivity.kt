package com.fluper.seeway.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fluper.seeway.R
import com.fluper.seeway.adapter.UploadImagesAdapter
import com.fluper.seeway.fragment.ChosseSecurityFragment
import com.fluper.seeway.model.ImageUploadModel
import com.google.android.material.textfield.TextInputLayout
import com.rilixtech.CountryCodePicker
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ProfileCreationDriverActivity : AppCompatActivity() {
    private val IMAGE_PICK_CODE = 1000;
    private val PERMISSION_CODE1 = 1000;
    private val IMAGE_CAPTURE_CODE = 1001
    private val IMAGE_CAPTURE_PROFILE = 1002
    var image_uri: Uri? = null
    private val PERMISSION_CODE = 1001;
    lateinit var img_upload : ImageView
    lateinit var vehicle_img_upload : ImageView
    lateinit var car_doc_img_upload : ImageView
    lateinit var btn_save : Button
    var myBitmap: Bitmap? = null
    var picUri: Uri? = null
    lateinit var driving_license_rec : RecyclerView
    lateinit var vehicle_img_rec : RecyclerView
    lateinit var car_doc_rec : RecyclerView
    var ccp: CountryCodePicker? = null
    val users = ArrayList<ImageUploadModel>()
    lateinit var txt_card_expire: TextInputLayout
    lateinit var edt_card_date: EditText
    lateinit var profile_image: ImageView


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_creation_driver)

        upload_recyclerview()



    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun upload_recyclerview(){
         driving_license_rec = findViewById(R.id.driving_license_rec) as RecyclerView
        vehicle_img_rec = findViewById(R.id.vehicle_img_rec) as RecyclerView
        car_doc_rec = findViewById(R.id.car_doc_rec) as RecyclerView
        img_upload = findViewById(R.id.img_upload) as ImageView
        vehicle_img_upload = findViewById(R.id.vehicle_img_upload) as ImageView
        car_doc_img_upload = findViewById(R.id.car_doc_img_upload) as ImageView
        btn_save = findViewById(R.id.btn_save) as Button
        txt_card_expire = findViewById(R.id.txt_card_expire)
        edt_card_date = findViewById(R.id.edt_card_date_driver)
        profile_image = findViewById(R.id.profile_image)
        ccp  = findViewById(R.id.ccp)
        val type = Typeface.createFromAsset(getAssets(),"font/avenir_black.ttf");
        (ccp as CountryCodePicker).setTypeFace(type)

        driving_license_rec.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val layoutManager : GridLayoutManager = GridLayoutManager(this, 2)
        driving_license_rec.setHasFixedSize(true);

        driving_license_rec.setLayoutManager(layoutManager);




        img_upload.setOnClickListener {
            upload_img()
        }

        vehicle_img_upload.setOnClickListener {
            upload_img()
        }
        car_doc_img_upload.setOnClickListener {
            upload_img()
        }

        btn_save.setOnClickListener {

            show_alert_submit()

        }

        profile_image.setOnClickListener {

            profile_image_pick()

        }

        txt_card_expire.setOnClickListener(View.OnClickListener {
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
                    edt_card_date.setText(date)
                },
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
            )

            datePickerDialog.show()
        })

    }


    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_CAPTURE_PROFILE)
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
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_CAPTURE_PROFILE){
            profile_image.setImageURI(data?.data)
        }

        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_CAPTURE_CODE){
            profile_image.setImageURI(data?.data)
        }

        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            val pickedImage = data!!.data

            val filePath =
                arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor? =
                contentResolver.query(pickedImage!!, filePath, null, null, null)
            cursor?.moveToFirst()
            val imagePath: String? = cursor?.getColumnIndex(filePath[0])?.let { cursor.getString(it) }

            val options: BitmapFactory.Options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            val bitmap: Bitmap = BitmapFactory.decodeFile(imagePath, options)


            users.add(ImageUploadModel(bitmap))

            cursor?.close()


            var  adapter = UploadImagesAdapter(users, this)
            driving_license_rec.adapter = adapter

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
    fun upload_img(){
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

    fun show_alert_submit(){
        val dialog = this?.let { it1 -> Dialog(it1) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.driver_profile_alert)



        val txt_msg = dialog.findViewById<View>(R.id.txt_msg) as TextView
        txt_msg.setOnClickListener {
           setFragment(ChosseSecurityFragment())
            dialog.dismiss() }


        dialog?.dismiss()
        dialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun profile_image_pick(){
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

    protected fun setFragment(fragment: Fragment?) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        if (fragment != null) {
            val args = Bundle()
            args.putString("profile", "driver")
            fragment.setArguments(args)
            fragmentTransaction.add(android.R.id.content, fragment)
        }
        fragmentTransaction
            .addToBackStack(null).commit()
    }

}
