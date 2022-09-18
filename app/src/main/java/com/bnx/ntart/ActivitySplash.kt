package com.bnx.ntart

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bnx.ntart.databinding.ActivitySplashBinding
import com.bnx.ntart.utils.Prefs
import com.bnx.ntart.utils.Tools
import com.google.firebase.database.*

class ActivitySplash : AppCompatActivity() {

    private lateinit var prefs: Prefs
    lateinit var binding: ActivitySplashBinding

    // creating a variable for
    // our Firebase Database.
    var firebaseDatabase: FirebaseDatabase? = null

    // creating a variable for our
    // Database Reference for Firebase.
    var databaseReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Tools.setSystemBarColor(this)



        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, ActivityHome::class.java)
            startActivity(intent)
            finish()
        }, 800)
    }


    /*override fun onStart() {
        super.onStart()
        // below line is used to get the instance
        // of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance()

        // below line is used to get
        // reference for our database.
        databaseReference = firebaseDatabase!!.getReference("admobAds")
        getdataFirebase()
    }

    private fun getdataFirebase() {

        // calling add value event listener method
        // for getting the values from database.
        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    if (snapshot.exists()) {
                        Constants.ADMOB_APP_ID =
                            snapshot.child("app_id").getValue(String::class.java)
                        Constants.BANNER_AD_ID =
                            snapshot.child("banner_ad").getValue(String::class.java)
                        Constants.INTER_AD_ID =
                            snapshot.child("interstial_ad").getValue(String::class.java)
                        Constants.NATIVE_AD_ID =
                            snapshot.child("native_ad").getValue(String::class.java)
                        Constants.APP_OPEN_AD_ID =
                            snapshot.child("apopen_ad").getValue(String::class.java)
                        Constants.NATIVE_AD_ITEM_COUNT =
                            snapshot.child("listadCount").getValue(Int::class.java)!!
                        Log.d(
                            "FirebaseData",
                            """

${Constants.ADMOB_APP_ID.toString()}ADMOB_APP_ID
${Constants.BANNER_AD_ID.toString()} BANNER_AD_ID
${Constants.INTER_AD_ID.toString()} INTER_AD_ID
${Constants.NATIVE_AD_ID.toString()} NATIVE_AD_ID
${Constants.APP_OPEN_AD_ID.toString()} APP_OPEN_AD_ID
${Constants.NATIVE_AD_ITEM_COUNT.toString()} NATIVE_AD_ITEM_COUNT"""
                        )
                        try {
                            val applicationInfo = packageManager.getApplicationInfo(
                                packageName, PackageManager.GET_META_DATA
                            )
                            val bundle = applicationInfo.metaData
                            val myApiKey =
                                bundle.getString("com.google.android.gms.ads.APPLICATION_ID")
                            Log.d("APP_ID", "$myApiKey old app id ")
                            bundle.putString(
                                "com.google.android.gms.ads.APPLICATION_ID",
                                Constants.ADMOB_APP_ID
                            )
                            val APP_ID =
                                bundle.getString("com.google.android.gms.ads.APPLICATION_ID")
                            Log.d("APP_ID", APP_ID!!)
                        } catch (e: PackageManager.NameNotFoundException) {
                            Log.e("TAG", "Failed to load meta-data, NameNotFound: " + e.message)
                        } catch (e: NullPointerException) {
                            Log.e("TAG", "Failed to load meta-data, NullPointer: " + e.message)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
            }
        })
    }*/


}