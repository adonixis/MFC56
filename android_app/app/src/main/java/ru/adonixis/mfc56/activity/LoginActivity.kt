package ru.adonixis.mfc56.activity

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.adonixis.mfc56.R

class LoginActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "LoginActivity"
        private const val REQUEST_GOOGLE_AUTH = 1
        private const val IS_LOGIN = "isLogin"
        private const val USER_ID = "userId"
        private const val EMAIL = "email"
        private const val NAME = "name"
        private const val PHOTO_URL = "photoUrl"
        private const val PHONE = "phone"
    }
    private lateinit var callbackManager: CallbackManager
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    onLoginFacebook(loginResult.accessToken)
                }

                override fun onCancel() {}

                override fun onError(exception: FacebookException) {
                    onAuthFailed(exception.message!!)
                }
            })

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = Firebase.auth

        btnLogInFacebook.setOnClickListener { logInFacebook() }
        btnLogInGoogle.setOnClickListener { logInGoogle() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_GOOGLE_AUTH -> {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleGoogleSignInResult(task)
            }
        }
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun logInFacebook() {
        LoginManager.getInstance()
            .logInWithReadPermissions(this, listOf(
                "public_profile",
                "email"
            ))
    }

    private fun logInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, REQUEST_GOOGLE_AUTH)
    }

    private fun onLoginFacebook(token: AccessToken) {
        btnLogInFacebook.isEnabled = false
        handleFacebookAccessToken(token)
        LoginManager.getInstance().logOut()
    }

    private fun onLoginGoogle(token: String) {
        btnLogInGoogle.isEnabled = false
        firebaseAuthWithGoogle(token)
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>?) = GlobalScope.launch(Dispatchers.IO) {
        try {
            val account = completedTask!!.getResult(ApiException::class.java)
            Log.d(TAG, "firebaseAuthWithGoogle:" + account!!.id)
            val idToken = account.idToken
            withContext(Dispatchers.Main) {
                onLoginGoogle(idToken!!)
            }
        } catch (e: ApiException) {
            Log.e(TAG, "googleSignInResult: failed code = " + e.statusCode)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    onAuthSuccess(user!!)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    onAuthSuccess(user!!)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun onAuthFailed(errorMessage: String) {
        btnLogInGoogle.isEnabled = false
        btnLogInFacebook.isEnabled = false
    }

    private fun onAuthSuccess(user: FirebaseUser) {
        btnLogInGoogle.isEnabled = false
        btnLogInFacebook.isEnabled = false
        val settings = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = settings.edit()
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(USER_ID, user.uid)
        editor.putString(EMAIL, user.email)
        editor.putString(NAME, user.displayName)
        editor.putString(PHOTO_URL, user.photoUrl.toString())
        editor.putString(PHONE, user.phoneNumber)
        editor.apply()
        startMainActivity()
    }

}