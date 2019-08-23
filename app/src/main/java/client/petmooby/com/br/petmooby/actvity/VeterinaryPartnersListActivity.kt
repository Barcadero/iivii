package client.petmooby.com.br.petmooby.actvity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.extensions.switchFragment
import client.petmooby.com.br.petmooby.fragment.VeterinaryPartnersListFragment

class VeterinaryPartnersListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_veterinary_partners_list)
        switchFragment(R.id.frameFragmentVetList,VeterinaryPartnersListFragment())
    }
}
