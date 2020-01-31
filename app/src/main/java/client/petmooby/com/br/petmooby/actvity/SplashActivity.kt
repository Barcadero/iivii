package client.petmooby.com.br.petmooby.actvity


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import client.petmooby.com.br.petmooby.LoginActivity
import client.petmooby.com.br.petmooby.R


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val handle = Handler()
        handle.postDelayed({ startLoginActivity() }, 2500)
    }

    private fun startLoginActivity() {
        val intent = Intent(this@SplashActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
