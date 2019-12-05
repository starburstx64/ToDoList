package com.redb.to_dolist.Vistas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.redb.to_dolist.R

class ActivityList : AppCompatActivity() {

    private var forEdit = false
    private lateinit var idList : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_add_list)

        forEdit = intent.getBooleanExtra("forEdit", false)

        if (forEdit) {
            idList = intent.getStringExtra("idList")!!


        }
    }
}
