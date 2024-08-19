package com.example.transcribeapp.recyclerView

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.transcribeapp.adapter.GenericRvAdapter
import com.example.transcribeapp.databinding.FragmentHomeBinding
import com.example.transcribeapp.databinding.HistoryItemLayoutBinding
import com.example.transcribeapp.extension.getFormattedTime
import com.example.transcribeapp.extension.getTimeAgoWithDate
import com.example.transcribeapp.extension.log
import com.example.transcribeapp.extension.setFormattedTextWithDots
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


    val historyAdapter: GenericRvAdapter<History, HistoryItemLayoutBinding> =
        GenericRvAdapter(
            inflater = { LayoutInflater, parent, attachToRoot ->
                HistoryItemLayoutBinding.inflate(LayoutInflater, parent, attachToRoot)
            },

            viewHolderBinder = { item, position ->

                title.text = item.title
                date.text = item.currentDate
                time.text = getFormattedTime(item.currentTime)
               // origionalTxt.text = ". ${item.text}"
                origionalTxt.setFormattedTextWithDots(item.text)


                daysWeekAgo.text = getTimeAgoWithDate(item.currentTime)
                startUpdatingTimeAgo(item.currentTime) { timeAgo ->
                    timeAgoTxt.text = timeAgo
                }

            }

        )

    historyAdapter.setonItemClickListener { item, pos ->
        "$pos".log()
        "listSize: ${historyAdapter.currentList.size}".log()
        historyViewModel.setSelectedItemHistory(item)
        onItemClick.invoke(pos)

    }

    binding.historyRcv.apply {

        lifecycleScope.launch {
            historyViewModel.readHistory.collect { historyList ->
                "data size ${historyList.size}".log()
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                setAdapter(historyAdapter)
                historyAdapter.submitList(historyList)
            }
        }


    }


    binding.searchBoxContainer.searchEditText.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            lifecycleScope.launch {
                historyViewModel.readHistory.collect { historyList ->
                    val filteredList = historyList.filter {
                        it.title.contains(s.toString(), ignoreCase = true) || it.text.contains(s.toString(), ignoreCase = true)
                    }
                    historyAdapter.submitList(filteredList)
                }
            }

        }

        override fun afterTextChanged(s: Editable?) {}
    })

}

