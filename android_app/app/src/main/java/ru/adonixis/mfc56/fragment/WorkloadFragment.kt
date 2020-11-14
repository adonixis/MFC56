package ru.adonixis.mfc56.fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.todorant.android.util.AlertUtils
import ru.adonixis.mfc56.R
import ru.adonixis.mfc56.model.CountVisitorsResponse
import ru.adonixis.mfc56.model.OktmoObject
import ru.adonixis.mfc56.model.OktmoObjectsResponse
import ru.adonixis.mfc56.viewmodel.WorkloadViewModel
import java.util.*


class WorkloadFragment : Fragment() {

    private val viewModel: WorkloadViewModel by viewModels()
    private var progressDialog: ProgressDialog? = null
    private var rootView: View? = null
    private var oktmoObjects: MutableList<OktmoObject> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_workload, container, false)
        rootView = root.findViewById(R.id.root_view)
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

        viewModel.getOktmoObjectsLiveData().observe(viewLifecycleOwner, Observer { showOktmoObjects(it) })
        viewModel.getCountVisitorsLiveData().observe(viewLifecycleOwner, Observer { showCountVisitors(it) })
        viewModel.getErrorMessageLiveData().observe(viewLifecycleOwner, Observer { showError(it) })

        viewModel.getOktmoObjects()
        progressDialog!!.show()
    }

    private fun showCountVisitors(it: CountVisitorsResponse) {
        progressDialog!!.dismiss()

    }

    private fun showOktmoObjects(it: OktmoObjectsResponse) {
        progressDialog!!.dismiss()
        oktmoObjects.clear()
        oktmoObjects.addAll(it.embedded.oktmoObjects)

        val listOktmo: MutableList<String> = mutableListOf()
        for (item in it.embedded.oktmoObjects) {
            listOktmo.add(item.name!!)
        }

        val dialog: AlertDialog.Builder = AlertDialog.Builder(context)
        dialog.setTitle("Справочник ОКТМО")
        val types: Array<String> = listOktmo.toTypedArray()
        dialog.setItems(types) { dialog, which ->
            dialog.dismiss()
            val unitId = oktmoObjects[which].id
            viewModel.getCountVisitors(unitId!!)
            progressDialog!!.show()
        }
        dialog.show()
    }

    private fun showError(error: String) {
        progressDialog!!.dismiss()
        AlertUtils.showSnackbar(
            rootView!!, Snackbar.Callback(),
            ContextCompat.getColor(requireContext(), R.color.red),
            Color.WHITE,
            error,
            Color.WHITE,
            getString(R.string.snackbar_action_hide), null
        )
    }

}