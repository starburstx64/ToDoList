package com.redb.to_dolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


class testActitvity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_actitvity)

        val bundle = Bundle()
        bundle.putString("activeList", "fjdasfj")

        val fragment = AddEditTaskFragment()
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.testActivity, fragment)

    }
}
