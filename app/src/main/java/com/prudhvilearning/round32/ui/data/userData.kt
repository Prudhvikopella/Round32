package com.prudhvilearning.round32.ui.data
import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val userName : String ,
    val userLocation : String,
    val matchPercentage : Int,
    val profileUrl : String,
     val activeStatus : Boolean ,
    val age : Int
)

data class Message(
    val id: Int,
    val text: String,
    val isSender: Boolean,
    val timestamp: String
)
