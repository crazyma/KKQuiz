package com.beibeilab.kkquiz

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.ActivityOptions
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import kotlinx.android.synthetic.main.activity_landing.*
import android.transition.Slide
import android.transition.Fade
import android.support.v4.app.ActivityOptionsCompat
import android.view.Window


class LandingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        setContentView(R.layout.activity_landing)
//        setupWindowAnimations()

        YoYo.with(Techniques.RubberBand)
                .duration(1500)
                .delay(500)
                .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                .withListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(p0: Animator?) {
                        jump2Main()
                    }
                })
                .playOn(imageIcon)
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    private fun jump2Main(){

        val intent = Intent()
        intent.setClass(this, MainActivity::class.java)

        ActivityCompat.startActivity(this, intent,ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
    }

    private fun setupWindowAnimations() {
        val fadeIn = Fade()
        fadeIn.duration = 2000
        window.enterTransition = fadeIn

        val fadeOut = Fade()
        fadeOut.duration = 2000
        window.exitTransition = fadeOut
    }
}
