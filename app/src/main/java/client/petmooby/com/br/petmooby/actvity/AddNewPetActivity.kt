package client.petmooby.com.br.petmooby.actvity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.extensions.setupToolbar
import client.petmooby.com.br.petmooby.util.DateMaskTextWatcher
import client.petmooby.com.br.petmooby.util.DateTimePickerDialog
import kotlinx.android.synthetic.main.activity_add_new_pet.*
import java.util.*

class AddNewPetActivity : AppCompatActivity() {

    var bithDate: Date?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_pet)
        btnNewPetCalender.setOnClickListener {
            bithDate = DateTimePickerDialog.showDatePicker(this,edtNewPetBirthday)
        }
        edtNewPetBirthday.addTextChangedListener(DateMaskTextWatcher(edtNewPetBirthday))
        setupToolbar(R.id.toolbarNewPet, R.string.addPet)
    }

}
