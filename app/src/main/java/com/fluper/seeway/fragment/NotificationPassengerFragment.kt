package com.fluper.seeway.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.fluper.seeway.R
import com.fluper.seeway.adapter.HomeAddressAdapter
import com.fluper.seeway.adapter.NotificationAdapter
import com.fluper.seeway.model.AddressModel
import com.fluper.seeway.model.NotificationModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NotificationPassengerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotificationPassengerFragment : Fragment() {
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

        var view : View = inflater.inflate(R.layout.fragment_notification_passenger, container, false)

        initView(view)

        return view
    }


    private fun initView(view: View){

        val recyclerView = view.findViewById(R.id.notification_recycler) as RecyclerView
        val img_back = view.findViewById(R.id.img_back) as ImageView


        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        img_back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        val users = ArrayList<NotificationModel>()


        users.add(NotificationModel("Lorem Ipsum is simply dummy text of the printing and typesetting industry", "12:00 pM"))
        users.add(NotificationModel("Lorem Ipsum is simply dummy text of the printing and typesetting industry", "Yesterday at 1:00 Pm"))
        users.add(NotificationModel("Lorem Ipsum is simply dummy text of the printing and typesetting industry", "Friday at 9:50 Am"))
        users.add(NotificationModel("Lorem Ipsum is simply dummy text of the printing and typesetting industry", "26/03/2020 at 4:00 Pm"))
        users.add(NotificationModel("Lorem Ipsum is simply dummy text of the printing and typesetting industry", "25/03/2010 at 3:00 PM"))
        users.add(NotificationModel("Lorem Ipsum is simply dummy text of the printing and typesetting industry", "20/03/2020 at 2:00 PM"))
        users.add(NotificationModel("Lorem Ipsum is simply dummy text of the printing and typesetting industry", "18/03/2020 at 11:00 Am"))
        users.add(NotificationModel("Lorem Ipsum is simply dummy text of the printing and typesetting industry", "15/03/2020 at 12:00 Pm"))




        var  adapter = NotificationAdapter(users, activity)
        recyclerView.adapter = adapter
    }

}
