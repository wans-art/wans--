package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.WanasDatabase
import com.example.data.model.CaregiverEntity
import com.example.data.model.DailyTaskEntity
import com.example.data.model.HealthVitalEntity
import com.example.data.model.MedicineEntity
import com.example.data.model.MedicineGuideItem
import com.example.data.repository.WanasRepository
import com.example.service.tts.VoiceReminderService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class WanasScreen(val titleArabic: String, val iconName: String) {
    TODAY("اليوم", "today"),
    MEDICINES("صيدليتي", "meds"),
    TASKS("مهامي", "tasks"),
    MEDICAL_RECORD("السجل الطبي", "record"),
    GUIDE("دليل العلاج", "guide"),
    FAMILY("الأهل والرعاية", "family")
}

enum class UserRoleMode(val titleArabic: String) {
    SENIOR("وضع الوالد/الوالدة"),
    CAREGIVER("وضع المرافق / الأهل")
}

class WanasViewModel(application: Application) : AndroidViewModel(application) {

    private val database = WanasDatabase.getDatabase(application, viewModelScope)
    private val repository = WanasRepository(database.wanasDao())
    val voiceService = VoiceReminderService(application)

    val medicines: StateFlow<List<MedicineEntity>> = repository.allMedicines.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val tasks: StateFlow<List<DailyTaskEntity>> = repository.allTasks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val caregivers: StateFlow<List<CaregiverEntity>> = repository.allCaregivers.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val vitals: StateFlow<List<HealthVitalEntity>> = repository.recentVitals.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val medicalProfile: StateFlow<com.example.data.model.MedicalProfileEntity> = repository.medicalProfile.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = com.example.data.model.MedicalProfileEntity()
    ).let { flow ->
        // Ensure non-null fallback
        kotlinx.coroutines.flow.MutableStateFlow(com.example.data.model.MedicalProfileEntity()).also { stateFlow ->
            viewModelScope.launch {
                flow.collect { profile ->
                    if (profile != null) {
                        stateFlow.value = profile
                    }
                }
            }
        }
    }

    val medicineGuide: List<MedicineGuideItem> = repository.getMedicineKnowledgeBase()

    private val _currentScreen = MutableStateFlow(WanasScreen.TODAY)
    val currentScreen: StateFlow<WanasScreen> = _currentScreen.asStateFlow()

    private val _userRoleMode = MutableStateFlow(UserRoleMode.SENIOR)
    val userRoleMode: StateFlow<UserRoleMode> = _userRoleMode.asStateFlow()

    private val _fontScale = MutableStateFlow(1.15f) // Default comfortably large for seniors
    val fontScale: StateFlow<Float> = _fontScale.asStateFlow()

    private val _waterCupsDrank = MutableStateFlow(4)
    val waterCupsDrank: StateFlow<Int> = _waterCupsDrank.asStateFlow()

    private val _snoozedMessage = MutableStateFlow<String?>(null)
    val snoozedMessage: StateFlow<String?> = _snoozedMessage.asStateFlow()

    val isVoiceSpeaking: StateFlow<Boolean> = voiceService.isSpeaking

    fun selectScreen(screen: WanasScreen) {
        _currentScreen.value = screen
    }

    fun toggleRoleMode() {
        _userRoleMode.value = if (_userRoleMode.value == UserRoleMode.SENIOR) {
            UserRoleMode.CAREGIVER
        } else {
            UserRoleMode.SENIOR
        }
    }

    fun setFontScale(scale: Float) {
        _fontScale.value = scale
    }

    // --- Medicine Actions ---
    fun markMedicineTaken(medicine: MedicineEntity) {
        viewModelScope.launch {
            val timeFormat = SimpleDateFormat("hh:mm a", Locale("ar"))
            val currentTime = timeFormat.format(Date())
            repository.markMedicineTaken(medicine.id, true, currentTime)
            voiceService.speakMedicineConfirmed(medicine.name)
        }
    }

    fun unmarkMedicineTaken(medicine: MedicineEntity) {
        viewModelScope.launch {
            repository.markMedicineTaken(medicine.id, false, null)
        }
    }

    fun snoozeMedicine(medicine: MedicineEntity, minutes: Int = 15) {
        val msg = "تم تأجيل موعد دواء ${medicine.name} لمدة $minutes دقيقة"
        _snoozedMessage.value = msg
        voiceService.speak("حاضر يا غالي، سأذكرك بتناول ${medicine.name} بعد $minutes دقيقة. خذ وقتك وارتح.")
    }

    fun dismissSnoozeMessage() {
        _snoozedMessage.value = null
    }

    fun speakMedicine(medicine: MedicineEntity) {
        voiceService.speakMedicineReminder(
            medicineName = medicine.name,
            dosage = medicine.dosage,
            mealTiming = medicine.mealTiming,
            instructions = "${medicine.purpose}. ${medicine.instructions}"
        )
    }

    fun speakGreeting() {
        voiceService.speakGreeting()
    }

    fun speakSummary() {
        val currentMeds = medicines.value
        val taken = currentMeds.count { it.isTakenToday }
        val pending = currentMeds.size - taken
        voiceService.speakDailySummary(pending, taken)
    }

    fun stopVoice() {
        voiceService.stop()
    }

    fun addNewMedicine(
        name: String,
        purpose: String,
        dosage: String,
        form: String,
        colorHex: Long,
        timeOfDay: String,
        scheduledTime: String,
        mealTiming: String,
        totalPills: Int,
        instructions: String,
        doctorNotes: String
    ) {
        viewModelScope.launch {
            val newMed = MedicineEntity(
                name = name,
                purpose = purpose,
                dosage = dosage,
                form = form,
                colorHex = colorHex,
                timeOfDay = timeOfDay,
                scheduledTime = scheduledTime,
                mealTiming = mealTiming,
                totalPills = totalPills,
                remainingPills = totalPills,
                instructions = instructions,
                doctorNotes = doctorNotes,
                isTakenToday = false
            )
            repository.insertMedicine(newMed)
            voiceService.speak("تم إضافة دواء $name بنجاح إلى جدولك.")
        }
    }

    fun deleteMedicine(medicine: MedicineEntity) {
        viewModelScope.launch {
            repository.deleteMedicine(medicine)
        }
    }

    // --- Daily Tasks ---
    fun toggleTask(task: DailyTaskEntity) {
        viewModelScope.launch {
            val newState = !task.isCompleted
            repository.setTaskCompleted(task.id, newState)
            if (newState) {
                voiceService.speak("ما شاء الله، أنجزت ${task.title}. بارك الله في صحتك.")
            }
        }
    }

    fun addNewTask(title: String, category: String, scheduledTime: String, note: String) {
        viewModelScope.launch {
            val newTask = DailyTaskEntity(
                title = title,
                category = category,
                scheduledTime = scheduledTime,
                isCompleted = false,
                note = note
            )
            repository.insertTask(newTask)
        }
    }

    fun deleteTask(task: DailyTaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun incrementWater() {
        if (_waterCupsDrank.value < 10) {
            _waterCupsDrank.value += 1
            if (_waterCupsDrank.value % 2 == 0) {
                voiceService.speak("هنيئاً ومريئاً! شربت ${_waterCupsDrank.value} أكواب ماء. الماء يمنحك النشاط والحيوية.")
            }
        }
    }

    fun decrementWater() {
        if (_waterCupsDrank.value > 0) {
            _waterCupsDrank.value -= 1
        }
    }

    // --- Health Vitals ---
    fun addVitalRecord(type: String, val1: String, val2: String?, statusText: String) {
        viewModelScope.launch {
            val timeFormat = SimpleDateFormat("dd/MM hh:mm a", Locale("ar"))
            val vital = HealthVitalEntity(
                type = type,
                value1 = val1,
                value2 = val2,
                statusText = statusText,
                recordedAt = timeFormat.format(Date())
            )
            repository.insertVital(vital)
            voiceService.speak("تم تسجيل قراءة $type بنجاح. حالتك الصحية: $statusText.")
        }
    }

    // --- Caregivers & Emergency SOS ---
    fun addNewCaregiver(name: String, relation: String, phone: String, isEmergency: Boolean) {
        viewModelScope.launch {
            val caregiver = CaregiverEntity(
                name = name,
                relationship = relation,
                phone = phone,
                isEmergencyContact = isEmergency
            )
            repository.insertCaregiver(caregiver)
        }
    }

    fun deleteCaregiver(caregiver: CaregiverEntity) {
        viewModelScope.launch {
            repository.deleteCaregiver(caregiver)
        }
    }

    fun triggerEmergencyCall(context: Context, caregiver: CaregiverEntity?) {
        val targetCaregiver = caregiver ?: caregivers.value.firstOrNull { it.isEmergencyContact }
        if (targetCaregiver != null) {
            voiceService.speakSosTriggered(targetCaregiver.name)
            try {
                val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${targetCaregiver.phone}")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(dialIntent)
            } catch (e: Exception) {
                Toast.makeText(context, "تعذر فتح لوحة الاتصال: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            voiceService.speak("يرجى إضافة رقم أحد أفراد الأسرة في شاشة الأهل للاتصال به عند الحاجة.")
            Toast.makeText(context, "لا يوجد جهة اتصال مسجلة للطوارئ", Toast.LENGTH_LONG).show()
        }
    }

    fun shareDailyComplianceReport(context: Context) {
        val currentMeds = medicines.value
        val takenCount = currentMeds.count { it.isTakenToday }
        val totalCount = currentMeds.size

        val reportBuilder = StringBuilder()
        reportBuilder.append("🟢 *تقرير تطبيق ونس اليومي لمتابعة الأهل*\n")
        val dateFormat = SimpleDateFormat("EEEE d MMMM yyyy", Locale("ar"))
        reportBuilder.append("📅 التاريخ: ${dateFormat.format(Date())}\n\n")
        reportBuilder.append("💊 *حالة الأدوية اليوم ($takenCount من أصل $totalCount تم تناولها):*\n")

        currentMeds.forEach { med ->
            if (med.isTakenToday) {
                reportBuilder.append("✅ ${med.name} (${med.dosage}) - تم تناوله الساعة ${med.lastTakenTime ?: "اليوم"}\n")
            } else {
                reportBuilder.append("⏳ ${med.name} (${med.dosage}) - موعده ${med.scheduledTime} (${med.mealTiming})\n")
            }
        }

        reportBuilder.append("\n💧 *الماء والصحة العامة:*\n")
        reportBuilder.append("- تم شرب ${_waterCupsDrank.value} أكواب ماء.\n")
        val lastVital = vitals.value.firstOrNull()
        if (lastVital != null) {
            val v2 = if (lastVital.value2 != null) "/${lastVital.value2}" else ""
            reportBuilder.append("- آخر قياس: ${lastVital.type} (${lastVital.value1}$v2) - الحالة: ${lastVital.statusText}\n")
        }
        reportBuilder.append("\n_تم الإرسال بواسطة تطبيق ونس - رفيقك الذكي ديما معاك_")

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, reportBuilder.toString())
            type = "text/plain"
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val shareIntent = Intent.createChooser(sendIntent, "مشاركة تقرير ونس مع الأهل")
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(shareIntent)
    }

    // --- Medical Profile (السجل الطبي للمستخدم) ---
    fun updateMedicalProfile(updated: com.example.data.model.MedicalProfileEntity) {
        viewModelScope.launch {
            repository.saveMedicalProfile(updated)
            voiceService.speak("تم حفظ وتحديث سجلك الطبي في ونس بنجاح.")
        }
    }

    fun speakMedicalProfile() {
        val profile = medicalProfile.value
        val speech = buildString {
            append("سجلك الطبي في ونس: ")
            append("الاسم: ${profile.fullName}، العمر: ${profile.age}، فصيلة الدم: ${profile.bloodType}. ")
            append("الأمراض المزمنة: ${profile.chronicConditions}. ")
            append("تحذيرات الحساسية الدوائية والغذائية: ${profile.allergies}. ")
            append("الطبيب المعالج: ${profile.doctor1Name}، تخصص ${profile.doctor1Specialty}. ")
            append("نسأل الله لك دوام الصحة والعافية.")
        }
        voiceService.speak(speech)
    }

    fun callDoctor(context: Context, phone: String, doctorName: String) {
        voiceService.speak("جاري الاتصال بالطبيب $doctorName")
        try {
            val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phone")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(dialIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "تعذر فتح الاتصال: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun shareMedicalProfile(context: Context) {
        val profile = medicalProfile.value
        val cardText = buildString {
            append("📋 *البطاقة الطبية والسجل الصحي - تطبيق وَنَس*\n\n")
            append("👤 *الاسم:* ${profile.fullName}\n")
            append("🎂 *العمر:* ${profile.age}\n")
            append("🩸 *فصيلة الدم:* ${profile.bloodType}\n")
            append("🏥 *المستشفى:* ${profile.hospitalName}\n")
            append("🛡️ *التأمين:* ${profile.nationalIdOrInsurance}\n")
            append("📞 *رقم الطوارئ:* ${profile.emergencyPhone}\n\n")
            append("⚠️ *تحذيرات الحساسية (حرج):*\n${profile.allergies}\n\n")
            append("🩺 *الأمراض المزمنة التشخيصية:*\n${profile.chronicConditions}\n\n")
            append("👨‍⚕️ *الأطباء المعالجون:*\n")
            append("1. ${profile.doctor1Name} (${profile.doctor1Specialty}) - ${profile.doctor1Phone}\n")
            if (profile.doctor2Name.isNotBlank()) {
                append("2. ${profile.doctor2Name} (${profile.doctor2Specialty}) - ${profile.doctor2Phone}\n")
            }
            if (profile.dietaryRestrictions.isNotBlank()) {
                append("\n🥗 *الحمية الغذائية:* ${profile.dietaryRestrictions}\n")
            }
            if (profile.generalNotes.isNotBlank()) {
                append("\n📝 *ملاحظات عامة:* ${profile.generalNotes}\n")
            }
            append("\n_تم الاستخراج عبر تطبيق ونس رفيقك الذكي لكبار السن_")
        }

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, cardText)
            type = "text/plain"
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val chooser = Intent.createChooser(sendIntent, "مشاركة السجل الطبي مع الطبيب أو الطوارئ")
        chooser.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(chooser)
    }

    override fun onCleared() {
        super.onCleared()
        voiceService.shutdown()
    }
}
