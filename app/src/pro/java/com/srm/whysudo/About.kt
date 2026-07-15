/*
 * Copyright (c) 2026 tutosrive. All rights reserved.
 *
 * Author: tutosrive
 * GitHub: https://github.com/tutosrive
 *
 * This source code is PROPRIETARY and CONFIDENTIAL.
 * Unauthorized copying, modification, or distribution of this file,
 * via any medium, is strictly prohibited.
 *
 * This software is provided "as is", without warranty of any kind.
 * In no event shall the author be liable for any claim or damages.
 */

package com.srm.whysudo

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import androidx.lifecycle.lifecycleScope
import com.srm.whysudo.enums.DataFileName
import com.srm.whysudo.markdown.MarkdownManager
import com.srm.whysudo.utils.RawDataManager
import com.srm.whysudo.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class About : AppCompatActivity() {
    private lateinit var infoText: TextView
    private lateinit var markmanAbout: MarkdownManager
    private lateinit var rawDatamanAbout: RawDataManager
    private lateinit var footerText: TextView
    private lateinit var titleLicense: TextView
    private lateinit var appVersion: String
    private lateinit var ctnAboutMain: LinearLayout
    private lateinit var ctnLicense: LinearLayout
    private lateinit var licenseView: TextView
    private lateinit var btnGoBack: ImageButton
    private var enableGoBack: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_about)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        @Suppress("SourceLockedOrientationActivity")
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        appVersion = packageManager.getPackageInfo(packageName, 0).versionName.toString()
        markmanAbout = MarkdownManager(this)
        rawDatamanAbout = RawDataManager(this, DataFileName.ABOUT())

        infoText = findViewById<TextView>(R.id.infoText)
        footerText = findViewById<TextView>(R.id.footerAbout)
        ctnAboutMain = findViewById<LinearLayout>(R.id.mainAboutCtn)
        ctnLicense = findViewById<LinearLayout>(R.id.LCtnLicenses)
        licenseView = findViewById<TextView>(R.id.licenseText)
        titleLicense = findViewById<TextView>(R.id.titleLicense)
        btnGoBack = findViewById<ImageButton>(R.id.btnGoBack)

        loadFooterInfo()
        loadInfoAbout()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (enableGoBack) {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                } else {
                    customBack()
                }
            }
        })
    }

    private fun loadFooterInfo() {
        val footerStr: String = getString(R.string.footer_message)
        Utils.setFooterContent(footerText, footerStr, markmanAbout)
    }

    fun ctaClick(view: View) {
        markmanAbout.setMark(
            "¿Por qué habrías de hacerlo?, un click en ¿un título?, ¿enserio?",
            infoText
        )
        lifecycleScope.launch {
            delay(1000)
            loadInfoAbout()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadInfoAbout() {
        var contentFileAbout = getString(R.string.loading_msg)
        markmanAbout.setMark(contentFileAbout, infoText)
        lifecycleScope.launch {
            delay(100)
            contentFileAbout = try {
                rawDatamanAbout.getContentString(rawDatamanAbout.getData(), "en")
            } catch (error: Exception) {
                getString(R.string.try_reload_msg) + error.message.toString()
            }
            contentFileAbout = addVersionToInfo(contentFileAbout)
            markmanAbout.setMark(contentFileAbout, infoText)
        }
    }

    private fun addVersionToInfo(info: String): String {
        var updated: String = info
        if (info.contains("{version}")) updated = info.replace("{version}", appVersion)
        return updated
    }

    fun loadLicense(view: View) {
        val ctx = this
        CoroutineScope(Dispatchers.Main).launch {
            val license = view.contentDescription.toString()
            val content = Utils.readInternalFile(ctx, license)
            showLicense(license, content)
        }
    }

    private fun showLicense(license: String, content: String) {
        toggleShowLicense(true)
        titleLicense.text = Utils.snakeCaseToCapital(license)
        licenseView.text = content
    }

    fun closeLicense(view: View) {
        toggleShowLicense(false)
    }

    fun toggleShowLicense(isShowLicense: Boolean) {
        this.enableGoBack = !isShowLicense
        val mainVisibility = if (isShowLicense) View.GONE else View.VISIBLE
        val licenseVisibility = if (isShowLicense) View.VISIBLE else View.GONE

        Utils.setViewVisibility(ctnAboutMain, mainVisibility)
        Utils.setViewVisibility(btnGoBack, mainVisibility)
        Utils.setViewVisibility(ctnLicense, licenseVisibility)
    }

    private fun customBack() {
        val visibleMain = ctnAboutMain.isGone
        if (visibleMain) {
            toggleShowLicense(false)
        }
    }

    fun close(view: View) {
        finish()
    }
}