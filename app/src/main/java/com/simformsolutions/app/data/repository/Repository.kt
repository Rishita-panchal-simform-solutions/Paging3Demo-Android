package com.simformsolutions.app.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import com.simformsolutions.app.data.db.UserDataBase
import com.simformsolutions.app.data.paging.UserRemoteMediator
import com.simformsolutions.app.domain.model.User
import com.simformsolutions.app.domain.repository.UsersRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class Repository @Inject constructor(
    private val usersRepository: UsersRepository,
    private val userDataBase: UserDataBase
) {

    fun getAllUsers(): Flow<PagingData<User>> {
        val pagingSourceFactory = { userDataBase.userDao().getAllUsers() }
        return Pager(
            config = androidx.paging.PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = UserRemoteMediator(usersRepository = usersRepository, userDataBase = userDataBase),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}