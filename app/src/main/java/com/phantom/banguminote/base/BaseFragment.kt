package com.phantom.banguminote.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<T : ViewBinding> : Fragment() {

    var binding: T? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflateViewBinding(inflater, container)
        init()
        return binding?.root
    }

    abstract fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?): T

    open fun init() {}

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    fun showToast(text: String?) {
        Toast.makeText(this.context, text, Toast.LENGTH_SHORT).show()
    }
}