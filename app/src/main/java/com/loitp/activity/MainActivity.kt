package com.loitp.activity

import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.core.base.BaseFontActivity
import com.core.utilities.LImageUtil
import com.core.utilities.LScreenUtil
import com.core.utilities.LSocialUtil
import com.core.utilities.LUIUtil
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.google.android.material.navigation.NavigationView
import com.loitp.R
import com.loitp.fragment.HistoryFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_drawer_end.*
import kotlinx.android.synthetic.main.view_drawer_main.*
import kotlinx.android.synthetic.main.view_drawer_start.view.*

class MainActivity : BaseFontActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun setFullScreen(): Boolean {
        return false
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_main
    }

    override fun setTag(): String? {
        return javaClass.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LUIUtil.createAdBanner(adView)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navViewStart.setNavigationItemSelectedListener(this)
        drawerLayout.useCustomBehavior(Gravity.START)
        drawerLayout.useCustomBehavior(Gravity.END)

        //cover
        LImageUtil.load(context = activity, url = getString(R.string.link_cover), imageView = navViewStart.getHeaderView(0).ivCover)
        showPDF()
        switchHomeScreen()
    }

    private fun showPDF() {
        val inputStream = assets.open("Fossil_Android_TrackMe.pdf")
        pdfView.fromStream(inputStream)
                //.pages(0) // all pages are displayed by default
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .onLoad { nbPages ->
                    logD("loadComplete $nbPages")
                } // called after document is loaded and starts to be rendered
                .onPageChange { page, pageCount ->
                    logD("onPageChange $page/$pageCount")
                }
                .onPageScroll { page, positionOffset ->
                    logD("onPageScrolled $page - $positionOffset")
                }
                .onError { t ->
                    logE("onError $t")
                }
                .onPageError { page, t ->
                    logE("onPageError $page -> $t")
                }
                .onRender { nbPages ->
                    logD("onInitiallyRendered nbPages $nbPages")
                } // called after document is rendered for the first time
                // called on single tap, return true if handled, false to toggle scroll handle visibility
                .onTap { _ ->
                    logD("onTap")
                    false
                }
                .onLongPress { _ ->
                    logD("OnLongPressListener")
                }
                .enableAnnotationRendering(true) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(DefaultScrollHandle(this))
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0)
                .autoSpacing(true) // add dynamic spacing to fit each page on its own on the screen
                .pageFitPolicy(FitPolicy.WIDTH)
                .pageSnap(true) // snap pages to screen boundaries
                .pageFling(true) // make a fling change only a single page like ViewPager
                .nightMode(false) // toggle night parrallaxMode
                .load()
    }

    private fun switchHomeScreen() {
        navViewStart.menu.performIdentifierAction(R.id.navHome, 0)
        navViewStart.menu.findItem(R.id.navHome).isChecked = true
    }

    public override fun onPause() {
        adView.pause()
        super.onPause()
    }

    public override fun onResume() {
        adView.resume()
        super.onResume()
    }

    public override fun onDestroy() {
        adView.destroy()
        super.onDestroy()
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }
            this.doubleBackToExitPressedOnce = true
            showShort(getString(R.string.press_again_to_exit))
            Handler().postDelayed({
                doubleBackToExitPressedOnce = false
            }, 2000)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navHome -> {
                logD("onNavigationItemSelected navHome")
                LScreenUtil.addFragment(activity, R.id.flContainer, HistoryFragment(), false)
            }
            R.id.navRateApp -> {
                LSocialUtil.rateApp(activity = activity, packageName = packageName)
            }
            R.id.navMoreApp -> {
                LSocialUtil.moreApp(activity)
            }
            R.id.navFacebookFanPage -> {
                LSocialUtil.likeFacebookFanpage(activity)
            }
            R.id.navShareApp -> {
                LSocialUtil.shareApp(activity)
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        navViewStart.postDelayed({
            navViewStart.menu.findItem(R.id.navHome).isChecked = true//hight light navViewStart menu home
        }, 500)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_drawer_end, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionRightDrawer -> {
                drawerLayout.openDrawer(GravityCompat.END)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
