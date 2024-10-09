package com.example.transcribeapp.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract

import android.view.View

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.transcribeapp.adapter.CalendarEventAdapter
import com.example.transcribeapp.dataClasses.CalendarEvent
import com.example.transcribeapp.databinding.FragmentCalenderBinding
import com.example.transcribeapp.extension.log
import com.example.transcribeapp.calanderEvents.uploadEventCalender.UploadCalanderEventReq
import com.example.transcribeapp.permission.PermissionUtils
import com.example.transcribeapp.permission.calenderPermission

import java.util.Calendar


class CalenderFragment : BaseFragment<FragmentCalenderBinding>(FragmentCalenderBinding::inflate) {

    private lateinit var calenderAdapter: CalendarEventAdapter

    private val appTag = "MyAppTag"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adEventInRcv()


        // requestCalendarPermissions()


        binding?.apply {
            btnAddEvent.setOnClickListener {
                PermissionUtils.checkPermission(
                    context = requireActivity(),
                    permissionArray = calenderPermission,
                    permissionListener = object : PermissionUtils.OnPermissionListener {
                        override fun onPermissionSuccess() {
                            openCalendarToAddEvent()
                        }

                    }
                )
            }
        }


    }

    private fun adEventInRcv() {
        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            calenderAdapter = CalendarEventAdapter()
            adapter = calenderAdapter
        }

    }


    private fun openCalendarToAddEvent() {
        val beginTime = Calendar.getInstance()
        val endTime = beginTime.clone() as Calendar
        endTime.add(Calendar.HOUR, 1) // Duration of 1 hour

        val intent = Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(
                CalendarContract.Events.TITLE,
                "New Event - $appTag"
            )  // Add your app tag in the title
            .putExtra(
                CalendarContract.Events.DESCRIPTION,
                "Event Description - $appTag"
            )  // Or in the description
            .putExtra(CalendarContract.Events.EVENT_LOCATION, "Event Location")
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.timeInMillis)
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.timeInMillis)

        startActivity(intent)
    }

    // Fetch only the events that were created by your app
    private fun fetchAppCreatedCalendarEvents(): List<CalendarEvent> {
        val events = mutableListOf<CalendarEvent>()
        val projection = arrayOf(
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND
        )
        "CalenderValues ID:${CalendarContract.Events._ID} ".log()
        "CalenderValues TITLE:${CalendarContract.Events.TITLE} "


        // Filter to only include events with the app tag
        val selection =
            "${CalendarContract.Events.TITLE} LIKE ? OR ${CalendarContract.Events.DESCRIPTION} LIKE ?"
        val selectionArgs = arrayOf("%$appTag%", "%$appTag%")

        val cursor = context?.contentResolver?.query(
            CalendarContract.Events.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            CalendarContract.Events.DTSTART + " ASC"
        )

        cursor?.use {
            val idIndex = it.getColumnIndex(CalendarContract.Events._ID)
            val titleIndex = it.getColumnIndex(CalendarContract.Events.TITLE)
            val startIndex = it.getColumnIndex(CalendarContract.Events.DTSTART)
            val endIndex = it.getColumnIndex(CalendarContract.Events.DTEND)

            "CalenderValues idIndex:$idIndex ".log()
            "CalenderValues titleIndex:$titleIndex ".log()
            "CalenderValues startIndex:$startIndex ".log()
            "CalenderValues endIndex:$endIndex ".log()


            while (it.moveToNext()) {
                val eventId = it.getLong(idIndex)
                val title = it.getString(titleIndex)
                val startTime = it.getLong(startIndex)
                val endTime = it.getLong(endIndex)


                "CalenderValues eventId:$eventId ".log()
                "CalenderValues title:$title ".log()
                "CalenderValues startTime:$startTime ".log()
                "CalenderValues endTime:$endTime ".log()

                events.add(CalendarEvent(eventId, title, startTime, endTime))


                val req = UploadCalanderEventReq(
                    description = title,
                    endTime = endTime.toString(),
                    startTime = startTime.toString(),
                    title = title
                )
                calenderEventViewModel.upLoadCalenderEvent(req)

            }
        }
        return events
    }

    // Reload the events when the fragment is resumed
    override fun onResume() {
        super.onResume()
        updateEventsInRecyclerView()
    }

    private fun updateEventsInRecyclerView() {
        val events = fetchAppCreatedCalendarEvents()
        calenderAdapter.submitList(events)

    }


}

