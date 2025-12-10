package com.example.mywhatsapp.model

import androidx.annotation.DrawableRes

data class Contact(
    val id: Int,
    @DrawableRes val contactImage: Int,
    val name: String,
    val anime: String
)