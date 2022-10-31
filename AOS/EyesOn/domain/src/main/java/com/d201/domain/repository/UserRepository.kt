package com.d201.domain.repository

import com.d201.domain.base.BaseResponse
import com.d201.domain.model.AngelInfo
import com.d201.domain.model.JWTToken
import com.d201.domain.model.Login
import com.d201.domain.utils.ResultType
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun loginUser(idToken:String, fcmToken:String): Flow<ResultType<BaseResponse<Login>>>

    fun getAngelInfo(): Flow<ResultType<BaseResponse<AngelInfo>>>
}