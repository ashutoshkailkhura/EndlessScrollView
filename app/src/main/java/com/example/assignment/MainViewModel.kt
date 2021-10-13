package com.example.assignment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.assignment.data.UserApi
import com.example.assignment.model.User
import kotlinx.coroutines.*

class MainViewModel : ViewModel() {

    private var _userList = MutableLiveData<MutableList<User.Data>>()
    val user: LiveData<MutableList<User.Data>>
        get() = _userList

    private var _apiTotalItemCount = MutableLiveData<Int>()
    val apiTotalItemCount: LiveData<Int>
        get() = _apiTotalItemCount

    private var _loadingStatus = MutableLiveData<Boolean>()
    val loadingStatus: LiveData<Boolean>
        get() = _loadingStatus

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        _userList.value = mutableListOf()
    }

    fun getUserData(page: Int, perPage: Int) {

        if (viewModelJob.isCompleted) {
            Log.d("XXX", "viewModelJob is isCompleted ,,,, returning  page :$page perPage $perPage")
        }
        if (viewModelJob.isActive) {
            Log.d("XXX", "viewModelJob is isActive ,,,, returning  page :$page perPage $perPage")
        }
        if (viewModelJob.isCancelled) {
            Log.d("XXX", "viewModelJob is isCancelled ,,,, returning  page :$page perPage $perPage")
        }
        if (uiScope.isActive) {
            Log.d("XXX", "uiScope is isActive ,,,, returning  page :$page perPage $perPage")
        }
        uiScope.launch {
            _loadingStatus.value = true
            delay(3_000)
            val userResponse =
                UserApi.retrofitService.getUser(page, perPage)
            try {
                if (userResponse?.data?.isNullOrEmpty() == true) {

                } else {
                    _apiTotalItemCount.value = userResponse!!.total
                    _userList.value = (userResponse!!.data.toMutableList())
                }
            } catch (e: Exception) {

            }
            _loadingStatus.value = false
        }
    }

    fun setupData() {
        _userList.value?.clear()
    }
}