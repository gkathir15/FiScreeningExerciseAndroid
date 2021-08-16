package com.guru.fi.model

/**Created by Guru kathir.J on 15,August,2021 **/
//Custom model is used instead of default date API
data class Date(var dd:Int?,var mm:Int?,var yyyy:Int?)


data class UserDetail(var pan:String?,var date:Date?)