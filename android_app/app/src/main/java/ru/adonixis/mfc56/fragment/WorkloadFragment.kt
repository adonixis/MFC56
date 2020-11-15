package ru.adonixis.mfc56.fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.todorant.android.util.AlertUtils
import ru.adonixis.mfc56.R
import ru.adonixis.mfc56.model.*
import ru.adonixis.mfc56.viewmodel.WorkloadViewModel
import java.lang.StringBuilder
import java.util.*


class WorkloadFragment : Fragment() {

    private val viewModel: WorkloadViewModel by viewModels()
    private var progressDialog: ProgressDialog? = null
    private var rootView: View? = null
    private var oktmoObjects: MutableList<OktmoObject> = mutableListOf()
    private var oktmoUnits: MutableList<OktmoUnit> = mutableListOf()

    private var oktmoId: Int? = null
    private var unitId: Int? = null

    private lateinit var tvApplicantsCount: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_workload, container, false)
        rootView = root.findViewById(R.id.root_view)
        tvApplicantsCount = root.findViewById(R.id.tv_applicants_count)
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
        viewModel.getUnitsLiveData().observe(viewLifecycleOwner, Observer { showUnits(it) })
        viewModel.getCountVisitorsLiveData().observe(viewLifecycleOwner, Observer { showCountVisitors(it) })
        viewModel.getErrorMessageLiveData().observe(viewLifecycleOwner, Observer { showError(it) })

        viewModel.getOktmoObjects()
        progressDialog!!.show()
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
        dialog.setTitle(getString(R.string.title_oktmo))
        val types: Array<String> = listOktmo.toTypedArray()
        dialog.setItems(types) { dialog, which ->
            dialog.dismiss()
            oktmoId = oktmoObjects[which].id
            viewModel.getUnits(oktmoId!!)
            progressDialog!!.show()
        }
        dialog.show()
    }

    private fun showUnits(it: UnitsResponse) {
        progressDialog!!.dismiss()
        oktmoUnits.clear()
        oktmoUnits.addAll(it.embeddedUnits.oktmoUnits)

        val listUnits: MutableList<String> = mutableListOf()
        for (item in oktmoUnits) {
            listUnits.add(item.shortName!!)
        }

        val dialog: AlertDialog.Builder = AlertDialog.Builder(context)
        dialog.setTitle(getString(R.string.title_units_oktmo))
        val types: Array<String> = listUnits.toTypedArray()
        dialog.setItems(types) { dialog, which ->
            dialog.dismiss()
            unitId = oktmoUnits[which].id
            progressDialog!!.show()
            viewModel.getCountVisitors(unitId!!)
        }
        dialog.show()
    }

    private fun showCountVisitors(it: CountVisitorsResponse) {
        progressDialog!!.dismiss()

        val sb = StringBuilder()
        sb.append("Количество людей в офисе МФЦ:")
        sb.append('\n')
        sb.append('\n')
        for (item in it.reportCountVisitorsData) {
            sb.append(item.serviceName)
            sb.append(" — ")
            sb.append(item.applicantsCount)
            sb.append('\n')
        }

        tvApplicantsCount.text = sb
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