package client.petmooby.com.br.petmooby.actvity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.*
import android.widget.TextView
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.extensions.setupToolbar
import client.petmooby.com.br.petmooby.model.CollectionsName
import client.petmooby.com.br.petmooby.model.VeterinaryTip
import client.petmooby.com.br.petmooby.util.PicassoUtil
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_veterinary_partner_detail.*
import kotlinx.android.synthetic.main.content_veterinary_partner_detail.*

class VeterinaryPartnerDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_veterinary_partner_detail)
        setSupportActionBar(toolbar)

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
        progressVetList.visibility = VISIBLE
        ivVetDetailProfile.visibility = GONE
        PicassoUtil.build(vetTip.photo!!, ivVetDetailProfile,context = this)
        toolbar.findViewById<TextView>(R.id.toolbarTitle).text = vetTip.name
        setupToolbar(R.id.toolbar,"")

    }

}
