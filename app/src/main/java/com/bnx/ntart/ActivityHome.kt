package com.bnx.ntart

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.bnx.ntart.advertise.AdNetworkHelper
import com.bnx.ntart.creator.activities.canvas.CanvasActivity
import com.bnx.ntart.creator.database.AppData
import com.bnx.ntart.creator.utility.FileHelperUtilities
import com.bnx.ntart.data.AppConfig
import com.bnx.ntart.data.SharedPref
import com.bnx.ntart.data.StringConstants
import com.bnx.ntart.databinding.ActivityHomeBinding
import com.bnx.ntart.databinding.DialogCreateProjectBinding
import com.bnx.ntart.fragment.FragmentExported
import com.bnx.ntart.fragment.FragmentProject
import com.bnx.ntart.fragment.FragmentSaved
import com.bnx.ntart.fragment.FragmentSetting
import com.bnx.ntart.model.MenuView
import com.bnx.ntart.utils.Prefs
import com.bnx.ntart.utils.Tools
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlin.math.abs


class ActivityHome : AppCompatActivity() {

    private var menuViews = hashMapOf<Int, MenuView>()
    private var menuSelected: MenuView? = null
    private lateinit var prefs: Prefs

    lateinit var binding: ActivityHomeBinding
    lateinit var sharedPref: SharedPref
    var myActionMenuItem: MenuItem? = null

    companion object{
        var spanCnt: Int = 0

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = SharedPref(this)
        prefs = Prefs(this)

        FirebaseMessaging.getInstance().subscribeToTopic("notification")
        initComponent()
        initToolbar()
        initDrawerMenu()
        onNavigationMenuClick(findViewById(R.id.menu_project))

        if (sharedPref.getFirstLaunch()) {
            ActivityIntro().navigate(this)
        }
        prepareAds()
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            if (!binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }
    }

