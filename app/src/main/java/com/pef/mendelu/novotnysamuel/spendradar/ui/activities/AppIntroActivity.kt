package com.pef.mendelu.novotnysamuel.spendradar.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.pef.mendelu.novotnysamuel.spendradar.R

@AndroidEntryPoint
class AppIntroActivity : AppIntro() {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, AppIntroActivity::class.java)
        }
    }

    private val viewModel: AppIntroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showAppIntro()
    }

    private fun showAppIntro() {
        addSlide(
            AppIntroFragment.newInstance(
                title = getString(R.string.welcome_to) + " " + getString(R.string.app_name),
                description = getString(R.string.intro_text_1),
                titleColor = ContextCompat.getColor(this, R.color.white),
                descriptionColor = ContextCompat.getColor(this, R.color.white),
                imageDrawable = R.drawable.undraw_receipt1,
                backgroundDrawable = R.drawable.gradient)
        )
        addSlide(
            AppIntroFragment.newInstance(
                title = getString(R.string.intro2),
                description = getString(R.string.intro_text_2),
                titleColor = ContextCompat.getColor(this, R.color.white),
                descriptionColor = ContextCompat.getColor(this, R.color.white),
                imageDrawable = R.drawable.undraw_location,
                backgroundDrawable = R.drawable.gradient
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                title = getString(R.string.intro3),
                description = getString(R.string.intro_text_3),
                titleColor = ContextCompat.getColor(this, R.color.white),
                descriptionColor = ContextCompat.getColor(this, R.color.white),
                imageDrawable = R.drawable.undraw_ai3,
                backgroundDrawable = R.drawable.gradient
            )
        )

        showSeparator(false)
        showStatusBar(false)
        setImmersiveMode()
        setColorDoneText(ContextCompat.getColor(this, R.color.white))
        setDoneText(getString(R.string.continue_to_app))
        setSkipText(getString(R.string.skip))
        setIndicatorColor(
            ContextCompat.getColor(this, R.color.white),
            ContextCompat.getColor(this, android.R.color.darker_gray))
        setNextArrowColor(ContextCompat.getColor(this, R.color.white))
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        continueToMainActivity()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        continueToMainActivity()
    }

    private fun continueToMainActivity() {
        lifecycleScope.launch {
            viewModel.setFirstRun()
        }.invokeOnCompletion {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}