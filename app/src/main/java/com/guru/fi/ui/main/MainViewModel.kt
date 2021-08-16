package com.guru.fi.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guru.fi.model.Date

class MainViewModel : ViewModel() {
    val panNumber = MutableLiveData<String>()
    val date = MutableLiveData<Date>(Date(null,null,null))

}