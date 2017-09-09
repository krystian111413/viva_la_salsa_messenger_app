package com.kowalski.krystian.vivalasalsamessenger.Kotlin

import android.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kowalski.krystian.vivalasalsamessenger.Data
import com.kowalski.krystian.vivalasalsamessenger.R

/**
 * Przepisana klasa na kotlina
 */
class Message : Fragment() {

    var data: Data? = null
    var tekst2: String = "lol"
    var prawda : Boolean = false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater?.inflate(R.layout.activity_message, container, false)

        return view!!
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        data = Data(activity)

//        pierwsza {  }

    }


    fun pierwsza(callback: (s: String) -> Boolean) : Unit{
        Log.d("pierwsza","pierwsza")

        callback("chuj")
        return
    }

}