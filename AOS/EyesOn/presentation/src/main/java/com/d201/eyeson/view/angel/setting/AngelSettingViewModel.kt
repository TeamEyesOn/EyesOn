package com.d201.eyeson.view.angel.setting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d201.domain.model.AngelInfo
import com.d201.domain.usecase.user.DeleteUserUseCase
import com.d201.domain.usecase.user.GetAngelInfoUseCase
import com.d201.domain.usecase.user.PutAngelInfoUseCase
import com.d201.domain.utils.ResultType
import com.d201.eyeson.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "AngelSettingViewModel"

@HiltViewModel
class AngelSettingViewModel @Inject constructor(
    private val putAngelInfoUseCase: PutAngelInfoUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val angelInfoUseCase: GetAngelInfoUseCase
) : ViewModel() {

    private val _angelInfo: MutableStateFlow<AngelInfo?> = MutableStateFlow(null)
    val angelInfo get() = _angelInfo.asStateFlow()

    fun getAngelInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            angelInfoUseCase.execute().collectLatest {
                if (it is ResultType.Success && it.data.status == 200) {
                    _angelInfo.value = it.data.data
                } else {
                    Log.d(TAG, "getAngelInfo: ${it}")
                }
            }
        }
    }

    private val _saveSettingEvent = SingleLiveEvent<Boolean>()
    val saveSettingEvent get() = _saveSettingEvent
    fun putAngelInfo(alarmStart: Int, alarmEnd: Int, alarmDay: Int, active: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            putAngelInfoUseCase.execute(AngelInfo(alarmStart, alarmEnd, alarmDay, active))
                .collectLatest {
                    if (it is ResultType.Success && it.data.status == 200) {
                        _angelInfo.value = it.data.data
                        _saveSettingEvent.postValue(true)
                    } else {
                        Log.d(TAG, "putAngelInfo: ${it}")
                    }
                }
        }
    }

    private val _deleteUserEvent = SingleLiveEvent<Boolean>()
    val deleteUserEvent get() = _deleteUserEvent
    fun deleteUser() {
        viewModelScope.launch(Dispatchers.IO) {
            deleteUserUseCase.execute().collectLatest {
                if (it is ResultType.Success) {
                    _deleteUserEvent.postValue(true)
                }
            }
        }
    }
}