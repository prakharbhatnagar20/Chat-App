package com.example.chattingapplication

import android.net.Uri

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import com.example.chattingapplication.data.CHATS
import com.example.chattingapplication.data.ChatData
import com.example.chattingapplication.data.ChatUser
import com.example.chattingapplication.data.Event
import com.example.chattingapplication.data.MESSAGE
import com.example.chattingapplication.data.Message
import com.example.chattingapplication.data.USER_NODE
import com.example.chattingapplication.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LCViewModel @Inject constructor(
    val auth: FirebaseAuth,
    var db : FirebaseFirestore,
    val storage : FirebaseStorage
) : ViewModel() {

    var inProcess = mutableStateOf(false)
    val eventMutableState = mutableStateOf<Event<String>?>(null)
    var signIn = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)
    val chats = mutableStateOf<List<ChatData>>(listOf())
    val chatMessages = mutableStateOf<List<Message>>(listOf())
    var inProgressChatMessages = mutableStateOf(false)
    var currentChatMessageListener: ListenerRegistration?=null
    init {
        val currentUser = auth.currentUser
        signIn.value = currentUser!=null
        currentUser?.uid?.let {
            getUserData(it)
        }

    }

    fun populateMessages(chatID: String){
        inProgressChatMessages.value = true
        currentChatMessageListener = db.collection(CHATS).document(chatID).collection(MESSAGE)
            .addSnapshotListener{
                value, error ->
                if (error!=null){
                    handleException(error)

                }
                if (value!=null){
                    chatMessages.value = value.documents.mapNotNull {
                        it.toObject<Message>()
                    }.sortedBy { it.timestamp }
                    inProgressChatMessages.value = false
                }
            }

    }

    fun depopulateMessages(){
        chatMessages.value = listOf()
        currentChatMessageListener = null
    }
    fun populateChats(){
        inProcess.value = true
        db.collection(CHATS).where(
            Filter.or(
                Filter.equalTo("user1.userId", userData.value?.userId),
                Filter.equalTo("user2.userId", userData.value?.userId),

            )
        ).addSnapshotListener{
            value, error ->
            if(error!=null){
                handleException(error)

            }
            if(value!=null){
                chats.value = value.documents.mapNotNull {
                    it.toObject<ChatData>()
                }


            }

        }
    }

    fun signUp(name: String, email: String, password: String, number: String){
        inProcess.value = true
        if(name.isEmpty() or email.isEmpty() or password.isEmpty() or number.isEmpty()){
            handleException(customMessage = "Fill All Details")
            return
        }
        inProcess.value = true
        db.collection(USER_NODE).whereEqualTo("number", number).get().addOnSuccessListener {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful){
                        Log.d("Tag","SignUp : Successful")
                        signIn.value = true
                        createOrUpdateProfile(name=name,email =  email, number = number)
                    }else{
                        handleException(it.exception, customMessage = "Sign Up failed")
                    }
                }

        }


    }

    fun logIn(email: String, password: String){
        if (email.isEmpty() or password.isEmpty()){
            handleException(customMessage = "Fill all entry fields")
            return
        }else{
            inProcess.value = true
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{
                    if(it.isSuccessful){
                        signIn.value = true
                        inProcess.value = false
                        auth.currentUser?.uid.let {
                            if (it != null) {
                                getUserData(it)
                            }
                        }
                    }else{
                        handleException(exception = it.exception, customMessage = "login Failed")
                    }
                }
        }

    }
    fun uploadProfileImage(uri: Uri){
        uploadImage(uri){
            createOrUpdateProfile(imageurl = it.toString())

        }
    }
    fun uploadImage(uri: Uri, onSucess:(Uri)->Unit){
        inProcess.value = true
        val storageRef = storage.reference
        val uuid = UUID.randomUUID()
        val imageRef = storageRef.child("images/$uuid")
        val uploadTask = imageRef.putFile(uri)
        uploadTask.addOnSuccessListener {
            val result = it.metadata?.reference?.downloadUrl
            result?.addOnSuccessListener(onSucess)
            inProcess.value = false

        }
            .addOnFailureListener{
                handleException(it)
            }


    }
    fun createOrUpdateProfile(name: String? = null, email: String? = null, number: String? = null, imageurl: String? = null, location: String? = null) {
        val uid = auth.currentUser?.uid
        uid?.let {
            inProcess.value = true
            db.collection(USER_NODE).document(uid).get().addOnSuccessListener { document ->
                val currentData = document.toObject(UserData::class.java)
                val userData = UserData(
                    userId = uid,
                    name = name ?: currentData?.name,
                    number = number ?: currentData?.number,
                    imageUrl = imageurl ?: currentData?.imageUrl,
                    location = location ?: currentData?.location
                )
                db.collection(USER_NODE).document(uid).set(userData).addOnCompleteListener {
                    inProcess.value = false
                    getUserData(uid)
                    updateChatNodes(userData)
                }
            }.addOnFailureListener {
                handleException(it, "Cannot retrieve user")
                inProcess.value = false
            }
        }
    }

    fun updateChatNodes(updatedUserData: UserData) {
        db.collection(CHATS).where(Filter.or(
            Filter.equalTo("user1.userId", updatedUserData.userId),
            Filter.equalTo("user2.userId", updatedUserData.userId)
        )).get().addOnSuccessListener { chatSnapshot ->
            chatSnapshot.documents.forEach { chatDocument ->
                val chatData = chatDocument.toObject<ChatData>()
                val updatedChat = chatData?.copy(
                    user1 = if (chatData.user1.userId == updatedUserData.userId) {
                        ChatUser(
                            userId = updatedUserData.userId,
                            name = updatedUserData.name,
                            imageUrl = updatedUserData.imageUrl,
                            number = updatedUserData.number,
                            location = updatedUserData.location
                        )
                    } else chatData.user1,
                    user2 = if (chatData.user2.userId == updatedUserData.userId) {
                        ChatUser(
                            userId = updatedUserData.userId,
                            name = updatedUserData.name,
                            imageUrl = updatedUserData.imageUrl,
                            number = updatedUserData.number,
                            location = updatedUserData.location
                        )
                    } else chatData.user2
                )

                updatedChat?.let {
                    db.collection(CHATS).document(chatDocument.id).set(it)
                        .addOnSuccessListener {
                            Log.d("Live ChatApp", "Chat node updated successfully for user ${updatedUserData.userId}")
                        }
                        .addOnFailureListener { exception ->
                            handleException(exception, "Failed to update chat node for user ${updatedUserData.userId}")
                        }
                }
            }
        }.addOnFailureListener { exception ->
            handleException(exception, "Failed to retrieve chats for updating")
        }
    }
    private fun getUserData(uid: String) {
        inProcess.value = true
        db.collection(USER_NODE).document(uid).addSnapshotListener{
            value, error->
            if (error!=null){
                handleException(error, "Cannot retrieve  User")
            }

            if(value!=null){
                var user = value.toObject<UserData>()
                userData.value = user
                inProcess.value = false
                populateChats()
            }
        }
    }

    fun handleException(exception: Exception?=null, customMessage: String=""){
        Log.e("Live ChatApp", "Live Chat Exception", exception)
        exception?.printStackTrace()
        val errorMsg = exception?.localizedMessage?:""
        val message = if(customMessage.isNullOrEmpty()) errorMsg else customMessage
        eventMutableState.value = Event(message)
        inProcess.value = false
    }
    fun logout(){
        auth.signOut()
        signIn.value=false
        userData.value = null
        eventMutableState.value = Event("Logged Out")
    }

    fun onAddChat(number: String) {
        if (number.isEmpty() or ! number.isDigitsOnly()){
            handleException(customMessage = "Number must be Digits")
        }else{
            db.collection(CHATS).where(Filter.or(
                Filter.and(
                    Filter.equalTo("user1.number", number),
                    Filter.equalTo("user2.number", userData.value?.number)
                ),
                Filter.and(
                    Filter.equalTo("user1.number", userData.value?.number),
                    Filter.equalTo("user2.number", number)
                )
            )).get().addOnSuccessListener {
                if (it.isEmpty){
                    db.collection(USER_NODE).whereEqualTo("number", number).get().addOnSuccessListener {
                        if(it.isEmpty){
                            handleException(customMessage = "Number not found")
                        }else{
                            val chatPartner = it.toObjects<UserData>()[0]
                            val id = db.collection(CHATS).document().id
                            val chat = ChatData(
                                chatId = id,
                                ChatUser(
                                    userData.value?.userId,
                                    userData.value?.name,
                                    userData.value?.imageUrl,
                                    userData.value?.number,
                                    userData.value?.location
                                ),
                                ChatUser(chatPartner.userId,chatPartner.name, chatPartner.imageUrl, chatPartner.number, chatPartner.location)
                            )
                            db.collection(CHATS).document(id).set(chat)
                        }
                    }
                        .addOnFailureListener{
                            handleException(it)
                        }
                }else{
                    handleException(customMessage = "Chats Already Exists")
                }
            }
        }

    }
    fun onSendReply(chatID: String, message: String){
        val time = Calendar.getInstance().time.toString()
        val msg =
            Message(userData.value?.userId, message, time)
        db.collection(CHATS).document(chatID).collection(MESSAGE).document().set(msg)

    }

    fun signUpValidation(number: String): Boolean{
        if (number.length<10){
            return false
        }
        return true


    }
}
