package com.fluper.seeway.fragment

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ClipData
import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fluper.seeway.R
import com.fluper.seeway.activity.ProfileCreationDriverActivity
import com.fluper.seeway.adapter.DriverVehicleInfoAdapter
import com.fluper.seeway.adapter.UploadImagesAdapter
import com.fluper.seeway.model.ImageUploadModel
import com.fluper.seeway.model.VehicleInfoModel
import kotlinx.android.synthetic.main.activity_profile_creation_driver.*
import kotlinx.android.synthetic.main.fragment_add_vehicle.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddVehicleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddVehicleFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var img_back_addv : ImageView
    lateinit var vehicle_img_upload : ImageView
    lateinit var car_doc_img_upload : ImageView
    lateinit var dummy_vehicle_img : ImageView
    lateinit var dummy_car_img : ImageView
    lateinit var vehicle_img_rec : RecyclerView
    lateinit var car_doc_rec : RecyclerView
    lateinit var btn_save_addv : Button
    lateinit var rg_relation_vehicle : RadioGroup
    lateinit var ll_vehicle_registration : LinearLayout
    lateinit var etCardDate_driver : AppCompatEditText
    private var IMAGE_PICK_CODE = 100
    private var IMAGE_CAPTURE_CODE = 200
    private val PERMISSION_CODE = 300
    private val PERMISSION_CODE1 = 400
    var image_uri: Uri? = null
    val uvi_arrayList = ArrayList<ImageUploadModel>()
    val ucd_arrayList = ArrayList<ImageUploadModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val  view : View =inflater.inflate(R.layout.fragment_add_vehicle, container, false)
        img_back_addv =view.findViewById(R.id.img_back_addv)
        btn_save_addv =view.findViewById(R.id.btn_save_addv)
        ll_vehicle_registration =view.findViewById(R.id.ll_vehicle_registration)
        etCardDate_driver =view.findViewById(R.id.etCardDate_driver)

        img_back_addv.setOnClickListener {

            requireActivity().onBackPressed()
        }


        btn_save_addv.setOnClickListener {

           val vn_name : String = edt_vn_driver.text.toString().trim()
           val vmn_model_number : String = edt_vmn_driver.text.toString().trim()
            if(!vn_name.isBlank() && !vmn_model_number.isBlank())
            initView(vn_name,vmn_model_number)

            val i  = Intent(activity, ProfileCreationDriverActivity::class.java)
            i.putExtra("vn_name", vn_name)
            i.putExtra("vmn_model_number", vmn_model_number)
            startActivity(i)

        }


        ll_vehicle_registration.setOnClickListener {
            val calendar = Calendar.getInstance()

            val datePickerDialog = activity?.let { it1 ->
                DatePickerDialog(
                    it1,
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
            }

            datePickerDialog?.show()

        }




        img_upload_rec(view)

        return  view
    }


    private fun initView(vn_name : String,  vmn_model_number : String){


        val vehicleInfoList = ArrayList<VehicleInfoModel>()

        vehicleInfoList.add(VehicleInfoModel(vn_name,vmn_model_number))

        var  adapter = DriverVehicleInfoAdapter(vehicleInfoList, activity)

    }


    fun img_upload_rec(view: View){

        vehicle_img_rec = view.findViewById(R.id.vehicle_img_rec)
        car_doc_rec = view.findViewById(R.id.car_doc_rec)
        vehicle_img_upload = view.findViewById(R.id.vehicle_img_upload)
        car_doc_img_upload = view.findViewById(R.id.car_doc_img_upload)
        dummy_vehicle_img = view.findViewById(R.id.dummy_vehicle_img)
        dummy_car_img = view.findViewById(R.id.dummy_car_img)
        rg_relation_vehicle = view.findViewById(R.id.rg_relation_vehicle)

        vehicle_img_rec.setLayoutManager(
            LinearLayoutManager(
                activity,
                LinearLayoutManager.HORIZONTAL,
                true
            )
        )

        car_doc_rec.setLayoutManager(
            LinearLayoutManager(
                activity,
                LinearLayoutManager.HORIZONTAL,
                true
            )
        )


        rg_relation_vehicle.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                when (checkedId) {
                    R.id.radioOwner -> edt_describ.visibility = View.GONE
                    R.id.radioTenant ->  edt_describ.visibility = View.GONE
                    R.id.radioLease ->  edt_describ.visibility = View.GONE
                    R.id.radioOther ->  edt_describ.visibility = View.VISIBLE
                }
            }
        })

        vehicle_img_upload.setOnClickListener(this)

        car_doc_img_upload.setOnClickListener(this)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.vehicle_img_upload -> {
                upload_img()
                IMAGE_PICK_CODE = 100
                IMAGE_CAPTURE_CODE = 200
                dummy_vehicle_img.visibility = View.GONE
                vehicle_img_rec.visibility = View.VISIBLE
            }

            R.id.car_doc_img_upload -> {
                upload_img()
                IMAGE_PICK_CODE = 101
                IMAGE_CAPTURE_CODE = 201
                dummy_car_img.visibility = View.GONE
                car_doc_rec.visibility = View.VISIBLE
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
        image_uri = activity?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
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
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    //   super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            val clipData: ClipData? = data!!.clipData
            if (clipData != null) {
                for (i in 0 until clipData.getItemCount()) {
                    val imageUri: Uri = clipData.getItemAt(i).getUri()
                    //val pickedImage = data!!.data

                    val filePath = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor: Cursor? =
                        activity?.contentResolver?.query(imageUri!!, filePath, null, null, null)
                    cursor?.moveToFirst()
                    val imagePath: String? =
                        cursor?.getColumnIndex(filePath[0])?.let { cursor.getString(it) }

                    val options: BitmapFactory.Options = BitmapFactory.Options()
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888
                    val bitmap: Bitmap = BitmapFactory.decodeFile(imagePath, options)



                    uvi_arrayList.add(ImageUploadModel(bitmap))

                    cursor?.close()

                    val uploadImageAdapter  = UploadImagesAdapter(uvi_arrayList, activity)
                    vehicle_img_rec.adapter = uploadImageAdapter
                }
            } else {
                val uri = data.data
                //val pickedImage = data!!.data

                val filePath = arrayOf(MediaStore.Images.Media.DATA)
                val cursor: Cursor? =
                    activity?.contentResolver?.query(uri!!, filePath, null, null, null)
                cursor?.moveToFirst()
                val imagePath: String? =
                    cursor?.getColumnIndex(filePath[0])?.let { cursor.getString(it) }

                val options: BitmapFactory.Options = BitmapFactory.Options()
                options.inPreferredConfig = Bitmap.Config.ARGB_8888
                val bitmap: Bitmap = BitmapFactory.decodeFile(imagePath, options)



                uvi_arrayList.add(ImageUploadModel(bitmap))

                cursor?.close()

                val uploadImageAdapter  = UploadImagesAdapter(uvi_arrayList, activity)
                vehicle_img_rec.adapter = uploadImageAdapter
            }


        }
        if(resultCode == Activity.RESULT_OK && requestCode == 200){


            val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, image_uri)

            uvi_arrayList.add(ImageUploadModel(bitmap))



            val uploadImageAdapter = UploadImagesAdapter(uvi_arrayList, activity)
            vehicle_img_rec.adapter = uploadImageAdapter
        }

        if (resultCode == Activity.RESULT_OK && requestCode == 101) {

            val clipData: ClipData? = data!!.clipData
            if (clipData != null) {
                for (i in 0 until clipData.getItemCount()) {
                    val imageUri: Uri = clipData.getItemAt(i).getUri()
                    //val pickedImage = data!!.data

                    val filePath = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor: Cursor? =
                        activity?.contentResolver?.query(imageUri!!, filePath, null, null, null)
                    cursor?.moveToFirst()
                    val imagePath: String? =
                        cursor?.getColumnIndex(filePath[0])?.let { cursor.getString(it) }

                    val options: BitmapFactory.Options = BitmapFactory.Options()
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888
                    val bitmap: Bitmap = BitmapFactory.decodeFile(imagePath, options)



                    ucd_arrayList.add(ImageUploadModel(bitmap))

                    cursor?.close()

                    val uploadImageAdapter  = UploadImagesAdapter(ucd_arrayList, activity)
                    car_doc_rec.adapter = uploadImageAdapter
                }
            } else {
                val uri = data.data
                //val pickedImage = data!!.data

                val filePath = arrayOf(MediaStore.Images.Media.DATA)
                val cursor: Cursor? =
                    activity?.contentResolver?.query(uri!!, filePath, null, null, null)
                cursor?.moveToFirst()
                val imagePath: String? =
                    cursor?.getColumnIndex(filePath[0])?.let { cursor.getString(it) }

                val options: BitmapFactory.Options = BitmapFactory.Options()
                options.inPreferredConfig = Bitmap.Config.ARGB_8888
                val bitmap: Bitmap = BitmapFactory.decodeFile(imagePath, options)



                ucd_arrayList.add(ImageUploadModel(bitmap))

                cursor?.close()

                val uploadImageAdapter  = UploadImagesAdapter(ucd_arrayList, activity)
                car_doc_rec.adapter = uploadImageAdapter
            }

        }
        if(resultCode == Activity.RESULT_OK && requestCode == 201){
            val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, image_uri)
            uvi_arrayList.clear()
            ucd_arrayList.add(ImageUploadModel(bitmap))


            val uploadImageAdapter = UploadImagesAdapter(ucd_arrayList, activity)
            car_doc_rec.adapter = uploadImageAdapter
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun upload_img() {
        val dialog = activity.let { it1 -> Dialog(it1!!) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.open_cemera)

        val btn_cemera = dialog.findViewById<Button>(R.id.btn_cemera) as TextView

        val btn_gellery = dialog.findViewById<View>(R.id.btn_gellery) as Button
        btn_gellery.setOnClickListener {
            if (activity?.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
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
                if (activity?.checkSelfPermission(android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    activity?.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
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