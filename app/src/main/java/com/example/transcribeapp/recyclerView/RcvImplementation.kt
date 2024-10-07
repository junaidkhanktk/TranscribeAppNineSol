package com.example.transcribeapp.recyclerView

import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.filter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.transcribeapp.adapter.GenericPagingAdapter
import com.example.transcribeapp.adapter.GenericRvAdapter
import com.example.transcribeapp.databinding.FragmentAiChatInnerBinding
import com.example.transcribeapp.databinding.FragmentHomeBinding
import com.example.transcribeapp.databinding.HistoryItemLayoutBinding
import com.example.transcribeapp.databinding.QuestionRcvItemBinding
import com.example.transcribeapp.extension.convertToDateTime
import com.example.transcribeapp.extension.getRecordCategory
import com.example.transcribeapp.extension.log
import com.example.transcribeapp.extension.setFormattedTextWithDots
import com.example.transcribeapp.history.server.get.Recordings
import com.example.transcribeapp.history.server.logicLayer.UserHistoryViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


fun Fragment.historyRcv(
    context: Context,
    binding: FragmentHomeBinding,
    onItemClick: (String, String, Long) -> Unit,
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

        onItemClick.invoke(item.title, item.id, item.timeStamp)

    }

    binding.historyRcv.apply {

        viewLifecycleOwner.lifecycleScope.launch {

            userHistoryViewModel.recordingsFlow.collect { uiState ->

                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                setAdapter(historyAdapter)
                historyAdapter.submitData(uiState)
                scrollToPosition(0)

            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            historyAdapter.loadStateFlow.collectLatest { state ->
                binding.progress.isVisible = (state.refresh is LoadState.Loading) || (state.append is LoadState.Loading)

                val refreshState = state.source.refresh
                if (refreshState is LoadState.NotLoading && refreshState.endOfPaginationReached.not()) {
                    scrollToPosition(0)
                }
            }
        }

       /* historyAdapter.addLoadStateListener { loadState ->
            // binding.progress.isVisible = loadState.source.refresh is LoadState.Loading
            val isRefreshing = loadState.source.refresh is LoadState.Loading
            val isAppending = loadState.source.append is LoadState.Loading
            val isPrepending = loadState.source.prepend is LoadState.Loading


            // Show progress bar when refreshing (initial load) or appending (loading more data)
            binding.progress.isVisible = isRefreshing || isAppending || isPrepending


            val refreshState = loadState.source.refresh
            if (refreshState is LoadState.NotLoading && refreshState.endOfPaginationReached.not()) {
                scrollToPosition(0)
            }

            // Handle errors: append or prepend state errors (when loading more items)
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
            errorState?.let {
                Log.e("Paging", "Error: ${it.error}")
            }

        }*/

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


fun Fragment.questionsRcv(
    context: Context,
    questions: String,
    binding: FragmentAiChatInnerBinding,
    onItemClick: (String) -> Unit,
) {

    val userHistoryViewModel by inject<UserHistoryViewModel>()

    val questionAdapter: GenericRvAdapter<chatQuestion, QuestionRcvItemBinding> =
        GenericRvAdapter(
            inflater = { layoutInflater, parent, attachToRoot ->
                QuestionRcvItemBinding.inflate(layoutInflater, parent, attachToRoot)
            },
            viewHolderBinder = { item, _ ->
                tvQuestions.text = item.chatQuestion
            }
        )

    questionAdapter.setonItemClickListener { item, pos ->
        "$pos".log()
        "listSize: $item".log()

        onItemClick.invoke(item.chatQuestion)

    }

    binding.rcvQuestions.apply {
        layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        setAdapter(questionAdapter)
        val questionList = questions.split("\n").map {
            chatQuestion(it.trim())
        }
        questionAdapter.submitList(questionList)
        //questionAdapter.submitList(getDummyChat())

    }


}

data class chatQuestion(val chatQuestion: String)


private fun getDummyChat(): ArrayList<chatQuestion> {
    return arrayListOf(
        chatQuestion("what is the purpose of Ai in It Industry breifly explain"),
        chatQuestion("what is the purpose of Ai in It Industry breifly explain"),
        chatQuestion("what is the purpose of Ai in It Industry breifly explain"),
    )
}