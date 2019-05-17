package client.petmooby.com.br.petmooby.fragment


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import client.petmooby.com.br.petmooby.LoginActivity
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.actvity.VeterinaryPartnersListActivity
import client.petmooby.com.br.petmooby.extensions.toast
import com.facebook.AccessTokenTracker
import com.facebook.login.LoginManager
import kotlinx.android.synthetic.main.fragment_menu.*
import com.facebook.AccessToken
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton


/**
 * A simple [Fragment] subclass.
 */
class MenuFragment : Fragment() {

    var tokenTrace = object : AccessTokenTracker() {
        override fun onCurrentAccessTokenChanged(oldAccessToken: AccessToken,
                                                 currentAccessToken: AccessToken?) {
            if (currentAccessToken == null) {
                //TODO clear preferences data , call login activity and close the main screen.
                startActivity(Intent(activity,LoginActivity::class.java))
                activity!!.finish()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnMenuLogout.setOnClickListener{logout()}
        btnMenuVeterinaryPartners.setOnClickListener { startActivity(Intent(activity,VeterinaryPartnersListActivity::class.java)) }
        tokenTrace.startTracking()
    }

    private fun logout(){
        activity!!.alert(R.string.Logout, R.string.areYouSure) {
            yesButton { LoginManager.getInstance().logOut() }
            noButton {}
        }.show()
    }
}// Required empty public constructor
