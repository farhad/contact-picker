package io.github.farhad.contactpicker

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import java.util.UUID.randomUUID

data class PickedContact(val number: String, val name: String?)

class ContactPicker constructor(val requestCode: Int = randomUUID().clockSequence()) : Fragment() {

    private lateinit var callBack: (PickedContact) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    fun open(callBack: (PickedContact) -> Unit) {
        this.callBack = callBack
        val contactPickerIntent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        activity?.startActivityForResult(contactPickerIntent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == this.requestCode) {
            var cursor: Cursor? = null
            try {
                cursor = data?.data.let { uri ->
                    uri as Uri
                    activity?.contentResolver?.query(uri, null, null, null, null)
                }
                cursor?.let {
                    it.moveToFirst()
                    val phoneNumber = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    val name = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    it.close()
                    callBack(PickedContact(phoneNumber, name))
                }

            } catch (e: Exception) {
                cursor?.close()
            }
        }
    }
}