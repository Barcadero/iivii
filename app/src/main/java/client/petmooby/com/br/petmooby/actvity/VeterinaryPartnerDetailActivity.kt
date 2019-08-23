package client.petmooby.com.br.petmooby.actvity

//import kotlinx.android.synthetic.main.activity_veterinary_partner_detail.*
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.extensions.setupToolbar
import client.petmooby.com.br.petmooby.model.CollectionsName
import client.petmooby.com.br.petmooby.model.VeterinaryTip
import client.petmooby.com.br.petmooby.util.PicassoUtil
import kotlinx.android.synthetic.main.activity_veterinary_partner_detail.*
import kotlinx.android.synthetic.main.content_veterinary_partner_detail.*


class VeterinaryPartnerDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_veterinary_partner_detail)
        setSupportActionBar(toolbarVetPartner as Toolbar)

        retrieveVetFromBundle()


    }

    private fun retrieveVetFromBundle() {
        var vetTip              = intent.getSerializableExtra(CollectionsName.VET_TIP) as VeterinaryTip
        tvVetDetailAbout.text   = if(vetTip.about != null) vetTip.about!!.PT_BR else ""
        tvVetDetailAddress.text = vetTip.address
        tvVetDetailClinic.text  = vetTip.clinic
        tvVetDetailEmail.text   = vetTip.email
        tvVetDetailPhone.text   = if(vetTip.contact == null)"" else vetTip.contact
        tvVetDetailName.text    = vetTip.name
        PicassoUtil.build(vetTip.photo!!, ivVetDetailProfile,context = this)
        toolbarVetPartner.findViewById<TextView>(R.id.toolbarTitle).text = vetTip.name
        setupToolbar(R.id.toolbar,"")

    }

}
