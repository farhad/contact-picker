package contact.picker

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.ContentProviderOperation
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.GrantPermissionRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @get:Rule
    val activityTestRule = IntentsTestRule(MainActivity::class.java, true, true)

    /**
     * These permission have been added only for testing requirements. The contact-picker library
     * DOES NOT need any contact permissions to operate.
     */
    @get:Rule
    val contactsPermission: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.READ_CONTACTS,
        android.Manifest.permission.WRITE_CONTACTS
    )

    private fun insertNewContact(
        displayName: String,
        phoneNumber: String,
        email: String? = "abc@xyz.com"
    ) {

        val operations = ArrayList<ContentProviderOperation>()

        operations.add(
            ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build()
        )

        operations.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                .withValue(StructuredName.DISPLAY_NAME, displayName)
                .build()
        )

        operations.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
                .withValue(Phone.NUMBER, phoneNumber)
                .withValue(Phone.TYPE, Phone.TYPE_HOME)
                .build()
        )
        operations.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
                .withValue(Email.DATA, email)
                .withValue(Email.TYPE, Email.TYPE_WORK)
                .build()
        )
        activityTestRule.activity.contentResolver.applyBatch(ContactsContract.AUTHORITY, operations)
    }

    private fun getContactUriByName(contactName: String): Uri? {
        val cursor = activityTestRule.activity.contentResolver.query(
            Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        cursor?.let {
            while (cursor.moveToNext()) {
                val id =
                    cursor.getString(cursor.getColumnIndex(Phone._ID))
                val name =
                    cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME))
                if (name == contactName) {
                    return Uri.withAppendedPath(ContactsContract.Data.CONTENT_URI, id)
                }
            }
        }

        return null
    }

    private fun getResultIntent(contactName: String): Intent = Intent().apply {
        this.data = getContactUriByName(contactName)
    }

    @Test
    fun when_called_from_an_activity_it_opens_the_picker_window_and_returns_picked_contact() {
        // Arrange
        val displayName = "Fred"
        val phoneNumber = "+18009585996"

        insertNewContact(displayName, phoneNumber)

        intending(hasAction(Intent.ACTION_PICK)).respondWith(
            ActivityResult(
                Activity.RESULT_OK,
                getResultIntent(displayName)
            )
        )

        // Act
        onView(withId(R.id.button_pick)).perform(click())

        // Assert
        onView(withId(R.id.text)).check(matches(withText("$displayName: $phoneNumber")))
    }
}
