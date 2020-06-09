package com.fluper.seeway.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

import com.fluper.seeway.R
import com.fluper.seeway.activity.LoginActivity

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OtpVerificationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OtpVerificationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var btn_otp_con : Button

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
        val view : View =inflater.inflate(R.layout.fragment_otp_verification, container, false)

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        btn_otp_con = view.findViewById(R.id.btn_otp_con)

        var type : String? = arguments?.getString("type")


        btn_otp_con.setOnClickListener {

            if(type.equals("forgot")){
                val userTypeFragment: Fragment = ResetPassword()
                val transaction = childFragmentManager.beginTransaction()
                transaction.add(R.id.frame_container, userTypeFragment).commit()
            }else {

                val userTypeFragment: Fragment = UserTypeFragment()
                val transaction = childFragmentManager.beginTransaction()
                transaction.add(R.id.frame_container, userTypeFragment).commit()
            }
        }

        return  view
    }


}
