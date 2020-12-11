package com.appchon.app.data.local

import android.content.Context
import android.content.SharedPreferences
import com.appchon.app.data.model.ActionState
import com.appchon.app.data.model.AuthUser
import com.appchon.app.util.getObject
import com.appchon.app.util.putObject

class AuthPref(val context: Context) {
    private val sp: SharedPreferences by lazy {
        context.getSharedPreferences(AuthPref::class.java.name, Context.MODE_PRIVATE)
    }

    private companion object {
        const val AUTH_USER = "auth_user"
        const val IS_LOGIN = "is_login"
    }

    var authUser: AuthUser?
    get() = sp.getObject(AUTH_USER)
    private set(value) = sp.edit().putObject(AUTH_USER, value).apply()

    var isLogin: Boolean
    get() = sp.getBoolean(IS_LOGIN, false)
    private set(value) = sp.edit().putBoolean(IS_LOGIN, value).apply()

    suspend fun login(email: String, password: String): ActionState<AuthUser> {
        val user = authUser
        return if (user == null) {
            ActionState(message = "You are not registered", isSuccess = false)
        } else if (email.isBlank() || password.isBlank()) {
            ActionState(message = "Email and Password cannot be empty", isSuccess = false)
        } else if (user.email == email && user.password == password) {
            isLogin = true
            ActionState(authUser, message = "You have successfully logged in!")
        } else {
            ActionState(message = "Email or Password wrong", isSuccess = false)
        }
    }

    suspend fun register(user: AuthUser): ActionState<AuthUser> {
        return if (user.email.isBlank() || user.password.isBlank()) {
            ActionState(message = "Email and Password cannot be empty", isSuccess = false)
        } else {
            authUser = user
            ActionState(user, message = "You have successfully registered!")
        }
    }

    suspend fun logout(): ActionState<Boolean> {
        isLogin = false
        return ActionState(true, message = "You have successfully logged out!")
    }
}