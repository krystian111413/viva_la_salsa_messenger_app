package com.zerter.teamconnect.AddContact

import android.app.Activity
import android.app.DialogFragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.gson.Gson
import com.zerter.teamconnect.Controlers.Data
import com.zerter.teamconnect.Controlers.MyTextView
import com.zerter.teamconnect.Models.Group
import com.zerter.teamconnect.R
import kotlinx.android.synthetic.main.select_group_to_add_to_contact.*
import java.util.*

/**
 * Dialog fragment for add contact to group in add contact view
 */
class DialogFragmentToAddContactToGroup: DialogFragment() {


    var parentClass: AddContactFragment? = null

    val onResult: OnResult? = null

    internal var groups: MutableList<Group> = ArrayList()


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.select_group_to_add_to_contact,container,false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Data(activity).groups == null) {
            Toast.makeText(activity, R.string.No_groups_error, Toast.LENGTH_LONG).show()
            dismiss()
        }


    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()


        buttonAddContactToGroups.setOnClickListener(View.OnClickListener {

            val intent : Intent = activity.intent
            intent.putExtra("groups",Gson().toJson(groups))
            targetFragment.onActivityResult(targetRequestCode,Activity.RESULT_OK,intent)

//            AddContact().setAdapter(groups)
            dismiss()

        })
    }


    internal fun setAdapter() {
        if (Data(activity).groups != null) {
            val adapter = ListAdapterGroups(activity, Data(activity).groups)
            listViewGroupToAdd.adapter = adapter
        } else {
            Toast.makeText(activity, R.string.No_groups_error, Toast.LENGTH_LONG).show()
            dismiss()
        }
    }

    private inner class ListAdapterGroups(internal var context: Context, groupList: List<Group>) : ArrayAdapter<Group>(context, 0, groupList) {

        internal var viewList: MutableList<View> = ArrayList()


        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val convertView: View?
            val grupa = getItem(position)
            val inflater = activity.layoutInflater
            convertView = inflater.inflate(R.layout.grupa_kontaktow_to_select, null)

            if (viewList.size > position && viewList[position] != null) {
                return viewList[position]
            }
            val name = convertView!!.findViewById<MyTextView>(R.id.myTextViewContacts) as MyTextView
            if (grupa != null) {
                name.text = grupa.name
            } else {
                name.text = "n/a"
            }
            convertView.setOnClickListener {
                if (name.currentTextColor == getContext().resources.getColor(R.color.textSelected)) {
                    name.setTextColor(getContext().resources.getColor(R.color.colorPrimary))

                    val newListGroup = ArrayList<Group>()
                    for (group in groups) {
                        if (group.name != grupa!!.name) {
                            newListGroup.add(group)
                        }
                    }
                    groups = newListGroup
                } else {
                    name.setTextColor(getContext().resources.getColor(R.color.textSelected))
                    groups.add(grupa)
                }
            }
            viewList.add(convertView)
            return convertView
        }
    }
}