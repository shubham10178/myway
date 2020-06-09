package com.fluper.seeway.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.fluper.seeway.Interface.ListAdapter
import com.fluper.seeway.R
import com.fluper.seeway.model.UserTypeModel


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserTypeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserTypeFragment : Fragment() {
    protected lateinit var rootView: View
    lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateComponent()
    }

    private fun onCreateComponent() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_user_type, container, false);
        initView(rootView)
        return rootView
    }

    private fun initView(view: View){

        val recyclerView = view.findViewById(R.id.recyclerView) as RecyclerView


        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

       val layoutManager : GridLayoutManager = GridLayoutManager(activity, 2)
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(layoutManager);
        val smoothScroller: SmoothScroller = object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }

        }
        smoothScroller.setTargetPosition(0);
        layoutManager.startSmoothScroll(smoothScroller)


        val users = ArrayList<UserTypeModel>()


        users.add(UserTypeModel("Passenger", R.drawable.passenger))
        users.add(UserTypeModel("Driver", R.drawable.driver2x))
        users.add(UserTypeModel("Tenant", R.drawable.tenant2x))
        users.add(UserTypeModel("Renter", R.drawable.renter))
        users.add(UserTypeModel("Parcel Send or Receive", R.drawable.parcel_send_receive3x))
        users.add(UserTypeModel("Delivery Boy", R.drawable.deliveryboy2x))
        users.add(UserTypeModel("User/ Master User", R.drawable.user_master_user2x))


     //   var adapter = ListAdapter(users)

      var  adapter = ListAdapter(users, activity)
        recyclerView.adapter = adapter
    }


}
