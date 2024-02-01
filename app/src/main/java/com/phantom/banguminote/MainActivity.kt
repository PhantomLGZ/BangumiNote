package com.phantom.banguminote

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.phantom.banguminote.base.BaseActivity
import com.phantom.banguminote.base.getUserToken
import com.phantom.banguminote.base.http.RetrofitHelper
import com.phantom.banguminote.base.setUserToken
import com.phantom.banguminote.databinding.ActivityMainBinding
import com.phantom.banguminote.me.login.LoginViewModel
import com.phantom.banguminote.me.login.data.AccessTokenReq
import com.phantom.banguminote.me.login.data.AccessTokenRes
import com.phantom.banguminote.me.login.data.GRANT_TYPE_REFRESH
import com.phantom.banguminote.me.login.data.TokenStatusRes
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val viewModel: LoginViewModel by viewModels()

    override fun inflateViewBinding(): ActivityMainBinding =
        ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RetrofitHelper.setAccessToken(getUserToken())
        binding.run {
            bottomNavigation.setupWithNavController(findNavController(R.id.navHostFragment))
        }
        viewModel.also { v ->
            v.error.observe(this) { showToast(it.message) }
            v.tokenStatusRes.observe(this) { checkToken(it) }
            v.accessTokenRes.observe(this) { refreshToken(it) }

            if (getUserToken().isNotBlank()) {
                v.tokenStatus()
            }
        }
    }

    private fun checkToken(res: TokenStatusRes) {
        if (Instant.ofEpochSecond(res.expires)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .isBefore(LocalDateTime.now())
        ) {
            viewModel.accessToken(
                AccessTokenReq(
                    grant_type = GRANT_TYPE_REFRESH,
                    refresh_token = getUserToken(),
                )
            )
        }
    }

    private fun refreshToken(res: AccessTokenRes) {
        setUserToken(res.refresh_token)
    }

}