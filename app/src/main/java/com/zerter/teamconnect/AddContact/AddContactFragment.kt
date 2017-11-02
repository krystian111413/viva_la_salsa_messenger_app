package com.zerter.teamconnect.AddContact

import android.app.Activity
import android.app.Fragment
import android.content.ContentProviderOperation
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zerter.teamconnect.Controlers.Data
import com.zerter.teamconnect.Controlers.MyTextView
import com.zerter.teamconnect.Dialogs.DialogFragmentToAddContactToGroup
import com.zerter.teamconnect.Models.Group
import com.zerter.teamconnect.Models.Person
import com.zerter.teamconnect.R
import kotlinx.android.synthetic.main.add_contact_view.*


/**
 * Fragment widoku dodawania kontaktu
 */
class AddContactFragment: Fragment(){
    public var groups: MutableList<Group> = java.util.ArrayList()

    var listView :ListView? = null
    val DIALOG_FRAGMENT = 1
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.add_contact_view,container,false)


        listView =  view.findViewById<ListView>(R.id.listViewGroupsToAddToContact)

        return view;
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addContactButton.setOnClickListener(View.OnClickListener {
            AddContact(contactName.text.toString().trim() + " " + contactSurname.text.toString().trim(),contactNumber.text.toString(),null,null,contactEmail.text.toString(),null,null)
        })

        contactAddGroup.setOnClickListener(View.OnClickListener {
            val manager = fragmentManager
            val dialog = DialogFragmentToAddContactToGroup()
            dialog.setTargetFragment(this,DIALOG_FRAGMENT)
            dialog.show(manager, "DialogFragmentToAddContactToGroup")
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            DIALOG_FRAGMENT ->
                    if (resultCode == Activity.RESULT_OK){
//                        Toast.makeText(activity,"result ok",Toast.LENGTH_SHORT).show()
                        val groups = activity.intent.extras.getString("groups")
                        val type = object : TypeToken<MutableList<Group>>() {
                        }.type
                        val groupList: MutableList<Group> = Gson().fromJson<MutableList<Group>>(groups, type)
                        setAdapter(groupList)
                    } else if (requestCode == Activity.RESULT_CANCELED){
//                        Toast.makeText(activity,"result canceled",Toast.LENGTH_SHORT).show()

                    }

        }
    }


    fun setAdapter(groups: MutableList<Group>) {
        if (groups.isNotEmpty() ) {

            this.groups = groups
            val adapter = ListAdapterGroups(activity,groups)
            listView!!.adapter = adapter
        }
    }


    private inner class ListAdapterGroups(context: Activity, groupList: List<Group>) : ArrayAdapter<Group>(context, R.layout.grupa_kontaktow_to_select, groupList) {

        internal var viewList: MutableList<View> = java.util.ArrayList()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

            Log.d("getView", "dziala")
            val view: View?
            val grupa = getItem(position)
            val inflater = activity.layoutInflater
            view = inflater.inflate(R.layout.grupa_kontaktow_selected, null)

            if (viewList.size > position && viewList[position] != null) {
                return viewList[position]
            }
            val name = view!!.findViewById<MyTextView>(R.id.myTextViewContacts)
            if (grupa != null) {
                name.text = grupa.name
            } else {
                name.text = "n/a"
            }

            viewList.add(view)
            return view
        }
    }

    fun AddContact(DisplayName: String, MobileNumber: String, HomeNumber: String?, WorkNumber: String?, emailID: String?, company: String?, jobTitle:String?){

        val ops = ArrayList<ContentProviderOperation>()

        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build())

        //------------------------------------------------------ Names
        if (DisplayName != "") {
            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            DisplayName).build())
        }

        //------------------------------------------------------ Mobile Number
        if (MobileNumber != "") {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build())
        }

        //------------------------------------------------------ Home Numbers
        if (HomeNumber != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, HomeNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                    .build())
        }

        //------------------------------------------------------ Work Numbers
        if (WorkNumber != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, WorkNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                    .build())
        }

        //------------------------------------------------------ Email
        if (emailID != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, emailID)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                    .build())
        }

        //------------------------------------------------------ Organization
        if (company != null && jobTitle != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, company)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, jobTitle)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .build())
        }

        // Asking the Contact provider to create a new contact
        try {
            activity.contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
            Toast.makeText(activity,getString(R.string.Contact_added),Toast.LENGTH_SHORT).show()

            if (groups.isNotEmpty()){

                val groups = this.groups
                val groupsTmp : MutableList<Group> = java.util.ArrayList() // zaktualizowane grupy

                groups.forEach(fun(group: Group){

                    val person = Person()
                    person.name = DisplayName
                    person.number = MobileNumber
                    val groupTmp = group
                    groupTmp.persons.add(person)
                    groupsTmp.add(groupTmp)

                })
                val memoryGropus: MutableList<Group> = Data(activity).groups
                val newListGroup: MutableList<Group> = java.util.ArrayList() // nowa lista zakutualizowana
                for (group in memoryGropus){
                    var update = false
                    for (tmpGroup in groupsTmp){
                        if (tmpGroup.name == group.name) {
                            update = true
                            newListGroup.add(tmpGroup)
                        }
                    }
                    if (!update){
                        newListGroup.add(group)
                    }
                }

                val stringObj = Gson().toJson(newListGroup)
                Log.d("new groups",stringObj)
                Data(activity).setGroups(stringObj)
            }

            clearForms()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(activity, "Exception: " + e.message, Toast.LENGTH_SHORT).show()
        }

    }

    fun clearForms(){
        contactName.text = null
        contactSurname.text = null
        contactNumber.text = null
        contactEmail.text = null
        listView!!.adapter = null
    }

    override fun onResume() {
        super.onResume()
        Log.d("AddContacts", "resume")
        setAdapter(groups)
//        targetFragment.onActivityResult(targetRequestCode,1,)
    }

}