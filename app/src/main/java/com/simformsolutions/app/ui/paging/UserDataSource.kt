package com.simformsolutions.app.ui.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.simformsolutions.app.domain.model.User
import com.simformsolutions.app.domain.remote.apiresult.foldSuspend
import com.simformsolutions.app.domain.repository.UsersRepository

class UserDataSource(
    private val repository: UsersRepository
): PagingSource<Int, User>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        return try {
            val page = params.key ?: 1
            Log.d("helloo", "Loading page: $page")
            val users = repository.loadUsers(page).foldSuspend(
                onSuccess = { it.results },
                onError = { _, _ -> emptyList() },
                onException = { emptyList() }
            )
            LoadResult.Page(
                data = users,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (users.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        // Use the anchor position to determine the key for refreshing
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}