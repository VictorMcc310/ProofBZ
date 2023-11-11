package com.example.proofbz.Main

import android.Manifest
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.proofbz.BuildConfig
import com.example.proofbz.Helper.HelperDialog
import com.example.proofbz.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class LoginActivity : AppCompatActivity() {
    lateinit var googleButton: SignInButton
    private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
    private var showOneTapUI = true
    private lateinit var auth: FirebaseAuth
    var permsRequestCode = 100
    var perms = arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth
        if(!permissionsCheck()){
            askForPermits()
        }
        if(auth.currentUser!=null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        googleButton = findViewById(R.id.sign_in_button)
        googleButton.setSize(SignInButton.SIZE_STANDARD);
        googleButton.setOnClickListener { dialogGoogle() }
    }

    fun dialogGoogle(){

        val gsoptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
           .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        var mGoogleSignInClient = GoogleSignIn.getClient(this, gsoptions);
       // mGoogleSignInClient.signOut()
        //val account = GoogleSignIn.getLastSignedInAccount(this)
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, REQ_ONE_TAP)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_ONE_TAP) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {

            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            println("Signed in successfully, show authenticated UI."+ account.email)

            val firebaseCredential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        Log.d(TAG, "signInWithCredential:success")
                        val user = auth.currentUser
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        HelperDialog(this).dialogMensaje(getString(R.string.titleFaildLogin),getString(R.string.txtFaildLogin))

                    }
                }


        /*} catch (e: ApiException) {
              Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            HelperDialog(this).dialogMensaje(getString(R.string.titleFaildLogin),getString(R.string.txtFaildLogin))
        }*/
    }


    override fun onStart() {
        super.onStart()

        if(auth.currentUser!=null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }



    private fun permissionsCheck(): Boolean {
        var check = false
            if ( checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED )
                check= true
        return check
    }

    private fun askForPermits ()
    {
        requestPermissions(perms, permsRequestCode)
    }

}