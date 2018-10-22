package com.orgzly.android.ui.repos

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import com.orgzly.BuildConfig
import com.orgzly.R
import com.orgzly.android.repos.WebDavClient
import com.orgzly.android.util.LogUtils
import kotlinx.android.synthetic.main.activity_repo_webdav.*
import java.net.URI
import java.net.URL

class WebDavRepoActivity: RepoActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_repo_webdav)

        setupActionBar(R.string.webdav)

        dav_test.setOnClickListener { onTest() }
    }

    fun onTest() {
        val user = dav_user.text.toString()
        val pass = dav_password.text.toString()
        //TODO URL doesn't parse
        //TODO invalid protocol provided
        val url = URL(dav_url.text.toString())
        AsyncTask.execute {
            try {
                WebDavClient(url, user, pass).getAllDocuments()
            } catch (ex: Exception) {
                if (BuildConfig.LOG_DEBUG) {
                    LogUtils.d(TAG, ex)
                }
            }
        }
    }


    companion object {
        private val TAG = WebDavRepoActivity::class.java.name

        private const val ARG_REPO_ID = "repo_id"

        @JvmStatic
        @JvmOverloads
        fun start(activity: Activity, repoId: Long = 0) {
            val intent = Intent(Intent.ACTION_VIEW)
                    .setClass(activity, WebDavRepoActivity::class.java)
                    .putExtra(ARG_REPO_ID, repoId)

            activity.startActivity(intent)
        }
    }
}