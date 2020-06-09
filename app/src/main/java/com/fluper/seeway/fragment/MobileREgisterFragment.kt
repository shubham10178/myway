package com.fluper.seeway.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText

import com.fluper.seeway.R
import com.rilixtech.CountryCodePicker

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MobileREgisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MobileREgisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var btn_continue : Button
    var ccp: CountryCodePicker? = null
     lateinit var edtPhoneNumber: AppCompatEditText
     lateinit var img_back: ImageView

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
        val view : View = inflater.inflate(R.layout.fragment_mobile_r_egister, container, false)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        ccp  = view.findViewById(R.id.ccp)
        edtPhoneNumber = view.findViewById(R.id.phone_number_edt)
        img_back = view.findViewById(R.id.img_back)



        btn_continue = view.findViewById(R.id.btn_continue)


        btn_continue.setOnClickListener {

            val email: String = edtPhoneNumber.text.toString()
            if(email.trim().length>0) {
                val childFragment: Fragment = OtpVerificationFragment()
                val args = Bundle()
                args.putString("type", "mobilefragment")
                childFragment.setArguments(args)
                val transaction = childFragmentManager.beginTransaction()
                transaction.replace(R.id.frame_container, childFragment).commit()
            }else{
                Toast.makeText(activity, "Please enter some message! ", Toast.LENGTH_SHORT).show()
            }


        }


        img_back.setOnClickListener {
            activity!!.onBackPressed()
        }


        return view
    }


}
