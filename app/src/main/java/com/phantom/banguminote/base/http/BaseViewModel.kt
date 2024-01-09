package com.phantom.banguminote.base.http

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.phantom.banguminote.base.http.entity.BaseData
import retrofit2.Response
import java.io.IOException

abstract class BaseViewModel : ViewModel() {

    val error = MutableLiveData<Exception>()

    fun <REQ : Any, DATA : Any> requestWithCoroutine(
        req: REQ,
        mutableLiveData: MutableLiveData<DATA>,
        errorLiveData: MutableLiveData<Exception>? = null,
        requestFunc: suspend (REQ) -> Response<BaseData<DATA>>
    ) {
        viewModelScope.launch {
            val result = safeCall {
                request(requestFunc(req))
            }
            withContext(Dispatchers.Main) {
                when (result) {
                    is ViewModelResult.Success -> {
                        val data = result.data
                        mutableLiveData.postValue(data)
                    }
                    is ViewModelResult.Error -> {
                        (errorLiveData ?: error).postValue(result.exception)
                    }
                }
            }
        }
    }

    private suspend fun <T : Any> safeCall(
        call: suspend () -> ViewModelResult<T>
    ): ViewModelResult<T> =
        try {
            call()
        } catch (e: Exception) {
            ViewModelResult.Error(e)
        }

    private fun <T : Any> request(res: Response<BaseData<T>>): ViewModelResult<T> {
        if (res.isSuccessful) {
            val result = res.body()
            if (result?.success() == true) {
                val data = result.data
                if (data != null) {
                    return ViewModelResult.Success(data)
                }
            } else {
                return ViewModelResult.Error(IOException("code:${result?.getCode()} msg:${result?.getMsg()}"))
            }
        }
        return ViewModelResult.Error(IOException("code:${res.code()} msg:${res.message()}"))
    }
}
