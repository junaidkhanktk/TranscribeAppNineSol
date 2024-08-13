package com.example.transcribeapp.recyclerView

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.transcribeapp.activities.ViewHistory
import com.example.transcribeapp.adapter.GenericRvAdapter
import com.example.transcribeapp.databinding.ActivityHistoryBinding
import com.example.transcribeapp.databinding.FragmentHomeBinding
import com.example.transcribeapp.databinding.HistoryItemLayoutBinding
import com.example.transcribeapp.extension.getFormattedTime
import com.example.transcribeapp.extension.getTimeAgoWithDate
import com.example.transcribeapp.extension.log
import com.example.transcribeapp.extension.startUpdatingTimeAgo
import com.example.transcribeapp.history.History
import com.example.transcribeapp.history.mvvm.HistoryViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


fun Fragment.historyRcv(
    context: Context,
    binding: FragmentHomeBinding,
    onItemClick: (Int) -> Unit,
) {

    val historyViewModel by inject<HistoryViewModel>()


    val adapter: GenericRvAdapter<History, HistoryItemLayoutBinding> =
        GenericRvAdapter(
            inflater = { LayoutInflater, parent, attachToRoot ->
                HistoryItemLayoutBinding.inflate(LayoutInflater, parent, attachToRoot)
            },

            viewHolderBinder = { item, position ->

                title.text = item.title
                date.text = item.currentDate
                time.text = getFormattedTime(item.currentTime)
                origionalTxt.text = item.text
                daysWeekAgo.text = getTimeAgoWithDate(item.currentTime)
                startUpdatingTimeAgo(item.currentTime) { timeAgo ->
                    timeAgoTxt.text = timeAgo
                }

            }

        )

    adapter.setonItemClickListener { item, pos ->
        "$pos".log()
        "listSize: ${adapter.currentList.size}".log()
        historyViewModel.setSelectedItemHistory(item)
        onItemClick.invoke(pos)

        /*  startActivity(Intent(this, ViewHistory::class.java).apply {
              // you can add values(if any) to pass to the next class or avoid using `.apply`
              putExtra("Position", pos)
          })*/
    }

    binding.historyRcv.apply {
/*
        historyViewModel.readHistory.observe(this@historyRcv) { historyList ->
        }*/
        lifecycleScope.launch {
            historyViewModel.readHistory.collect { historyList ->
                "data size ${historyList.size}".log()
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                setAdapter(adapter)
                adapter.submitList(historyList)
            }
        }


    }

}