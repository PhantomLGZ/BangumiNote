package com.phantom.banguminote.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.viewbinding.ViewBinding


abstract class BaseActivity<T : ViewBinding>() : AppCompatActivity() {

    lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflateViewBinding()
        setContentView(binding.root)
        val controller = WindowCompat.getInsetsController(window, binding.root)
        controller.isAppearanceLightStatusBars = true
        controller.isAppearanceLightNavigationBars = true
    }

    abstract fun inflateViewBinding(): T

    fun showToast(text: String?) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}