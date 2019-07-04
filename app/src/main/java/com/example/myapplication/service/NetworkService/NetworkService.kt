package com.example.nowsudm_jirani.service.NetworkServiceN

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.view.View
import com.google.android.material.snackbar.Snackbar


object NetworkService {

    open class NetworkException(error: Throwable): RuntimeException(error)

    class NoNetworkException(error: Throwable): NetworkException(error)

    class ServerUnreachableException(error: Throwable): NetworkException(error)

    class HttpCallFailureException(error: Throwable): NetworkException(error)

    class MaxRetriesExceededException(error: Throwable): NetworkException(error)

    class TokenExpiredException(error: Throwable) : NetworkException(error)


    public fun displayError(error: Throwable,view : View) {
        when (error) {
            is NoNetworkException -> {
                Snackbar.make(view,"Please connect to a network", Snackbar.LENGTH_LONG).show()
                return
            }
            is ServerUnreachableException ->{
                Snackbar.make(view,"Cannot connect to server",Snackbar.LENGTH_LONG).show()
                return
            }
            is HttpCallFailureException ->{
                Snackbar.make(view,"Problem communicating with server",Snackbar.LENGTH_LONG).show()
                return
            }

            else -> {
                Snackbar.make(view, "Please try again later", Snackbar.LENGTH_LONG).show()
                return
            }
        }
    }



    fun checkIfHasNetwork(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val networkInfo = cm!!.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}