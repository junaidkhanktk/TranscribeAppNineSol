package com.example.transcribeapp.fragment

import android.Manifest

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.transcribeapp.R
import com.example.transcribeapp.adapter.CalendarEventAdapter
import com.example.transcribeapp.dataClasses.CalendarEvent
import com.example.transcribeapp.databinding.FragmentAIChatBinding
import com.example.transcribeapp.databinding.FragmentCalenderBinding
import com.example.transcribeapp.history.server.eventCalander.UploadCalanderEventReq

import java.util.Calendar


class CalenderFragment : BaseFragment<FragmentCalenderBinding>(FragmentCalenderBinding::inflate) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CalendarEventAdapter

    // Request code for calendar permissions
    private val CALENDAR_PERMISSION_REQUEST_CODE = 100
    private val appTag = "MyAppTag"  // Unique identifier for app-created events

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calender, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = CalendarEventAdapter()
        recyclerView.adapter = adapter

        // Request calendar permissions
        requestCalendarPermissions()

        // Button to open the device's calendar for adding an event
        view.findViewById<Button>(R.id.btnAddEvent).setOnClickListener {
            openCalendarToAddEvent()
        }




        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val req = UploadCalanderEventReq(
            description = "this is my testing event",
            endTime ="1725761316",
            startTime = "1725761320",
            title = "jjjjjjj"
        )
        userHistoryViewModel.upLoadCalenderEvent(req)

    }


    // Request calendar read/write permissions at runtime
    private fun requestCalendarPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR),
            CALENDAR_PERMISSION_REQUEST_CODE
        )
    }

    // Open calendar intent to create a new event
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
        Log.d("CalenderValues", "fetchAppCreatedCalendarEvents:${CalendarContract.Events._ID} ")
        Log.d("CalenderValues", "fetchAppCreatedCalendarEvents:${CalendarContract.Events.TITLE} ")
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




            Log.d("CalenderValues", "idIndex:$idIndex ")
            Log.d("CalenderValues", "titleIndex:$titleIndex ")
            Log.d("CalenderValues", "startIndex:$startIndex ")
            Log.d("CalenderValues", "endIndex:$endIndex ")

            while (it.moveToNext()) {
                val eventId = it.getLong(idIndex)
                val title = it.getString(titleIndex)
                val startTime = it.getLong(startIndex)
                val endTime = it.getLong(endIndex)
                Log.d("CalenderValues", "eventId:$eventId ")
                Log.d("CalenderValues", "title:$title ")
                Log.d("CalenderValues", "startTime:$startTime ")
                Log.d("CalenderValues", "endTime:$endTime ")

                events.add(CalendarEvent(eventId, title, startTime, endTime))
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
        adapter.submitList(events)


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CALENDAR_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
               // updateEventsInRecyclerView()
            }
        }
    }
}

