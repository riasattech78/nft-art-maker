package com.bnx.ntart.data

import android.R
import android.app.Application
import android.content.Context
import android.util.Log
import com.bnx.ntart.advertise.AdNetworkHelper
import com.bnx.ntart.creator.dao.PixelArtCreationsDao
import com.bnx.ntart.creator.database.AppData
import com.bnx.ntart.creator.database.ColorPalettesDatabase
import com.bnx.ntart.creator.database.PixelArtDatabase
import com.google.android.gms.ads.MobileAds
import papaya.`in`.admobopenads.AppOpenManager


class DataApp : Application() {

    lateinit var sharedPref: SharedPref
    lateinit var dao: PixelArtCreationsDao

    override fun onCreate() {
        super.onCreate()
        sharedPref = SharedPref(this)

        AppData.pixelArtDB = PixelArtDatabase.getDatabase(this)
        AppData.colorPalettesDB = ColorPalettesDatabase.getDatabase(this)

        dao = PixelArtDatabase.getDatabase(this).pixelArtCreationsDao()

        initAdNetwork(this)

        MobileAds.initialize(
            this
        ) { initializationStatus ->
            Log.e(
                "Admob_initialize_status",
                initializationStatus.toString()
            )
        }


        AppOpenManager(this, getString(com.bnx.ntart.R.string.openAds))

    }

    private fun initAdNetwork(context: Context) {
        // Init ADS Admob
        AdNetworkHelper.init(this)
    }

}