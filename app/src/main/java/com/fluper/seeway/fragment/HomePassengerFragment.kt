package com.fluper.seeway.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.fluper.seeway.R
import com.fluper.seeway.adapter.HomeAddressAdapter
import com.fluper.seeway.model.AddressModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomePassengerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomePassengerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        var view : View =  inflater.inflate(R.layout.activity_home_screen_passenger, container, false)

        initView(view)

        return view
    }

    private fun initView(view: View){

        val recyclerView = view.findViewById(R.id.home_recycler) as RecyclerView


        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)


        val users = ArrayList<AddressModel>()


        users.add(AddressModel("Home", "383 joriseen st sunnyside Pretora", R.drawable.home_file))
        users.add(AddressModel("Work", "580 Paul grugre St. pretoria",R.drawable.work_file))
        users.add(AddressModel("Rosandra", "580 Paul grugre St. pretoria",R.drawable.star_file))




        var  adapter = HomeAddressAdapter(users, activity)
        recyclerView.adapter = adapter
    }
}
