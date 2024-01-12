package com.phantom.banguminote.base.http

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException

abstract class BaseViewModel : ViewModel() {

    val error = MutableLiveData<Exception>()

    fun <REQ : Any, DATA : Any> requestWithCoroutine(
        req: REQ,
        mutableLiveData: MutableLiveData<DATA>,
        errorLiveData: MutableLiveData<Exception> = error,
        requestFunc: suspend (REQ) -> Response<DATA>
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
                        Log.e("Net", result.exception.stackTraceToString())
                        errorLiveData.postValue(result.exception)
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

    private fun <T : Any> request(res: Response<T>): ViewModelResult<T> {
        return if (res.isSuccessful)
            ViewModelResult.Success(res.body())
        else
            ViewModelResult.Error(IOException("code:${res.code()} msg:${res.message()}"))
    }
}
