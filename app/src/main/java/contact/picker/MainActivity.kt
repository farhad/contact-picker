package contact.picker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.farhad.contactpicker.ContactPicker
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_pick.setOnClickListener {

            val contactPicker: ContactPicker? = ContactPicker.create(
                activity = this,
                onContactPicked = { text.text = "${it.name}: ${it.number}" },
                onFailure = { text.text = it.localizedMessage })

            contactPicker?.pick() // call this to open the picker app chooser
        }
    }
}
