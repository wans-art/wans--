package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity representing a senior's prescribed medication.
 */
@Entity(tableName = "medicines")
data class MedicineEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val purpose: String,               // دواعي الاستعمال (e.g. تنظيم ضغط الدم)
    val dosage: String,                // الجرعة (e.g. قرص واحد ٥٠ ملغ)
    val form: String,                  // كبسولة، قرص، شراب، قطرة، حقنة
    val colorHex: Long = 0xFF2E7D32,   // لون تمييز الدواء
    val timeOfDay: String,             // الصباح، الظهر، المساء، قبل النوم
    val scheduledTime: String,         // توقيت الجرعة (e.g. 08:00 AM)
    val mealTiming: String,            // بعد الأكل، قبل الأكل، مع الأكل، على الريق
    val totalPills: Int = 30,          // إجمالي الأقراص
    val remainingPills: Int = 30,      // الأقراص المتبقية
    val instructions: String = "",     // تعليمات خاصة (e.g. اشرب مع كوب ماء وفير)
    val doctorNotes: String = "",      // ملاحظات الطبيب
    val isTakenToday: Boolean = false, // هل تم تناوله اليوم
    val lastTakenTime: String? = null, // وقت التناول
    val isActive: Boolean = true
)

/**
 * Daily Task / Health habit (e.g. شرب ماء، قياس سكر، مشي، أذكار).
 */
@Entity(tableName = "daily_tasks")
data class DailyTaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: String,             // ماء، قياسات، نشاط، عبادة، موعد
    val scheduledTime: String,
    val isCompleted: Boolean = false,
    val date: String = "",
    val note: String = ""
)

/**
 * Caregiver contact for family follow-up and emergency SOS.
 */
@Entity(tableName = "caregivers")
data class CaregiverEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val relationship: String,          // الابن، الابنة، الزوج/الزوجة، الطبيب المعالج
    val phone: String,
    val isEmergencyContact: Boolean = true,
    val notifyOnMissedDose: Boolean = true
)

/**
 * Daily health vital check (pressure, sugar, etc.).
 */
@Entity(tableName = "health_vitals")
data class HealthVitalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String,                  // ضغط الدم، سكر الدم، نبضات القلب، الوزن
    val value1: String,                // 120 (أو 110)
    val value2: String? = null,        // 80 (لضغط الدم)
    val statusText: String,            // طبيعي، مرتفع، منخفض
    val recordedAt: String
)

/**
 * Educational guide item for medicine knowledge (دليل معرفة الأدوية).
 */
data class MedicineGuideItem(
    val title: String,
    val category: String,
    val commonNames: String,
    val purpose: String,
    val howToTake: String,
    val seniorPrecautions: String,
    val warningSign: String
)

/**
 * Senior's Medical Record (السجل الطبي للمستخدم).
 * Contains personal info, chronic conditions, drug/food allergies, and treating doctors with direct phone numbers.
 */
@Entity(tableName = "medical_profiles")
data class MedicalProfileEntity(
    @PrimaryKey
    val id: Long = 1,
    val fullName: String = "الوالد الحبيب (عبد الرحمن)",
    val age: String = "٧٢ سنة",
    val bloodType: String = "O+",
    val nationalIdOrInsurance: String = "تأمين طبي فئة أ - شركة التعاونية #889211",
    val hospitalName: String = "مستشفى الملك فهد التخصصي",
    val emergencyPhone: String = "0501234567",
    val chronicConditions: String = "ارتفاع ضغط الدم، السكري (النوع الثاني)، خشونة مفاصل الركبة",
    val allergies: String = "حساسية شديدة من البنسلين (Penicillin)، حساسية من الأسبرين الخام بجرعات عالية",
    val doctor1Name: String = "د. عادل الشريف",
    val doctor1Specialty: String = "طبيب استشاري القلب والأوعية الدموية",
    val doctor1Phone: String = "0555123456",
    val doctor2Name: String = "د. فاطمة الصالح",
    val doctor2Specialty: String = "استشارية الغدد الصماء والسكري",
    val doctor2Phone: String = "0555789012",
    val dietaryRestrictions: String = "حمية قليلة الملح، تجنب السكريات المكررة والدهون المشبعة",
    val generalNotes: String = "يفضل فحص الضغط صباحاً ومساءً، والمشي الخفيف لمدة ربع ساعة يومياً"
)

