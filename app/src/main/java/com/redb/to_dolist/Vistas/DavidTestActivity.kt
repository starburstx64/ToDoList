package com.redb.to_dolist.Vistas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.facebook.stetho.Stetho
import com.redb.to_dolist.R

class DavidTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.david_activity_test)

        Stetho.initializeWithDefaults(this)
    }
}
