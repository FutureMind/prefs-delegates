package com.futuremind.example

import android.arch.lifecycle.ViewModelProvider
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import com.facebook.stetho.Stetho
import com.futuremind.daggerutils.Injectable
import com.futuremind.preferencesdelegates.PrefsObserver
import javax.inject.Inject

class MainActivity : AppCompatActivity(), Injectable {

    /*@Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MainViewModel by lazy { initViewModel<MainViewModel>(viewModelFactory) }*/

    private lateinit var testStore: TestStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*(application as App).appComponent.inject(this)
        viewModel.testStore.saveToken.observe().subscribe {
            Toast.makeText(this, "Token saved: $it", Toast.LENGTH_SHORT).show()
        }*/
        Stetho.initializeWithDefaults(this)

        val sharedPrefs =  PreferenceManager.getDefaultSharedPreferences(this)
        val prefsObserver = PrefsObserver(sharedPrefs, String::class.java)
        testStore = TestStore(prefsObserver)
        testStore.saveToken.observe().subscribe {
            Toast.makeText(this, "Token saved: $it", Toast.LENGTH_LONG).show()
        }
        testStore.saveToken.prefsDelegate = "test_token"


        Toast.makeText(this, "Token get: ${testStore.saveToken.prefsDelegate}", Toast.LENGTH_LONG).show()
    }

    override fun onStart() {
        super.onStart()
        //viewModel.saveToken("test_token")
    }
}
