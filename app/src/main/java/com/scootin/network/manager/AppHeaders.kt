package com.scootin.network.manager

import com.scootin.network.response.login.ROLE
import com.scootin.network.response.login.ResponseUser
import com.scootin.util.constants.AppConstants

//This class will deal with below param
object AppHeaders {

    var userID: String = ""
    var userMobileNumber: String = ""
    var token:String = ""

    var role: ROLE = ROLE.USER

    const val PREFIX = "Bearer "

    fun getHeaderMap() = hashMapOf(AppConstants.AUTHORIZATION to PREFIX + token)

    fun updateUserData(user: ResponseUser) {
        userID = user.id.toString()
        userMobileNumber = user.user
        token = user.getTokenData()
        role = user.role
    }


}