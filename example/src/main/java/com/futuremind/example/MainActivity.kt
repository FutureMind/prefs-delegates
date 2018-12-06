package com.futuremind.example

import android.arch.lifecycle.ViewModelProvider
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.facebook.stetho.Stetho
import com.futuremind.daggerutils.Injectable
import javax.inject.Inject

class MainActivity : AppCompatActivity(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MainViewModel by lazy { initViewModel<MainViewModel>(viewModelFactory) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.observeTokenChange().subscribe {
            Toast.makeText(this, "Token saved: $it", Toast.LENGTH_SHORT).show()
        }
        Stetho.initializeWithDefaults(this)
    }

    override fun onStart() {
        super.onStart()
        viewModel.saveToken("test_token")
        viewModel.saveAge(26)
    }
}