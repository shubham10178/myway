package com.fluper.seeway.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

import com.fluper.seeway.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EmailRegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EmailRegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var edt_verify_email : EditText
    lateinit var btn_continue : Button
    lateinit var img_back : ImageView

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

        val view : View =inflater.inflate(R.layout.fragment_email_register, container, false)


        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        edt_verify_email =view.findViewById(R.id.edt_verify_email)

        btn_continue = view.findViewById(R.id.btn_continue)
        img_back = view.findViewById(R.id.img_back)


        btn_continue.setOnClickListener {
            val email: String = edt_verify_email.text.toString()
            if(email.trim().length>0) {


                val otpVerificationFragment: Fragment = OtpVerificationFragment()
                val args = Bundle()
                args.putString("type", "emailfragment")
                otpVerificationFragment.setArguments(args)
                val transaction = childFragmentManager.beginTransaction()
                transaction.replace(R.id.frame_container, otpVerificationFragment).commit()

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
