package com.phantom.banguminote.detail.character

import com.phantom.banguminote.base.BaseActivity
import com.phantom.banguminote.databinding.ActivityCharacterBinding

class CharacterActivity: BaseActivity<ActivityCharacterBinding>() {

    override fun inflateViewBinding(): ActivityCharacterBinding =
        ActivityCharacterBinding.inflate(layoutInflater)

}