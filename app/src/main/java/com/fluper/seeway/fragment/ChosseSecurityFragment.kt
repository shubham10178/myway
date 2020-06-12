package com.fluper.seeway.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import com.fluper.seeway.R
import com.fluper.seeway.activity.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChosseSecurityFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChosseSecurityFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var txt_skip : TextView
    lateinit var btn_proceed : Button
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

      var  view : View = inflater.inflate(R.layout.fragment_chosse_security, container, false)

        txt_skip = view.findViewById(R.id.txt_skip)
        btn_proceed = view.findViewById(R.id.btn_proceed)

        var profile : String? = arguments?.getString("profile")


        if(profile.equals("driver")){
            show_alert_submit()
        }else {

        }

        txt_skip.setOnClickListener {

            if(profile.equals("driver")){
                val i  = Intent(activity, HomeScreenNavDriverActivity::class.java)
                startActivity(i)
            }else {
                val i  = Intent(activity, NewPassengerNavActivity::class.java)
                startActivity(i)
            }


        }

        btn_proceed.setOnClickListener {

            if(profile.equals("driver")){
                val i  = Intent(activity, HomeScreenNavDriverActivity::class.java)
                startActivity(i)
            }else {
                val i  = Intent(activity, NewPassengerNavActivity::class.java)
                startActivity(i)
            }


        }
        return view
    }

    fun show_alert_submit(){
        val dialog = activity?.let { it1 -> Dialog(it1) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.driver_profile_approval_alert1)
        val txt_msg = dialog?.findViewById<View>(R.id.txt_msg) as TextView
        val btn_no = dialog?.findViewById<View>(R.id.btn_no) as Button
        val btn_yes = dialog?.findViewById<View>(R.id.btn_yes) as Button
        txt_msg.setText(R.string.alert_driver_profile1)
        btn_no.setOnClickListener {
            val i  = Intent(activity,ProfileCreationDriverActivity::class.java)
            startActivity(i)

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
            val i  = Intent(activity,ProfileCreationDriverActivity::class.java)
            startActivity(i)
            dialog?.dismiss() }

        btn_yes.setOnClickListener {

            dialog?.dismiss() }
        dialog?.dismiss()
        dialog?.show()
    }
}
