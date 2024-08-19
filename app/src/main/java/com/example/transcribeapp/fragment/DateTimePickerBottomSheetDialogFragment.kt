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

        val tvSelectedDateTime = view.findViewById<TextView>(R.id.tvSelectedDateTime)
        val npDay = view.findViewById<NumberPicker>(R.id.npDay)
        val npMonth = view.findViewById<NumberPicker>(R.id.npMonth)
        val npYear = view.findViewById<NumberPicker>(R.id.npYear)
        val npHour = view.findViewById<NumberPicker>(R.id.npHour)
        val npMinute = view.findViewById<NumberPicker>(R.id.npMinute)

        // Configure NumberPickers
        npDay.minValue = 1
        npDay.maxValue = 31

        npMonth.minValue = 1
        npMonth.maxValue = 12

        npYear.minValue = 2023
        npYear.maxValue = 2030

        npHour.minValue = 0
        npHour.maxValue = 23

        npMinute.minValue = 0
        npMinute.maxValue = 59

        // Handle OK Button click
        view.findViewById<Button>(R.id.btnOk).setOnClickListener {
            val day = npDay.value
            val month = npMonth.value
            val year = npYear.value
            val hour = npHour.value
            val minute = npMinute.value

            val calendar = Calendar.getInstance()
            calendar.set(year, month - 1, day, hour, minute)

            val dateTime = SimpleDateFormat("EEE, MMM d, yyyy HH:mm", Locale.getDefault())
                .format(calendar.time)
            onDateTimeSelected(dateTime)
            dismiss()
        }

        // Handle Cancel Button click
        view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            dismiss()
        }

        return view
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
}
