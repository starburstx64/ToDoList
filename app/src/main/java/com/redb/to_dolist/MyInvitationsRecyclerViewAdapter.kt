package com.redb.to_dolist

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.redb.to_dolist.DB.Entidades.InvitacionEntity


import com.redb.to_dolist.InvitationsFragment.OnListFragmentInteractionListener
import com.redb.to_dolist.dummy.DummyContent.DummyItem

import kotlinx.android.synthetic.main.fragment_invitations.view.*
import kotlinx.android.synthetic.main.rv_list_name_holder.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyInvitationsRecyclerViewAdapter(
    private val mValues: List<String>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyInvitationsRecyclerViewAdapter.ViewHolder>() {

    init {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_list_name_holder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mIdView.text = item
        //holder.mContentView.text = item.content

        with(holder.mView) {
            tag = item
            findViewById<ImageButton>(R.id.task_button_confirmar).setOnClickListener {
                // Aceptar Invitatcion
            }

            findViewById<ImageButton>(R.id.task_button_cancel).setOnClickListener {
                // Eliminar Invitacion
            }
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.list_name_textView_nombre
        //val mContentView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + mIdView.text + "'"
        }
    }
}
