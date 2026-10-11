package com.example.service.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class VoiceReminderService(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _lastSpokenMessage = MutableStateFlow("")
    val lastSpokenMessage: StateFlow<String> = _lastSpokenMessage.asStateFlow()

    init {
        tts = TextToSpeech(context.applicationContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val arLocale = Locale("ar")
            val result = tts?.setLanguage(arLocale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.w("WanasVoice", "Arabic language missing or not supported on this device TTS engine, falling back to default")
                tts?.setLanguage(Locale.getDefault())
            }
            // Seniors understand best at slightly slower, clearer pitch & speech rate
            tts?.setSpeechRate(0.85f)
            tts?.setPitch(1.0f)
            isInitialized = true

            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _isSpeaking.value = true
                }

                override fun onDone(utteranceId: String?) {
                    _isSpeaking.value = false
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    _isSpeaking.value = false
                }
            })
        } else {
            Log.e("WanasVoice", "Failed to initialize TTS")
        }
    }

    fun speak(text: String) {
        _lastSpokenMessage.value = text
        if (isInitialized && tts != null) {
            _isSpeaking.value = true
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "WanasSpeechId_${System.currentTimeMillis()}")
        }
    }

    fun stop() {
        tts?.stop()
        _isSpeaking.value = false
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }

    // Helper functions for common senior audio prompts
    fun speakGreeting() {
        speak("أهلاً بك يا غالي في ونس. رفيقك الذكي ديما معاك. صحتك وراحتك هي أمانتنا، نتمنى لك يوماً مباركاً وصحة دائمة.")
    }

    fun speakMedicineReminder(medicineName: String, dosage: String, mealTiming: String, instructions: String) {
        val extra = if (instructions.isNotBlank()) ". وتذكر: $instructions" else ""
        speak("تذكير بموعد الدواء: حان وقت أخذ دواء $medicineName. الجرعة: $dosage، $mealTiming $extra. صحة وعافية يا رب.")
    }

    fun speakMedicineConfirmed(medicineName: String) {
        speak("ما شاء الله، تم تأكيد أخذ دواء $medicineName. عافية على قلبك يا غالي، ومستمرون في متابعة صحتك.")
    }

    fun speakDailySummary(pendingCount: Int, completedCount: Int) {
        if (pendingCount == 0) {
            speak("أحسنت صنعاً! لقد تناولت جميع أدويتك المحددة لهذا اليوم بنجاح، ونس فخور بالتزامك.")
        } else {
            speak("يا غالي، لقد أخذت $completedCount دواء اليوم، وما زال لديك $pendingCount متبقين. نسأل الله لك دوام العافية.")
        }
    }

    fun speakSosTriggered(caregiverName: String) {
        speak("تم تفعيل نداء المساعدة. يتم الآن التواصل مع $caregiverName للاطمئنان عليك وتقديم الدعم فوراً.")
    }
}
