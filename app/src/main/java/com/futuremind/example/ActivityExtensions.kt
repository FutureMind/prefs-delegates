package com.futuremind.example

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.widget.Toast


inline fun <reified T : ViewModel> AppCompatActivity.initViewModel(factory: ViewModelProvider.Factory) =
        ViewModelProviders.of(this, factory).get(T::class.java)

inline fun <reified T : Activity> Context.startActivity() = startActivity(Intent(this, T::class.java))

fun Context.showToast(message: Int, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, message, duration).show()

