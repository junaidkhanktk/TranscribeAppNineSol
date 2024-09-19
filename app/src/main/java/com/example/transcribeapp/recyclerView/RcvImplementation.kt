package com.example.transcribeapp.recyclerView

import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.filter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.transcribeapp.adapter.GenericPagingAdapter
import com.example.transcribeapp.databinding.FragmentHomeBinding
import com.example.transcribeapp.databinding.HistoryItemLayoutBinding
import com.example.transcribeapp.extension.convertToDateTime
import com.example.transcribeapp.extension.getRecordCategory
import com.example.transcribeapp.extension.log
import com.example.transcribeapp.extension.setFormattedTextWithDots
import com.example.transcribeapp.history.server.get.Recordings
import com.example.transcribeapp.history.server.logicLayer.UserHistoryViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


fun Fragment.historyRcv(
    context: Context,
    binding: FragmentHomeBinding,
    onItemClick: (String,String,Long) -> Unit,
) {

    val userHistoryViewModel by inject<UserHistoryViewModel>()

    val historyAdapter: GenericPagingAdapter<Recordings, HistoryItemLayoutBinding> =
        GenericPagingAdapter(
            inflater = { layoutInflater, parent, attachToRoot ->
                HistoryItemLayoutBinding.inflate(layoutInflater, parent, attachToRoot)
            },
            viewHolderBinder = { item, _ ->
                title.text = item.title
                date.text = convertToDateTime(item.timeStamp)
                item.transcribeTxt.let {
                    transcribeTxt.setFormattedTextWithDots(it)
                }
               // origionalTxt.setFormattedTextWithDots(item.transcribeTxt,3)
                daysWeekAgo.text = getRecordCategory(item.timeStamp)
            }
        )

    historyAdapter.setOnItemClickListener { item, pos ->
        "$pos".log()
        "listSize: $item".log()

        onItemClick.invoke(item.title,item.id,item.timeStamp)

    }

    binding.historyRcv.apply {

        viewLifecycleOwner.lifecycleScope.launch {

            userHistoryViewModel.recordingsFlow.collect { uiState ->

                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                setAdapter(historyAdapter)
                historyAdapter.submitData(uiState)
            }
        }



        historyAdapter.addLoadStateListener { loadState ->
            binding.progress.isVisible = loadState.source.refresh is LoadState.Loading

            // binding.errorText.isVisible = loadState.source.refresh is LoadState.Error
        }

    }


    binding.searchBoxContainer.searchEditText.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            viewLifecycleOwner.lifecycleScope.launch {
                userHistoryViewModel.recordingsFlow.collect { uiState ->

                    val filteredList = uiState.filter {
                        it.title.contains(
                            s.toString(),
                            ignoreCase = true
                        ) || it.title.contains(s.toString(), ignoreCase = true)
                    }
                    historyAdapter.submitData(filteredList)


                }
            }

        }

        override fun afterTextChanged(s: Editable?) {}
    })

}






