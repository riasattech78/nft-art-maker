package com.bnx.ntart.creator.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bnx.ntart.creator.converters.JsonConverter
import com.bnx.ntart.creator.dao.ColorPalettesDao
import com.bnx.ntart.creator.models.ColorPalette
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

@Database(entities = [ColorPalette::class], version = 3)
abstract class ColorPalettesDatabase: RoomDatabase() {
    abstract fun colorPalettesDao(): ColorPalettesDao
    companion object {
        private var instance: ColorPalettesDatabase? = null
        fun getDatabase(context: Context): ColorPalettesDatabase {
            if (instance == null) {
                synchronized(ColorPalettesDatabase::class) {
                    if (instance == null) instance = Room.databaseBuilder(context.applicationContext, ColorPalettesDatabase::class.java, AppData.colorPalettesDBFileName).fallbackToDestructiveMigration().addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Executors.newSingleThreadExecutor().execute {
                                CoroutineScope(Dispatchers.IO).launch {
                                    instance?.colorPalettesDao()?.insertColorPalette(ColorPalette("Default", JsonConverter.convertListOfIntToJsonString(ColorDatabase.toList()), true))
                                }
                            }
                        }
                    }).allowMainThreadQueries().build()
                }
            }
            return instance!!
        }
    }
}