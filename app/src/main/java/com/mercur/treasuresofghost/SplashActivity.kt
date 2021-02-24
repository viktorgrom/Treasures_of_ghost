package com.mercur.treasuresofghost

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.preference.PreferenceManager
import com.facebook.applinks.AppLinkData
import kotlinx.android.synthetic.main.activity_splash.*

const val SHARED_PREFS_URL = "SHARED URL"

class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)
        progress_bar.visibility = View.VISIBLE

        if (getOk())
        {
            startActivity(Intent(this, WebActivity::class.java))
            finish()
        }
        else{
            getFBDeep()
        }

    }


    private fun getFBDeep() {
        AppLinkData.fetchDeferredAppLinkData(this) {
            if (it != null) {
                val getIt = it.targetUri.toString()
                Log.e("Facebook", "deep link $getIt")
                Log.e("Success", "FB: $getIt")
                setDepL(unHideToLog(getIt.substringAfter("app://")))
                setOk(true)
                startActivity(Intent(this, WebActivity::class.java))
                finish()
            } else {
                Log.e("Facebook" , "deep link NULL")
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    fun unHideToLog(str: String): String {
        var str = str
        str = str.replace("-", "")
        var result = ""
        var i = 0
        while (i < str.length) {
            val hex = str.substring(i + 1, i + 3)
            result += (hex.toInt(16) xor str[i].toString().toInt()).toChar()
            i += 3
        }
        Log.i("UNHIDE", "OK $result")
        return  result
    }


    private fun setDepL(valu: String) {
        val editor = PreferenceManager.getDefaultSharedPreferences(this).edit()
        editor.putString("DEPL", valu)
        editor.apply()
    }

    fun setOk(value: Boolean) {
        val editor = PreferenceManager.getDefaultSharedPreferences(this).edit()
        editor.putBoolean("ok", value)
        editor.apply()
    }

    private fun getOk() =
            PreferenceManager.getDefaultSharedPreferences(applicationContext).getBoolean("ok", false)


}