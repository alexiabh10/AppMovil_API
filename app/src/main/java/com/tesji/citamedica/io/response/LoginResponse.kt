package com.tesji.citamedica.io.response

import com.tesji.citamedica.model.User

data class LoginResponse(
    val success: Boolean,
    val user: User,
    val jwt:String
)
