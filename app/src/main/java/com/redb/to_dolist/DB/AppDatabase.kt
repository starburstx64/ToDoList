package com.redb.to_dolist.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.redb.to_dolist.DB.DAOs.*
import com.redb.to_dolist.DB.Entidades.*

@Database(
    entities =[
        AplicacionEntity::class,
        InvitacionEntity::class,
        ListaEntity::class,
        TareaEntity::class,
        UsuarioEntity::class
    ],
    version = 1
)

abstract class AppDatabase: RoomDatabase(){

    //AQUI VAN LAS REFERENCIAS A LOS DAOs
    abstract fun getAplicacionDao():AplicacionDao
    abstract fun getInvitacionDao():InvitacionDao
    abstract fun getListaDao():ListaDao
    abstract fun getTareaDao():TareaDao
    abstract fun getUsuarioDao():UsuarioDao

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

            db.execSQL("INSERT INTO Aplicacion(idAplicacion, logedUser) VALUES (0, 'raulhotmailcom')")
            db.execSQL("INSERT INTO Usuario(idUsuario, username, password, selectedAvatar, email) VALUES ('raulhotmailcom', 'Raul', 'notiene', 1, 'raul@hotmail.com')")
            db.execSQL("INSERT INTO Lista(idLista, idUsuario, title, description, creator, " +
                    "creatorName, shared, listIcon, backgroudColor) VALUES ('12345', 'notiene', " +
                    "'Test List', 'Lista de prueba', 'notiene', 'Notiene', 0, 0, 'Green')")

            db.execSQL("INSERT INTO Tarea(idTarea, idLista, title, importance, dueDate, completed, " +
                    "creator, creatorName, creatorIcon, description) VALUES ('1234', '12345', " +
                    "'Padoru', 1, '20190823', 0, 'notiene', 'notiene', 0, 'Tarea de prueba')")

            db.setTransactionSuccessful()
            db.endTransaction()
        }
    }
}