package com.redb.to_dolist

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.redb.to_dolist.DB.Entidades.InvitacionEntity


import com.redb.to_dolist.InvitationsFragment.OnListFragmentInteractionListener
import com.redb.to_dolist.Vistas.MenuPrincipalActivity
import com.redb.to_dolist.dummy.DummyContent.DummyItem

import kotlinx.android.synthetic.main.fragment_invitations.view.*
import kotlinx.android.synthetic.main.rv_list_name_holder.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyInvitationsRecyclerViewAdapter(
    private var mRows: MutableList<InvitationsFragment.InvitationRow>,
    private val mListener: OnListFragmentInteractionListener?,
    private val activity: MenuPrincipalActivity
) : RecyclerView.Adapter<MyInvitationsRecyclerViewAdapter.ViewHolder>() {

    fun setData(newList : MutableList<InvitationsFragment.InvitationRow>) {
        mRows = newList
        if (newList.isNotEmpty()) notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_list_name_holder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mRows[position].listTitle + " by " + mRows[position].creatorName
        holder.mIdView.text = item
        //holder.mContentView.text = item.content

        with(holder.mView) {
            tag = item
            findViewById<ImageButton>(R.id.invitation_button_confirmar).setOnClickListener {
                activity.handleInvitation(mRows[position].idInvitation, mRows[position].idList, true)
                removeInvitation(position)
            }

            findViewById<ImageButton>(R.id.invitation_button_cancelar).setOnClickListener {
                activity.handleInvitation(mRows[position].idInvitation, mRows[position].idList, false)
                removeInvitation(position)
            }
        }
    }

    fun removeInvitation(position: Int) {
        mRows.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = mRows.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.list_name_textView_nombre
        //val mContentView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + mIdView.text + "'"
        }
    }
}
