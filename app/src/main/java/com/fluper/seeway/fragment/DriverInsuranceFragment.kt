package com.fluper.seeway.fragment

import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fluper.seeway.R
import com.fluper.seeway.activity.NewDriverNavActivity
import com.fluper.seeway.adapter.UploadImagesAdapter
import com.fluper.seeway.model.ImageUploadModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DriverInsuranceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DriverInsuranceFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var img_upload_passport : ImageView
    lateinit var dummy_img_pass : ImageView
    lateinit var img_back_insurence : ImageView
    lateinit var btn_save_insur : Button
    lateinit var passport_rec : RecyclerView
    private var IMAGE_PICK_CODE = 300
    private var IMAGE_CAPTURE_CODE = 400
    private val PERMISSION_CODE = 1
    private val PERMISSION_CODE1 = 2
    var image_uri: Uri? = null
    val idOrpassport_arrayList = ArrayList<ImageUploadModel>()

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
        var  view : View = inflater.inflate(R.layout.fragment_driver_insurance, container, false)

        img_upload_rec(view)



        return view
    }
    fun img_upload_rec(view: View){

        passport_rec = view.findViewById(R.id.passport_rec)

        img_upload_passport = view.findViewById(R.id.img_upload_passport)

        dummy_img_pass = view.findViewById(R.id.dummy_img_pass)
        btn_save_insur = view.findViewById(R.id.btn_save_insur)
        img_back_insurence = view.findViewById(R.id.img_back_insurence)


        passport_rec.setLayoutManager(
            LinearLayoutManager(
                activity,
                LinearLayoutManager.HORIZONTAL,
                true
            )
        )


        img_upload_passport.setOnClickListener(this)

        btn_save_insur.setOnClickListener {
            alert_submit()
          }

        img_back_insurence.setOnClickListener {
            requireActivity().onBackPressed()
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_upload_passport -> {

                upload_img()
                IMAGE_PICK_CODE = 100
                IMAGE_CAPTURE_CODE = 200
                dummy_img_pass.visibility = View.GONE
                passport_rec.visibility = View.VISIBLE
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



                    idOrpassport_arrayList.add(ImageUploadModel(bitmap))

                    cursor?.close()

                    val uploadImageAdapter  = UploadImagesAdapter(idOrpassport_arrayList, activity)
                    passport_rec.adapter = uploadImageAdapter
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



                idOrpassport_arrayList.add(ImageUploadModel(bitmap))

                cursor?.close()

                val uploadImageAdapter  = UploadImagesAdapter(idOrpassport_arrayList, activity)
                passport_rec.adapter = uploadImageAdapter
            }


        }
        if(resultCode == Activity.RESULT_OK && requestCode == 200){


            val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, image_uri)

            idOrpassport_arrayList.add(ImageUploadModel(bitmap))



            val uploadImageAdapter = UploadImagesAdapter(idOrpassport_arrayList, activity)
            passport_rec.adapter = uploadImageAdapter
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


    fun alert_submit(){
        val dialog = activity?.let { it1 -> Dialog(it1) }
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
        val dialog = activity?.let { it1 -> Dialog(it1) }
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
            val i  = Intent(activity,NewDriverNavActivity::class.java)
            startActivity(i)
            dialog?.dismiss() }
        dialog?.dismiss()
        dialog?.show()
    }

}