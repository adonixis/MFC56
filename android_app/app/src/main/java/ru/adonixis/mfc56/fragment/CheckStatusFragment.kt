package ru.adonixis.mfc56.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.ktx.Firebase
import ru.adonixis.mfc56.R

class CheckStatusFragment : Fragment() {

    private lateinit var inputDealNumber: TextInputEditText
    private lateinit var inputPinCode: TextInputEditText
    private var progressDialog: ProgressDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_check_status, container, false)
        inputDealNumber = root.findViewById(R.id.input_deal_number)
        inputPinCode = root.findViewById(R.id.input_pin_code)
        val btnCheckStatus = root.findViewById<Button>(R.id.btn_check)
        btnCheckStatus.setOnClickListener { checkStatus() }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(activity)
        progressDialog!!.isIndeterminate = true
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage(getString(R.string.progress_loading))

        progressDialog!!.show()

        progressDialog!!.dismiss()
    }

    private fun checkStatus() {
        var dealNumber = inputDealNumber.text.toString()
        var pinCode = inputPinCode.text.toString()
    }
}