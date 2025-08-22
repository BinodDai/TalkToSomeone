package com.binod.talktosomeone.domain.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class Profile(
    @DocumentId
    val docId: String = "",

    @PropertyName("userId")
    val userId: String = "",

    @PropertyName("codeName")
    val codeName: String = "",

    @PropertyName("age")
    val age: Int = 0,

    @PropertyName("gender")
    val gender: String = "",

    @PropertyName("deviceId")
    val deviceId: String = "",

    @PropertyName("online")
    val online: Boolean = false,

    @PropertyName("lastSeen")
    val lastSeen: Long? = null,

    @PropertyName("typingTo")
    val typingTo: String? = null,

    @PropertyName("accountCreatedDate")
    val accountCreatedDate: Long = System.currentTimeMillis()
)

