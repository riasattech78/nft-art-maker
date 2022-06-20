package com.bnx.ntart.creator.activities.canvas

import com.bnx.ntart.advertise.AdNetworkHelper
import com.bnx.ntart.data.AppConfig

fun CanvasActivity.prepareAds() {
    val adNetworkHelper = AdNetworkHelper(this)
    adNetworkHelper.showGDPR()
    adNetworkHelper.loadBannerAd(AppConfig.ADS_CREATOR_BANNER)
}