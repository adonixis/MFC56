package ru.adonixis.mfc56.activity

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.view.*
import ru.adonixis.mfc56.R

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private const val IS_LOGIN = "isLogin"
        private const val USER_ID = "userId"
        private const val EMAIL = "email"
        private const val NAME = "name"
        private const val PHOTO_URL = "photoUrl"
        private const val PHONE = "phone"
    }
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var settings: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var userId: String? = ""
    private var email: String? = ""
    private var name: String? = ""
    private var photoUrl: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        if (needLogin()) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            super.onCreate(savedInstanceState)
            finish()
        } else {
            setTheme(R.style.Theme_MFC56_NoActionBar)
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
            val navView: NavigationView = findViewById(R.id.navView)
            val navController = findNavController(R.id.nav_host_fragment)
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.nav_pre_record,
                    R.id.nav_check_status,
                    R.id.nav_workload,
                    R.id.nav_info
                ), drawerLayout
            )
            navView.setupWithNavController(navController)
            contentMain.ivHamburger.setOnClickListener{ drawerLayout.openDrawer(GravityCompat.START); }

            settings = PreferenceManager.getDefaultSharedPreferences(this)
            editor = settings.edit()
            userId = settings.getString(USER_ID, "")
            email = settings.getString(EMAIL, "")
            name = settings.getString(NAME, "")
            photoUrl = settings.getString(PHOTO_URL, "")

            val imageViewAvatar = navView.getHeaderView(0).findViewById<ImageView>(R.id.imageViewAvatar)
            val tvName = navView.getHeaderView(0).findViewById<TextView>(R.id.tvName)
            val tvEmail = navView.getHeaderView(0).findViewById<TextView>(R.id.tvEmail)

            tvName.text = name
            tvEmail.text = email
            Glide
                .with(this)
                .load(Uri.parse(photoUrl))
                .apply(RequestOptions().circleCrop())
                .placeholder(R.drawable.ic_person)
                .into(imageViewAvatar)

            navView.getHeaderView(0).setOnClickListener {
                navController.navigate(R.id.nav_account)
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            btnLogout.setOnClickListener { logOut() }

            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                val token = task.result

                val msg = getString(R.string.msg_token_fmt, token)
                Log.d(TAG, msg)
            })
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun needLogin(): Boolean {
        val settings = PreferenceManager.getDefaultSharedPreferences(this)
        return !settings.getBoolean(IS_LOGIN, false) || settings.getString(USER_ID, "")!!.isEmpty()
    }

    private fun logOut() {
        Firebase.auth.signOut()
        editor.clear()
        editor.commit()
        this.recreate()
    }
}