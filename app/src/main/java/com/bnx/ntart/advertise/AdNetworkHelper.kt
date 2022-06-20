package com.bnx.ntart.advertise

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.bnx.ntart.BuildConfig
import com.bnx.ntart.data.AppConfig
import com.bnx.ntart.data.GDPR
import com.bnx.ntart.data.SharedPref
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.bnx.ntart.R


class AdNetworkHelper(private val activity: Activity) {

    private val TAG = AdNetworkHelper::class.java.simpleName

    private val sharedPref = SharedPref(activity)

    //Interstitial
    private var adMobInterstitialAd: InterstitialAd? = null


    companion object {
        fun init(context: Context) {
            MobileAds.initialize(context)
            if (BuildConfig.DEBUG) {
                val configuration = RequestConfiguration.Builder()
                    .setTestDeviceIds(listOf("")).build()
                MobileAds.setRequestConfiguration(configuration)
            }
        }
    }

    fun showGDPR() {
        GDPR.updateConsentStatus(activity)
    }

    fun loadBannerAd(enable: Boolean) {
        if (!enable) return
        val adContainer = activity.findViewById<LinearLayout>(R.id.ad_container)
        adContainer.removeAllViews()
        val adRequest = AdRequest.Builder()
            .addNetworkExtrasBundle(AdMobAdapter::class.java, GDPR.getBundleAd(activity)).build()
        adContainer.visibility = View.GONE
        val adView = AdView(activity)
        adView.adUnitId = activity.getString(R.string.ad_admob_banner_unit_id)
        adContainer.addView(adView)
        adView.adSize = getAdmobBannerSize()
        adView.loadAd(adRequest)
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                adContainer.visibility = View.VISIBLE
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
                adContainer.visibility = View.GONE
            }
        }
    }

    fun loadInterstitialAd(enable: Boolean) {
        if (!enable) return
        val adRequest = AdRequest.Builder()
            .addNetworkExtrasBundle(AdMobAdapter::class.java, GDPR.getBundleAd(activity)).build()
        InterstitialAd.load(activity,
            activity.getString(R.string.ad_admob_interstitial_unit_id),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    adMobInterstitialAd = interstitialAd
                    adMobInterstitialAd!!.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                adMobInterstitialAd = null
                                loadInterstitialAd(enable)
                            }

                            override fun onAdShowedFullScreenContent() {
                                Log.d(TAG, "The ad was shown.")
                                sharedPref.setIntersCounter(0)
                            }
                        }
                    Log.i(TAG, "onAdLoaded")
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.i(TAG, loadAdError.message)
                    adMobInterstitialAd = null
                    Log.d(TAG, "Failed load AdMob Interstitial Ad")
                    Handler(Looper.getMainLooper()).postDelayed({
                        loadInterstitialAd(enable)
                    }, 10000)
                }
            })
    }

    fun showInterstitialAd(enable: Boolean): Boolean {
        if (!enable) return false
        val counter: Int = sharedPref.getIntersCounter()
        if (counter > AppConfig.ADS_INTERS_INTERVAL) {
            if (adMobInterstitialAd == null) return false
            adMobInterstitialAd?.show(activity)
            return true
        } else {
            sharedPref.setIntersCounter(sharedPref.getIntersCounter() + 1)
        }
        return false
    }

    private fun getAdmobBannerSize(): AdSize? {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        val display = activity.windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val widthPixels = outMetrics.widthPixels.toFloat()
        val density = outMetrics.density
        val adWidth = (widthPixels / density).toInt()
        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth)
    }
}