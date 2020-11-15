package ru.adonixis.mfc56.fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.todorant.android.util.AlertUtils
import ru.adonixis.mfc56.R
import ru.adonixis.mfc56.model.*
import ru.adonixis.mfc56.viewmodel.PreRecordViewModel


class PreRecordFragment : Fragment() {

    companion object {
        private const val TAG = "PreRecordFragment"
        private const val NAME = "name"
        private const val PHONE = "phone"
        private const val SNILS = "snils"
    }
    private lateinit var settings: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var inputFIO: TextInputEditText
    private lateinit var inputSnils: TextInputEditText
    private lateinit var inputPhone: TextInputEditText

    private var name: String = ""
    private var phone: String = ""
    private var snils: String = ""

    private val viewModel: PreRecordViewModel by viewModels()
    private var progressDialog: ProgressDialog? = null
    private var rootView: View? = null
    private var oktmoObjects: MutableList<OktmoObject> = mutableListOf()
    private var oktmoUnits: MutableList<OktmoUnit> = mutableListOf()
    private var services: MutableList<ServiceResponse> = mutableListOf()
    private var bookingDates: MutableList<BookingDate> = mutableListOf()

    private var oktmoId: Int? = null
    private var unitId: Int? = null
    private var serviceId: Int? = null
    private var reserveTime: String? = null

    private lateinit var tvPinCode: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = PreferenceManager.getDefaultSharedPreferences(activity)
        editor = settings.edit()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_pre_record, container, false)
        rootView = root.findViewById(R.id.root_view)
        tvPinCode = root.findViewById(R.id.tv_pin_code)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        name = settings.getString(NAME, "")!!
        phone = settings.getString(PHONE, "")!!
        snils = settings.getString(SNILS, "")!!

        progressDialog = ProgressDialog(activity)
        progressDialog!!.isIndeterminate = true
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage(getString(R.string.progress_loading))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getOktmoObjectsLiveData().observe(viewLifecycleOwner, Observer { showOktmoObjects(it) })
        viewModel.getUnitsLiveData().observe(viewLifecycleOwner, Observer { showUnits(it) })
        viewModel.getServicesLiveData().observe(viewLifecycleOwner, Observer { showServices(it) })
        viewModel.getBookingDatesLiveData().observe(viewLifecycleOwner, Observer { showBookingDates(it) })
        viewModel.getTicketLiveData().observe(viewLifecycleOwner, Observer { showTicket(it) })
        viewModel.getErrorMessageLiveData().observe(viewLifecycleOwner, Observer { showError(it) })

        viewModel.getOktmoObjects()
        progressDialog!!.show()
    }

    private fun showOktmoObjects(it: OktmoObjectsResponse) {
        progressDialog!!.dismiss()
        oktmoObjects.clear()
        oktmoObjects.addAll(it.embedded.oktmoObjects)

        val listOktmo: MutableList<String> = mutableListOf()
        for (item in oktmoObjects) {
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
            viewModel.getServices(unitId!!)
        }
        dialog.show()
    }

    private fun showServices(it: List<ServiceResponse>) {
        progressDialog!!.dismiss()
        services.clear()
        services.addAll(it)

        val listServices: MutableList<String> = mutableListOf()
        for (item in services) {
            listServices.add(item.description!!)
        }

        val dialog: AlertDialog.Builder = AlertDialog.Builder(context)
        dialog.setTitle(getString(R.string.title_services))
        val types: Array<String> = listServices.toTypedArray()
        dialog.setItems(types) { dialog, which ->
            dialog.dismiss()
            serviceId = services[which].id
            progressDialog!!.show()
            viewModel.getBookingDates(unitId!!, serviceId!!)
        }
        dialog.show()
    }

    private fun showBookingDates(it: BookingDatesResponse) {
        progressDialog!!.dismiss()
        bookingDates.clear()
        bookingDates.addAll(it.content.dates!!)

        val listBookingDates: MutableList<String> = mutableListOf()
        for (item in bookingDates) {
            listBookingDates.add(item.timeFrom!!.substring(0, 10) + " " + item.timeFrom!!.substring(11, 16) + " - " + item.timeTo!!.substring(11, 16))
        }

        val dialog: AlertDialog.Builder = AlertDialog.Builder(context)
        dialog.setTitle(getString(R.string.title_booking_dates))
        val types: Array<String> = listBookingDates.toTypedArray()
        dialog.setItems(types) { dialog, which ->
            dialog.dismiss()
            reserveTime = bookingDates[which].timeFrom
            
            val dialogBuilder = AlertDialog.Builder(context)
            val inflater = this.layoutInflater
            val dialogView: View = inflater.inflate(R.layout.dialog_register_ticket, null)
            dialogBuilder.setTitle(getString(R.string.title_register_ticket))
            dialogBuilder.setView(dialogView)
            dialogBuilder.setCancelable(true)
            dialogBuilder.setPositiveButton(
                getString(R.string.btn_register)
            ) { dialog, which ->
                name = inputFIO.text.toString()
                snils = inputSnils.text.toString()
                phone = inputPhone.text.toString()
                dialog.dismiss()
                progressDialog!!.show()
                viewModel.registerTicket(unitId!!, serviceId!!, name, snils, phone, reserveTime!!)
            }
            inputFIO = dialogView.findViewById(R.id.input_fio) as TextInputEditText
            inputSnils = dialogView.findViewById(R.id.input_snils) as TextInputEditText
            inputPhone = dialogView.findViewById(R.id.input_phone) as TextInputEditText
            inputFIO.setText(name)
            inputSnils.setText(snils)
            inputPhone.setText(phone)
            val alertDialog = dialogBuilder.create()
            alertDialog.show()
        }
        dialog.show()
    }

    private fun showTicket(it: TicketResponse) {
        progressDialog!!.dismiss()

        val listBookingDates: MutableList<String> = mutableListOf()
        for (item in bookingDates) {
            listBookingDates.add(
                item.timeFrom!!.substring(0, 10) + " " + item.timeFrom!!.substring(
                    11,
                    16
                ) + " - " + item.timeTo!!.substring(11, 16)
            )
        }

        tvPinCode.text = "Ваш пин-код - ${it.pin}"
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