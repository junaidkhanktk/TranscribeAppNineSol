package com.example.transcribeapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import com.example.transcribeapp.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * @Author: Naveed Ur Rehman
 * @Designation: Android Developer
 * @Date: 16/08/2024
 * @Gmail: naveedurrehman.ninesol@gmail.com
 * @Company: Ninesol Technologies
 */
class DateTimePickerBottomSheetDialogFragment(
    private val onDateTimeSelected: (String) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_date_time_picker_bottom_sheet, container, false)


        return view
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
}
