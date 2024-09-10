package com.example.transcribeapp.recyclerView

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.filter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.transcribeapp.adapter.GenericPagingAdapter
import com.example.transcribeapp.databinding.FragmentHomeBinding
import com.example.transcribeapp.databinding.HistoryItemLayoutBinding
import com.example.transcribeapp.extension.beGone
import com.example.transcribeapp.extension.beVisible
import com.example.transcribeapp.extension.getFormattedTime
import com.example.transcribeapp.extension.getTimeAgoWithDate
import com.example.transcribeapp.extension.log
import com.example.transcribeapp.extension.setFormattedTextWithDots
import com.example.transcribeapp.extension.startUpdatingTimeAgo
import com.example.transcribeapp.history.History
import com.example.transcribeapp.history.mvvm.HistoryViewModel
import com.example.transcribeapp.history.server.get.Recordings
import com.example.transcribeapp.history.server.logicLayer.UserHistoryViewModel
import com.example.transcribeapp.uiState.UiState
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


fun Fragment.historyRcv(
    context: Context,
    binding: FragmentHomeBinding,
    onItemClick: (Int) -> Unit,
   // state: (String) -> Unit,
) {

    val historyViewModel by inject<HistoryViewModel>()
    val userHistoryViewModel by inject<UserHistoryViewModel>()


    val historyAdapter: GenericPagingAdapter<Recordings, HistoryItemLayoutBinding> =
        GenericPagingAdapter(
            inflater = { LayoutInflater, parent, attachToRoot ->
                HistoryItemLayoutBinding.inflate(LayoutInflater, parent, attachToRoot)
            },

            viewHolderBinder = { item, position ->

                title.text = item.title
                date.text = item.dataTime
                time.text = getFormattedTime(item.timeStamp)
                // origionalTxt.text = ". ${item.text}"
                origionalTxt.setFormattedTextWithDots(item.title)


                /*    daysWeekAgo.text = getTimeAgoWithDate(item.currentTime)
                    startUpdatingTimeAgo(item.currentTime) { timeAgo ->
                        timeAgoTxt.text = timeAgo
                    }*/

            }

        )

    historyAdapter.setOnItemClickListener { item, pos ->
        "$pos".log()
        "listSize: $item".log()
        // historyViewModel.setSelectedItemHistory(item)
        onItemClick.invoke(pos)

    }

    binding.historyRcv.apply {

        viewLifecycleOwner.lifecycleScope.launch {

            userHistoryViewModel.recordingsFlow.collect { uiState ->

                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                setAdapter(historyAdapter)
                historyAdapter.submitData(uiState)

              /*  when (uiState) {
                    is UiState.Idle -> {
                        //state.invoke("idl")
                    }

                    is UiState.Loading -> {
                        binding.progress.beVisible()
                        //state.invoke("loading")
                    }

                    is UiState.Error -> {
                       // state.invoke("error")
                        "data error ".log(Log.DEBUG, "MyList")
                        binding.progress.beGone()
                    }

                    is UiState.Success -> {
                        binding.progress.beGone()
                        //state.invoke("success")
                        "data size ${uiState.data?.data?.recordings}".log(Log.DEBUG, "MyList")
                        layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        setAdapter(historyAdapter)
                        historyAdapter.submitData(uiState.data?.data?.recordings)
                    }
                }*/
            }
        }



        historyAdapter.addLoadStateListener { loadState ->
            binding.progress.isVisible = loadState.source.refresh is LoadState.Loading

           // binding.errorText.isVisible = loadState.source.refresh is LoadState.Error
        }




/*
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                "scrollide".log()
                if (!userHistoryViewModel.isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    "nextPageload".log()
                    userHistoryViewModel.loadNextPage()
                }
            }
        })
*/

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

                 /*   when (uiState) {
                        is UiState.Idle -> {

                        }

                        is UiState.Loading -> {

                        }

                        is UiState.Error -> {
                            "data error ".log(Log.DEBUG, "MyList")
                        }

                        is UiState.Success -> {
                            val filteredList = uiState.data?.data?.recordings?.filter {
                                it.title.contains(
                                    s.toString(),
                                    ignoreCase = true
                                ) || it.title.contains(s.toString(), ignoreCase = true)
                            }
                            historyAdapter.submitList(filteredList)

                        }
                    }*/


                }
            }


            /*
                        lifecycleScope.launch {
                            historyViewModel.readHistory.collect { historyList ->
                                val filteredList = historyList.filter {
                                    it.title.contains(s.toString(), ignoreCase = true) || it.text.contains(s.toString(), ignoreCase = true)
                                }
                                historyAdapter.submitList(filteredList)
                            }
                        }*/


        }

        override fun afterTextChanged(s: Editable?) {}
    })

}

fun refreshAdapter(){
   // historyAdapter.refresh()
}

