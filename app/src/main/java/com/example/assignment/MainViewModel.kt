package com.example.assignment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.assignment.data.UserApi
import com.example.assignment.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private var _userList = MutableLiveData<MutableList<User.Data>>()
    val user: LiveData<MutableList<User.Data>>
        get() = _userList

    private var _apiTotalItemCount = MutableLiveData<Int>()
    val apiTotalItemCount: LiveData<Int>
        get() = _apiTotalItemCount

    private var _toastMsg = MutableLiveData<String>()
    val toastMsg: LiveData<String>
        get() = _toastMsg

    private var _loadingStatus = MutableLiveData<Boolean>()
    val loadingStatus: LiveData<Boolean>
        get() = _loadingStatus

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        _userList.value = mutableListOf()
    }

    fun getUserData(page: Int, perPage: Int) {
        uiScope.launch {
            _loadingStatus.value = true
            val userResponse =
                UserApi.retrofitService.getUser(page, perPage)
            try {
                if (userResponse?.data?.isNullOrEmpty() == true) {
                    _toastMsg.value = "error"
                } else {
                    _apiTotalItemCount.value = userResponse!!.total
                    _userList.value = (userResponse!!.data.toMutableList())
                }
            } catch (e: Exception) {
                _toastMsg.value = "catch error"
            }
            _loadingStatus.value = false
        }
    }

    fun setupData() {
        _userList.value?.clear()
    }
}