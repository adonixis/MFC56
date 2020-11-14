package ru.adonixis.mfc56.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import ru.adonixis.mfc56.R

class CheckStatusFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_check_status, container, false)
        val textView: TextView = root.findViewById(R.id.text_view)
        textView.text = "Проверка статуса заявления"
        return root
    }
}