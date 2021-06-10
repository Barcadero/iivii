package client.petmooby.com.br.petmooby.fragment


import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import client.petmooby.com.br.petmooby.LoginActivity
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.actvity.MapsActivity
import client.petmooby.com.br.petmooby.actvity.VeterinaryPartnersListActivity
import client.petmooby.com.br.petmooby.extensions.callEmailHost
import client.petmooby.com.br.petmooby.extensions.setupToolbar
import client.petmooby.com.br.petmooby.extensions.showLoadingDialog
import client.petmooby.com.br.petmooby.ui.viewmodel.MenuViewModel
import com.facebook.AccessToken
import com.facebook.AccessTokenTracker
import dagger.hilt.android.AndroidEntryPoint
import hotchemi.android.rate.AppRate
import kotlinx.android.synthetic.main.menu_fragment_content.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton


/**
 * A simple [Fragment] subclass.
 * Email account for heroku: pwd: 88petymoo22&&
 * email: petmooby.heroku@gmail.com
 */
const val URL = "https://petmooby.herokuapp.com"
const val TERMS = "/pages/terms"
@AndroidEntryPoint
class MenuFragment : Fragment() {

    private val menuViewModel : MenuViewModel by viewModels()
    private var dialog : ProgressDialog? = null
    var tokenTrace = object : AccessTokenTracker() {
        override fun onCurrentAccessTokenChanged(oldAccessToken: AccessToken?,
                                                 currentAccessToken: AccessToken?) {
            if (currentAccessToken == null) {
                //TODO clear preferences data , call login activity and close the main screen.
                try {
                    startActivity(Intent(activity, LoginActivity::class.java))
                    activity!!.finish()
                }catch (e:Exception){
                    e.printStackTrace()
                }
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
        setupButtonsEvents()
        initObservers()
        tokenTrace.startTracking()
        setupToolbar(R.id.toolbar,getString(R.string.Menu))
    }

    private fun setupButtonsEvents() {
        btnMenuLogout.setOnClickListener { logout() }
        btnMenuVeterinaryPartners.setOnClickListener { startActivity(Intent(activity, VeterinaryPartnersListActivity::class.java)) }
        btnMenuAppName.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(URL))
            startActivity(browserIntent)
        }
        btnMenuRateApp.setOnClickListener { AppRate.with(activity).showRateDialog(activity) }
        btnMenuTermsOfUse.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(URL + TERMS))
            startActivity(browserIntent)
        }
        btnMenuContactUs.setOnClickListener {
            callEmailHost("contact.petmooby@gmail.com", getString(R.string.emailContatSubject), getString(R.string.emailBody), getString(R.string.emailContactTitle))
        }
        btnMenuMap.setOnClickListener {
            startActivity(Intent(requireContext(), MapsActivity::class.java))
        }
    }

    private fun logout(){
        requireActivity().alert( R.string.logoutMessage,R.string.Logout) {
            yesButton { logoutYesButton() }
            noButton {}
        }.show()
    }

    private fun logoutYesButton(){
        dialog = showLoadingDialog()
        menuViewModel.clear(requireContext())
    }

    private fun initObservers(){
        dialog?.dismiss()
        menuViewModel.clearLiveData.observe(viewLifecycleOwner, Observer { isOK ->
            if(isOK){
                startActivity(Intent(requireActivity(),LoginActivity::class.java))
                requireActivity().finish()
            }
        })
    }
}
