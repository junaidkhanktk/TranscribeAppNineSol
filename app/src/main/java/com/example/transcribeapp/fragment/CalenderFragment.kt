package com.example.transcribeapp.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.transcribeapp.R
import com.example.transcribeapp.bottomSheet.timeDatePicker
import com.example.transcribeapp.databinding.FragmentCalenderBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class CalenderFragment : BaseFragment<FragmentCalenderBinding>(FragmentCalenderBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            toEditText.setOnClickListener {
                val dialog = DateTimePickerBottomSheetDialogFragment { dateTime ->
                    toEditText.setText(dateTime)
                }
                dialog.show(parentFragmentManager, "DateTimePicker")
            }

            fromEditText.setOnClickListener {

                requireActivity().timeDatePicker()

            /*    val dialog = DateTimePickerBottomSheetDialogFragment { dateTime ->
                    fromEditText.setText(dateTime)
                }
                dialog.show(parentFragmentManager, "DateTimePicker")*/
            }
        }
    }
    private fun showDateTimePicker(onDateTimeSelected: (String) -> Unit) {
        val currentDate = Calendar.getInstance()

        DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)

            TimePickerDialog(requireContext(), { _, hourOfDay, minute ->
                selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedDate.set(Calendar.MINUTE, minute)

                val dateTime = SimpleDateFormat("EEE, MMM d, yyyy HH:mm", Locale.getDefault())
                    .format(selectedDate.time)
                onDateTimeSelected(dateTime)
            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show()

        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH)).show()
    }

}