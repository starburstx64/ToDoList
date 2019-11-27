package com.redb.to_dolist.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

//@Database(
//    entities =[
//
//    ],
//    version = 1
//)

abstract class AppDatabase: RoomDatabase(){

    //AQUI VAN LAS REFERENCIAS A LOS DAOs


    //Objeto singleton
    companion object {

        private var INSTANCE: AppDatabase? = null

        fun getAppDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase::class.java,
                    "QuizzAppPro.db"
                )
                    .allowMainThreadQueries()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            initializeData(db)
                        }
                    })
                    .build()
            }

            return INSTANCE as AppDatabase
        }

        fun initializeData(db: SupportSQLiteDatabase) {
            db.beginTransaction()



            db.setTransactionSuccessful()
            db.endTransaction()
        }
    }
}