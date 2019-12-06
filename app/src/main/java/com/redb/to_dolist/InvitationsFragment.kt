package com.redb.to_dolist

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.redb.to_dolist.DB.AppDatabase
import com.redb.to_dolist.DB.Entidades.InvitacionEntity
import com.redb.to_dolist.Vistas.MenuPrincipalActivity

import com.redb.to_dolist.dummy.DummyContent
import com.redb.to_dolist.dummy.DummyContent.DummyItem

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [InvitationsFragment.OnListFragmentInteractionListener] interface.
 */
class InvitationsFragment : Fragment() {

    data class InvitationRow(val idInvitation : String, val idList : String, val listTitle : String, val creatorName : String)

    // TODO: Customize parameters
    private var columnCount = 1

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_invitations_list, container, false)

        // Set the adapter
        val invitationsRows = mutableListOf<InvitationRow>()

        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyInvitationsRecyclerViewAdapter(invitationsRows, listener, activity as MenuPrincipalActivity)
            }
        }

        val roomDatabase = AppDatabase.getAppDatabase(view.context)
        val fbDatabase = FirebaseDatabase.getInstance()

        val invitationsRef = fbDatabase.getReference("App").child("userInvitations").child(roomDatabase.getAplicacionDao().getLoggedUser()!!)
        invitationsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val id = it.key.toString()
                    val idList = it.child("idList").value.toString()
                    val listTitle = it.child("listTitle").value.toString()
                    var idCreator: String
                    var userName : String

                    if (!it.hasChild("accepted")) {
                        fbDatabase.getReference("App").child("lists").child(idList).child("creator").addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(p0: DataSnapshot) {
                                idCreator = p0.value.toString()

                                fbDatabase.getReference("App").child("users").child(idCreator).child("username").addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(p0: DataSnapshot) {
                                        userName = p0.value.toString()
                                        invitationsRows.add(InvitationRow(id, idList, listTitle, userName))
                                        with(view as RecyclerView) {
                                            (adapter as MyInvitationsRecyclerViewAdapter).setData(invitationsRows)
                                        }
                                    }

                                    override fun onCancelled(p0: DatabaseError) {

                                    }
                                })
                            }

                            override fun onCancelled(p0: DatabaseError) {

                            }
                        })
                    }
                }


            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
           // throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: DummyItem?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            InvitationsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
