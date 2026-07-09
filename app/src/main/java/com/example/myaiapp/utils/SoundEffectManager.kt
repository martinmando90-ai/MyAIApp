
// ============================================================
// SoundEffectManager.kt
// إدارة المؤثرات الصوتية
// ============================================================

package com.example.myaiapp.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import com.example.myaiapp.R

class SoundEffectManager(context: Context) {

    private val soundPool: SoundPool
    private val sounds = mutableMapOf<String, Int>()
    private val loadedSounds = mutableMapOf<Int, Boolean>()

    init {
        // إنشاء SoundPool مع AudioAttributes
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(10)
            .setAudioAttributes(audioAttributes)
            .build()

        // تحميل المؤثرات الصوتية
        loadSounds(context)

        // الاستماع لتحميل الصوت
        soundPool.setOnLoadCompleteListener { _, sampleId, status ->
            loadedSounds[sampleId] = status == 0
        }
    }

    private fun loadSounds(context: Context) {
        sounds["send"] = soundPool.load(context, R.raw.sfx_send, 1)
        sounds["receive"] = soundPool.load(context, R.raw.sfx_receive, 1)
        sounds["error"] = soundPool.load(context, R.raw.sfx_error, 1)
        sounds["success"] = soundPool.load(context, R.raw.sfx_success, 1)
        sounds["typing"] = soundPool.load(context, R.raw.sfx_typing, 1)
        sounds["notification"] = soundPool.load(context, R.raw.sfx_notification, 1)
        sounds["search"] = soundPool.load(context, R.raw.sfx_search, 1)
        sounds["record_start"] = soundPool.load(context, R.raw.sfx_record_start, 1)
        sounds["record_stop"] = soundPool.load(context, R.raw.sfx_record_stop, 1)
        sounds["reminder"] = soundPool.load(context, R.raw.sfx_reminder, 1)
    }

    /**
     * تشغيل مؤثر صوتي
     */
    fun play(soundName: String, volume: Float = 1.0f) {
        sounds[soundName]?.let { soundId ->
            if (loadedSounds[soundId] == true) {
                soundPool.play(soundId, volume, volume, 1, 0, 1.0f)
            }
        }
    }

    /**
     * تشغيل صوت الإرسال
     */
    fun playSend() = play("send", 0.5f)

    /**
     * تشغيل صوت الاستلام
     */
    fun playReceive() = play("receive", 0.5f)

    /**
     * تشغيل صوت الخطأ
     */
    fun playError() = play("error", 0.7f)

    /**
     * تشغيل صوت النجاح
     */
    fun playSuccess() = play("success", 0.6f)

    /**
     * تشغيل صوت الكتابة
     */
    fun playTyping() = play("typing", 0.3f)

    /**
     * تشغيل صوت الإشعار
     */
    fun playNotification() = play("notification", 0.8f)

    /**
     * تشغيل صوت البحث
     */
    fun playSearch() = play("search", 0.4f)

    /**
     * تشغيل صوت بدء التسجيل
     */
    fun playRecordStart() = play("record_start", 0.5f)

    /**
     * تشغيل صوت إيقاف التسجيل
     */
    fun playRecordStop() = play("record_stop", 0.5f)

    /**
     * تشغيل صوت التذكير
     */
    fun playReminder() = play("reminder", 1.0f)

    /**
     * تحرير الموارد
     */
    fun release() {
        soundPool.release()
    }
}

// ============================================================
// الاستخدام في ChatViewModel:
// ============================================================

/*
class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val soundManager = SoundEffectManager(application)
    private val haptic = HapticFeedbackManager(application)

    fun sendMessage(message: String) {
        // اهتزاز + صوت
        haptic.mediumTap()
        soundManager.playSend()

        // إرسال الرسالة...
    }

    fun onAIResponse(response: String) {
        // اهتزاز نجاح + صوت استلام
        haptic.success()
        soundManager.playReceive()

        // عرض الرد...
    }

    fun onError(error: String) {
        // اهتزاز خطأ + صوت خطأ
        haptic.error()
        soundManager.playError()

        // عرض الخطأ...
    }

    fun startVoiceRecording() {
        haptic.heavyTap()
        soundManager.playRecordStart()

        // بدء التسجيل...
    }

    fun stopVoiceRecording() {
        haptic.lightTap()
        soundManager.playRecordStop()

        // إيقاف التسجيل...
    }

    override fun onCleared() {
        super.onCleared()
        soundManager.release()
    }
}
*/
