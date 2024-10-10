package com.example.transcribeapp.calanderEvents.logicLayer

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.transcribeapp.calanderEvents.eventCalender.Event

class AllEventPagingSource(private val repo: CalenderEventRepo) :
    PagingSource<Int, Event>() {
    override fun getRefreshKey(state: PagingState<Int, Event>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)

        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Event> {
        val page = params.key ?: 1
        return try {
           // val result = repo.getAllCalenderEvent(page, params.loadSize)
            val result = repo.getAllCalenderEvent()
            if (result.isSuccess) {
                val allEvent = result.getOrNull()?.data?.events ?: emptyList()
                LoadResult.Page(
                    data = allEvent,
                    prevKey = if (page==1) null else page-1,
                    nextKey = if (allEvent.isEmpty()) null else page+1

                )
            }else{
                LoadResult.Error(Exception("No Data"))
            }
        }catch (e:Exception){
            LoadResult.Error(e)
        }
    }
}