    private fun initComponent() {

        val menuHome = MenuView(
            R.string.menu_home,
            R.id.menu_project,
            R.id.menu_project_img,
            R.id.frame_container_home,
            FragmentProject.instance()
        )
        val menuExported = MenuView(
            R.string.menu_exported,
            R.id.menu_exported,
            R.id.menu_exported_img,
            R.id.frame_container_exported,
            FragmentExported.instance()
        )
        val menuAdd = MenuView(R.string.menu_add, R.id.menu_add, R.id.menu_add_strip, -1, null)
        val menuSaved = MenuView(
            R.string.menu_saved,
            R.id.menu_saved,
            R.id.menu_saved_img,
            R.id.frame_container_saved,
            FragmentSaved.instance()
        )
        val menuSetting = MenuView(
            R.string.menu_setting,
            R.id.menu_setting,
            R.id.menu_setting_img,
            R.id.frame_container_setting,
            FragmentSetting.instance()
        )

        menuViews[R.id.menu_project] = menuHome
        menuViews[R.id.menu_exported] = menuExported
        menuViews[R.id.menu_add] = menuAdd
        menuViews[R.id.menu_saved] = menuSaved
        menuViews[R.id.menu_setting] = menuSetting

        binding.appbarLayout.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout: AppBarLayout, verticalOffset: Int ->
            val offset = abs(verticalOffset)
            val range = appBarLayout.totalScrollRange
            val heightMax = binding.lytBarContent.height
            if (range <= 0) return@OnOffsetChangedListener;
            val translationY = offset * heightMax / range
            binding.lytBar.translationY = translationY.toFloat()
        })
    }

    fun onNavigationMenuClick(view: View) {
        val menu = menuViews[view.id]
        if (menuSelected?.parent == menu?.parent) return

        if (menu?.parent == R.id.menu_add) {
            if (prefs.premium == 1) {
                showDialogCreate()

            } else {
                startActivity(
                    Intent(this, IpAppPurchaseActivity::class.java)

                )
            }
            return
        }

        val image = findViewById<ImageView>(menu?.image!!)
        image.colorFilter = PorterDuffColorFilter(
            ContextCompat.getColor(this, R.color.primary),
            PorterDuff.Mode.SRC_IN
        )
        val frameLayout = findViewById<FrameLayout>(menu.frame)
        frameLayout.visibility = View.VISIBLE

        binding.toolbar.title = getString(menu.title)

        when {
            supportFragmentManager.findFragmentById(menu.frame) == null -> {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(menu.frame, menu.fragment!!)
                transaction.commit()
            }
            menu.parent == R.id.menu_exported -> {
                (menu.fragment as FragmentExported?)!!.onResume()
            }
            menu.parent == R.id.menu_setting -> {
                (menu.fragment as FragmentSetting?)!!.onResume()
            }
        }
        if (menuSelected != null) {
            (findViewById<View>(menuSelected!!.image) as ImageView).setColorFilter(
                ContextCompat.getColor(
                    this,
                    R.color.ic_soft
                ), PorterDuff.Mode.SRC_IN
            )
            findViewById<View>(menuSelected!!.frame).visibility = View.GONE
        }
        menuSelected = menu
        myActionMenuItem?.collapseActionView()
    }

    private fun initDrawerMenu() {
        binding.include.folderLocation.text = FileHelperUtilities(this).getDirectory()?.absolutePath
        binding.include.buildVersion.text = Tools.getVersionName(this)

        binding.include.lytFolderLocation.setOnClickListener {
            Tools.openFolder(this)
        }

        binding.include.lytIntroduction.setOnClickListener {
            ActivityIntro().navigate(this)
        }
        binding.include.lytAbout.setOnClickListener {
            Tools().showDialogProject(this)
        }
        binding.include.lytTnc.setOnClickListener {
            Tools.directLinkToBrowser(this, getString(R.string.term_and_condition_url))
        }

        binding.include.PremiumUser.setOnClickListener {
            startActivity(Intent(this,IpAppPurchaseActivity::class.java))
        }
        binding.include.lytRateApp.setOnClickListener {
            Tools.rateAction(this)
        }
        binding.include.lytMoreApps.setOnClickListener {
            Tools.directLinkToBrowser(this, getString(R.string.more_app_url))
        }
        binding.include.lytContactUs.setOnClickListener {
            Tools.directLinkToBrowser(this, getString(R.string.contact_us_url))
        }
    }

    fun showDialogCreate() {
        var bindingView = DialogCreateProjectBinding.inflate(layoutInflater)
        val dialog = Dialog(this)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var count: Int = AppData.pixelArtDB.pixelArtCreationsDao().countPixelArt()
        count++
        bindingView.name.setText("Untitled-" + count)
        bindingView.dimen.setText("12")
        dialog.setCancelable(true)
        dialog.setContentView(bindingView.root)
        bindingView.dimen.visibility = View.GONE
        val radios = arrayOf(
            bindingView.radio12x,
            bindingView.radio16x,
            bindingView.radio24x,
            bindingView.radio32x,
            bindingView.radio48x,
            bindingView.radio64x,
            bindingView.radioCustom
        )
        bindingView.radio12x.isChecked = true;
        var spanCount: Int = 12
        var selectedRadio = bindingView.radio12x
        for (r in radios) {
            r.setOnClickListener {
                selectedRadio.isChecked = false
                selectedRadio = r
                spanCount = Integer.parseInt(r.tag.toString())
                spanCnt=spanCount
                if (r == bindingView.radioCustom) {
                    bindingView.dimen.visibility = View.VISIBLE
                } else {
                    bindingView.dimen.visibility = View.GONE
                }
            }

        }
        bindingView.btnCreate.setOnClickListener {
            if (selectedRadio == bindingView.radioCustom) {
                var countStr: String = bindingView.dimen.text.toString()
                if (!Tools.isNumber(countStr)) return@setOnClickListener
                spanCount = Integer.parseInt(countStr)
                spanCnt=spanCount;
            }
            dialog.dismiss()
            if (prefs.premium == 1) {
                startActivity(
                    Intent(this, CanvasActivity::class.java)
                        .putExtra(StringConstants.SPAN_COUNT_EXTRA, spanCount)
                        .putExtra(
                            StringConstants.PROJECT_TITLE_EXTRA,
                            bindingView.name.text.toString())
                )
            } else {
                startActivity(
                    Intent(this, IpAppPurchaseActivity::class.java)

                )
            }

        }
        dialog.show()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_home, menu)

        myActionMenuItem = menu?.findItem(R.id.action_search)
        var searchView = myActionMenuItem?.actionView as SearchView
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                (menuSelected?.fragment as FragmentProject).initComponent(query)
                return true
            }

            override fun onQueryTextChange(s: String): Boolean {
                return false
            }
        })
        myActionMenuItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                if (menuSelected?.parent != R.id.menu_project) onNavigationMenuClick(findViewById(R.id.menu_project))
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                if (menuSelected?.parent == R.id.menu_project && myActionMenuItem!!.isActionViewExpanded) {
                    (menuSelected?.fragment as FragmentProject).initComponent("")
                }
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    private lateinit var adNetworkHelper: AdNetworkHelper

    private fun prepareAds() {
        adNetworkHelper = AdNetworkHelper(this)
        adNetworkHelper.showGDPR()
        adNetworkHelper.loadBannerAd(AppConfig.ADS_MAIN_BANNER)
        adNetworkHelper.loadInterstitialAd(AppConfig.ADS_MAIN_INTERS)
    }

    fun showInterstitialAd(): Boolean {
        return adNetworkHelper.showInterstitialAd(AppConfig.ADS_MAIN_INTERS)
    }

    override fun onBackPressed() {
        doExitApp()
    }

    private var exitTime: Long = 0

    fun doExitApp() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(this, R.string.press_again_exit_app, Toast.LENGTH_SHORT).show()
            exitTime = System.currentTimeMillis()
        } else {
            finish()
        }
    }
}