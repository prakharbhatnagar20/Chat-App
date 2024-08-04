package com.pal.chattingapplication.data

data class UserData(
    var userId: String?="",
    var name: String?="",
    var number: String?="",
    var imageUrl: String?="",
    var location: String?=""

){
    fun toMap() = mapOf(
        "userId" to userId,
        "name" to name,
        "number" to number,
        "imageUrl" to imageUrl,
        "location" to location
    )
}

data class ChatData(
    val chatId: String?="",
    val user1:ChatUser=ChatUser(),
    val user2:ChatUser=ChatUser()
)

data class ChatUser(
    val userId: String?="",
    val name: String?="",
    val imageUrl: String?="",
    val number: String?="",
    val location: String?=""
)

data class Message(
    var sendBy: String?="",
    val message: String?="",
    val timestamp: String?=""
)