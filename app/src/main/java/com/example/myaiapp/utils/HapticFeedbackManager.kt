
// ============================================================
// HapticFeedbackManager.kt
// إدارة الاهتزازات في التطبيق
// ============================================================

package com.example.myaiapp.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.HapticFeedbackConstants
import android.view.View

class HapticFeedbackManager(private val context: Context) {

    private val vibrator: Vibrator by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    /**
     * اهتزاز خفيف - لما تضغط على زر
     */
    fun lightTap() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(10, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(10)
        }
    }

    /**
     * اهتزاز متوسط - لما ترسل رسالة
     */
    fun mediumTap() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(30, 100))
        } else {
            vibrator.vibrate(30)
        }
    }

    /**
     * اهتزاز قوي - لما يجيلك إشعار
     */
    fun heavyTap() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, 200))
        } else {
            vibrator.vibrate(50)
        }
    }

    /**
     * اهتزاز نجاح - لما العملية تنجح
     */
    fun success() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // نمط: قصير - طويل
            val pattern = longArrayOf(0, 30, 50, 60)
            val amplitudes = intArrayOf(0, 100, 0, 150)
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, amplitudes, -1))
        } else {
            vibrator.vibrate(longArrayOf(0, 30, 50, 60), -1)
        }
    }

    /**
     * اهتزاز خطأ - لما يحصل خطأ
     */
    fun error() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // نمط: سريع متكرر
            val pattern = longArrayOf(0, 20, 30, 20, 30, 20)
            val amplitudes = intArrayOf(0, 150, 0, 150, 0, 150)
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, amplitudes, -1))
        } else {
            vibrator.vibrate(longArrayOf(0, 20, 30, 20, 30, 20), -1)
        }
    }

    /**
     * اهتزاز كتابة - لما الـ AI بيكتب
     */
    fun typing() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(5, 50))
        }
    }

    /**
     * اهتزاز طويل - لما يجي تذكير
     */
    fun reminder() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val pattern = longArrayOf(0, 100, 200, 100, 200, 300)
            val amplitudes = intArrayOf(0, 150, 0, 150, 0, 200)
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, amplitudes, -1))
        } else {
            vibrator.vibrate(longArrayOf(0, 100, 200, 100, 200, 300), -1)
        }
    }

    /**
     * اهتزاز نقر مزدوج - لما تضغط ضغطة طويلة
     */
    fun doubleTap() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val pattern = longArrayOf(0, 20, 50, 20)
            val amplitudes = intArrayOf(0, 120, 0, 120)
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, amplitudes, -1))
        } else {
            vibrator.vibrate(longArrayOf(0, 20, 50, 20), -1)
        }
    }

    /**
     * اهتزاز باستخدام HapticFeedbackConstants (للأزرار)
     */
    fun performHaptic(view: View, feedbackType: Int = HapticFeedbackConstants.CONFIRM) {
        view.performHapticFeedback(feedbackType)
    }

    /**
     * اهتزاز مخصص بالكامل
     */
    fun customVibrate(pattern: LongArray, amplitudes: IntArray? = null, repeat: Int = -1) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && amplitudes != null) {
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, amplitudes, repeat))
        } else {
            vibrator.vibrate(pattern, repeat)
        }
    }
}

// ============================================================
// الاستخدام في التطبيق:
// ============================================================

/*
// في Activity أو Fragment:
val haptic = HapticFeedbackManager(context)

// لما تضغط زر الإرسال:
sendButton.setOnClickListener {
    haptic.mediumTap()
    // إرسال الرسالة...
}

// لما يجيلك رد من الـ AI:
fun onAIResponse() {
    haptic.success()
    playSound(R.raw.sfx_receive)
}

// لما يحصل خطأ:
fun onError() {
    haptic.error()
    playSound(R.raw.sfx_error)
}

// لما تبدأ تسجيل صوت:
micButton.setOnTouchListener { _, event ->
    when (event.action) {
        MotionEvent.ACTION_DOWN -> {
            haptic.heavyTap()
            startRecording()
        }
        MotionEvent.ACTION_UP -> {
            haptic.lightTap()
            stopRecording()
        }
    }
    true
}

// لما يجي تنبيه:
fun onReminder() {
    haptic.reminder()
    playSound(R.raw.sfx_reminder)
}
*/
