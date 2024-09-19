package com.example.transcribeapp.history.server.logicLayer

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.transcribeapp.history.server.get.Recordings

class RecordingsPagingSource(private val repo: UserHistoryRepo) : PagingSource<Int, Recordings>() {
    override fun getRefreshKey(state: PagingState<Int, Recordings>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Recordings> {
        val page = params.key ?: 1
        return try {
            val result = repo.getRecordData(page, params.loadSize)
            if (result.isSuccess) {
                val recordings = result.getOrNull()?.data?.recordings ?: emptyList()
                LoadResult.Page(
                    data = recordings,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (recordings.isEmpty()) null else page + 1
                )
            } else {
                LoadResult.Error(Exception("No data"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}