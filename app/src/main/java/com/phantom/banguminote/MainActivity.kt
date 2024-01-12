package com.phantom.banguminote

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.phantom.banguminote.base.BaseActivity
import com.phantom.banguminote.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun inflateViewBinding(): ActivityMainBinding =
        ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.run {
            bottomNavigation.setupWithNavController(findNavController(R.id.navHostFragment))
        }
    }

}