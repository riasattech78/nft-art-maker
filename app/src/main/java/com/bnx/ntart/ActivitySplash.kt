package com.bnx.ntart

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.bnx.ntart.databinding.ActivitySplashBinding
import com.bnx.ntart.utils.Prefs
import com.bnx.ntart.utils.Tools

class ActivitySplash : AppCompatActivity() {

    private lateinit var prefs: Prefs
    lateinit var binding: ActivitySplashBinding
    var billingClient: BillingClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Tools.setSystemBarColor(this)


        prefs = Prefs(this)

        checkSubscription()

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, ActivityHome::class.java)
            startActivity(intent)
            finish()
        }, 800)
    }


    fun checkSubscription() {
        billingClient = BillingClient.newBuilder(this).enablePendingPurchases()
            .setListener { billingResult: BillingResult?, list: List<Purchase?>? -> }
            .build()
        val finalBillingClient: BillingClient = billingClient as BillingClient
        billingClient!!.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {}
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    finalBillingClient.queryPurchasesAsync(
                        BillingClient.SkuType.SUBS
                    ) { billingResult1: BillingResult, list: List<Purchase>? ->
                        if (billingResult1.responseCode == BillingClient.BillingResponseCode.OK && list != null) {
                            var i = 0
                            for (purchase in list) {
                                if (purchase.skus[i] == "nt_art_6") {
                                    Log.d("SubTest", purchase.toString() + "")
                                    prefs.setPremium(1)
                                } else {
                                    prefs.setPremium(0)
                                }
                                i++
                            }
                        }
                    }
                }
            }
        })
    }

}