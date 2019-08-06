package contact.picker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.farhad.contactpicker.ContactPicker
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            ContactPicker.create(
                this,
                { text.text = "${it.name}: ${it.number}" },
                { text.text = it.localizedMessage })?.start()
        }
    }
}
