package com.fluper.seeway.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

    companion object {
        var TAG = ListFragment::class.java.simpleName
        const val ARG_POSITION: String = "positioin"

        fun newInstance(): ListFragment {
            var fragment = ListFragment();
            val args = Bundle()
            args.putInt(ARG_POSITION, 1)
            fragment.arguments = args
            return fragment
        }
    }

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


        val users = ArrayList<UserTypeModel>()

        //adding some dummy data to the list
        users.add(UserTypeModel("Passenger", R.drawable.walk_3))
        users.add(UserTypeModel("Driver", R.drawable.driver2x))
        users.add(UserTypeModel("Tenant", R.drawable.tenant2x))
        users.add(UserTypeModel("Renter", R.drawable.walk_2))
        users.add(UserTypeModel("Parcle send or Receive", R.drawable.parcel_send_receive3x))
        users.add(UserTypeModel("Delivery Boy", R.drawable.deliveryboy2x))
        users.add(UserTypeModel("User/ Master User", R.drawable.user_master_user2x))


        val adapter = ListAdapter(users)


        recyclerView.adapter = adapter
    }


}
