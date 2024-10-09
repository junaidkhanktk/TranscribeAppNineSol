package com.example.transcribeapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.transcribeapp.R
import com.example.transcribeapp.adapter.ChatMessageAdapter.AnswerViewHolder
import com.example.transcribeapp.dataClasses.CalendarEvent
import com.example.transcribeapp.databinding.ItemAnswerBinding
import com.example.transcribeapp.databinding.ItemEventBinding
import com.example.transcribeapp.databinding.ItemQuestionBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * @Author: Naveed Ur Rehman
 * @Designation: Android Developer
 * @Date: 26/09/2024
 * @Gmail: naveedurrehman.ninesol@gmail.com
 * @Company: Ninesol Technologies
 */
class CalendarEventAdapter :
    ListAdapter<CalendarEvent, CalendarEventAdapter.CalendarEventViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CalendarEvent>() {
            override fun areItemsTheSame(oldItem: CalendarEvent, newItem: CalendarEvent): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: CalendarEvent,
                newItem: CalendarEvent,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarEventViewHolder {

        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalendarEventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarEventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    class CalendarEventViewHolder(private val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: CalendarEvent) {
            itemView.findViewById<TextView>(R.id.eventTitle).text = event.title
            binding.apply {

                eventTitle.text = event.title

                val sTime = SimpleDateFormat(
                    "hh:mm a",
                    Locale.getDefault()
                ).format(Date(event.startTime))
                val eTime = SimpleDateFormat(
                    "hh:mm a",
                    Locale.getDefault()
                ).format(Date(event.endTime))
               // dd MMM yyyy, HH:mm
                eventTime.text = "$sTime - $eTime"
            }


        }
    }
}
