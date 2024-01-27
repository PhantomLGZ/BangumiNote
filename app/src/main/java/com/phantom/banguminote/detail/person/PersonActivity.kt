package com.phantom.banguminote.detail.person

import com.phantom.banguminote.base.BaseActivity
import com.phantom.banguminote.databinding.ActivityPersonBinding

class PersonActivity : BaseActivity<ActivityPersonBinding>() {

    override fun inflateViewBinding(): ActivityPersonBinding =
        ActivityPersonBinding.inflate(layoutInflater)

}