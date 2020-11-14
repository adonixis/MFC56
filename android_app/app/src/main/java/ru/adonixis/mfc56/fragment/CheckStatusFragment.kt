package ru.adonixis.mfc56.fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.todorant.android.util.AlertUtils.showSnackbar
import ru.adonixis.mfc56.R
import ru.adonixis.mfc56.model.StatusResponse
import ru.adonixis.mfc56.viewmodel.CheckStatusViewModel


class CheckStatusFragment : Fragment() {

    private val viewModel: CheckStatusViewModel by viewModels()
    private lateinit var inputDealNumber: TextInputEditText
    private lateinit var inputPinCode: TextInputEditText
    private var progressDialog: ProgressDialog? = null
    private var rootView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_check_status, container, false)
        rootView = root.findViewById(R.id.root_view)
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
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getCheckStatusLiveData().observe(viewLifecycleOwner, Observer { showStatus(it) })
        viewModel.getErrorMessageLiveData().observe(viewLifecycleOwner, Observer { showError(it) })
    }

    private fun checkStatus() {
        progressDialog!!.show()

        var dealNumber = inputDealNumber.text.toString().toInt()
        var pinCode = inputPinCode.text.toString().toInt()

        viewModel.checkStatus(dealNumber, pinCode)
    }

    private fun showStatus(it: StatusResponse) {
        progressDialog!!.dismiss()
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.title_status))
            .setMessage(it.name)
            .setPositiveButton(android.R.string.yes
            ) { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showError(error: String) {
        progressDialog!!.dismiss()
        showSnackbar(
            rootView!!, Snackbar.Callback(),
            ContextCompat.getColor(requireContext(), R.color.red),
            Color.WHITE,
            error,
            Color.WHITE,
            getString(R.string.snackbar_action_hide), null
        )
    }

}