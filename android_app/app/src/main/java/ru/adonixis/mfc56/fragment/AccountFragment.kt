package ru.adonixis.mfc56.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import ru.adonixis.mfc56.R

class AccountFragment : Fragment() {

    companion object {
        private const val TAG = "AccountFragment"
        private const val USER_ID = "userId"
        private const val EMAIL = "email"
        private const val NAME = "name"
        private const val PHONE = "phone"
        private const val SNILS = "snils"
    }
    private lateinit var settings: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var inputFIO: TextInputEditText
    private lateinit var inputSnils: TextInputEditText
    private lateinit var inputEmail: TextInputEditText
    private lateinit var inputPhone: TextInputEditText

    private var userId: String = ""
    private var email: String = ""
    private var name: String = ""
    private var phone: String = ""
    private var snils: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = PreferenceManager.getDefaultSharedPreferences(activity)
        editor = settings.edit()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_account, container, false)
        inputFIO = root.findViewById(R.id.input_fio)
        inputSnils = root.findViewById(R.id.input_snils)
        inputEmail = root.findViewById(R.id.input_email)
        inputPhone = root.findViewById(R.id.input_phone)
        val btnSave = root.findViewById<Button>(R.id.btn_save)
        btnSave.setOnClickListener { saveInfo() }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = settings.getString(USER_ID, "")!!
        email = settings.getString(EMAIL, "")!!
        name = settings.getString(NAME, "")!!
        phone = settings.getString(PHONE, "")!!
        snils = settings.getString(SNILS, "")!!

        inputFIO.setText(name)
        inputSnils.setText(snils)
        inputEmail.setText(email)
        inputPhone.setText(phone)
    }

    private fun saveInfo() {
        name = inputFIO.text.toString()
        snils = inputSnils.text.toString()
        email = inputEmail.text.toString()
        phone = inputPhone.text.toString()

        editor.putString(EMAIL, email)
        editor.putString(SNILS, snils)
        editor.putString(NAME, name)
        editor.putString(PHONE, phone)

        editor.apply()
    }
}