package com.demolotteryapp.ui.splash

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.demolotteryapp.R
import com.demolotteryapp.base.BaseActivity
import com.demolotteryapp.data.model.LotteryInfo
import com.demolotteryapp.databinding.SplashActivityBinding
import dagger.android.AndroidInjection
import javax.inject.Inject

class SplashActivity : BaseActivity() {

    /*
     * Inject the ViewModelFactory. The ViewModelFactory class
     * has a list of ViewModels and will provide
     * the corresponding ViewModel in this activity
     * */
    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    /*
    * This is our ViewModel class
    * */
    lateinit var lotteryViewModel: LotteryViewModel


    private lateinit var binding: SplashActivityBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        initialiseView()
        initialiseViewModel()
    }

    /*
     * Initialising the View using Data Binding
     * */
    private fun initialiseView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
    }

    /*
     * Step 3: Initialising the ViewModel class here.
     * We are adding the ViewModelFactory class here.
     * We are observing the LiveData
     * */
    private fun initialiseViewModel() {
        lotteryViewModel = ViewModelProviders.of(this, viewModelFactory).get(LotteryViewModel::class.java)

        lotteryViewModel.getLotteryLiveData().observe(this, Observer { resource ->
            if (resource!!.isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else if (resource.data != null) {
                binding.progressBar.visibility = View.INVISIBLE
                binding.tvText.text = "Drnow:" +  (resource.data as LotteryInfo).drwNo.toString()
            } else {
                //handleErrorResponse()
            }
        })

    }

    fun callApi(view: View){
        binding.tvText.text = "Start calling api"

        /* Fetch movies list  */
        lotteryViewModel.loadLotteryInfo(1)
    }
}
