package com.scootin.network.response.orders

 data class OrderHistoryList (
     val id:Int,
     val orderId:String,
     val deliveryType:String,
     val orderDate:String,
     val status:String,
     val amount:String

 )