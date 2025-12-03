package com.example.mywhatsapp.model

import androidx.annotation.DrawableRes

data class Contact(
    @DrawableRes val contactImage: Int,
    val name: String,
    val anime: String
)