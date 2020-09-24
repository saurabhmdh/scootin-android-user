package com.scootin.extensions

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.scootin.R

fun View.slideUpDownVisibility(isVisible: Boolean) {
    this.updateVisibility(isVisible)
    val slideAnimation = if (isVisible) {
        AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom)
    } else {
        AnimationUtils.loadAnimation(context, R.anim.slide_out_bottom)
    }
    this.startAnimation(slideAnimation)
}

fun View.animSlideUpVisibility(isVisible: Boolean) {
    this.updateVisibility(isVisible)
    val slideAnimation = if (isVisible) {
        AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom)
    } else {
        return
    }
    this.startAnimation(slideAnimation)
}

fun View.animSlideDownVisibility(visibility: Boolean) {
    val slideAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_out_bottom)
    slideAnimation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationEnd(animation: Animation?) {
            updateVisibility(visibility)
        }

        override fun onAnimationRepeat(animation: Animation?) {
        }

        override fun onAnimationStart(animation: Animation?) {
        }
    })
    this.startAnimation(slideAnimation)
}

fun View.updateVisibleWithAlpha() {
    animate().alpha(1.0f).setDuration(600).start()
}

fun View.makeInvisibleWithAlpha() {
    animate().alpha(0f).setDuration(600).start()
}