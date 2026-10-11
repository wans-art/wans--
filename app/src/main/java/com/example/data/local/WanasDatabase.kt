package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.CaregiverEntity
import com.example.data.model.DailyTaskEntity
import com.example.data.model.HealthVitalEntity
import com.example.data.model.MedicalProfileEntity
import com.example.data.model.MedicineEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        MedicineEntity::class,
        DailyTaskEntity::class,
        CaregiverEntity::class,
        HealthVitalEntity::class,
        MedicalProfileEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class WanasDatabase : RoomDatabase() {

    abstract fun wanasDao(): WanasDao

    companion object {
        @Volatile
        private var INSTANCE: WanasDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): WanasDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WanasDatabase::class.java,
                    "wanas_elderly_db"
                )
                    .addCallback(WanasDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class WanasDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.wanasDao())
                    }
                }
            }

            private suspend fun populateInitialData(dao: WanasDao) {
                // Initial elderly medicines
                val initialMedicines = listOf(
                    MedicineEntity(
                        name = "كونكور (Concor 5mg)",
                        purpose = "تنظيم ضغط الدم وحماية القلب",
                        dosage = "قرص واحد (٥ ملغ)",
                        form = "قرص",
                        colorHex = 0xFF2E7D32, // أخضر طبي
                        timeOfDay = "الصباح",
                        scheduledTime = "08:00 ص",
                        mealTiming = "بعد الفطور مباشرة",
                        totalPills = 30,
                        remainingPills = 24,
                        instructions = "يُفضل تناوله مع كوب ماء دافئ في نفس الموعد يومياً",
                        doctorNotes = "الدكتور عادل: الاستمرار عليه بانتظام وعدم التوقف دون استشارة",
                        isTakenToday = true,
                        lastTakenTime = "08:15 ص"
                    ),
                    MedicineEntity(
                        name = "غلوكوفاج (Glucophage 500mg)",
                        purpose = "ضبط مستوى السكر في الدم",
                        dosage = "قرص واحد (٥٠٠ ملغ)",
                        form = "قرص",
                        colorHex = 0xFF0284C7, // أزرق سماوي
                        timeOfDay = "الظهر",
                        scheduledTime = "01:30 م",
                        mealTiming = "مع وجبة الغداء أو بعدها",
                        totalPills = 30,
                        remainingPills = 18,
                        instructions = "يتم تناوله في منتصف الوجبة لتجنب أي انزعاج بالمعدة",
                        doctorNotes = "الدكتور عادل: قياس السكر التراكمي الشهر القادم",
                        isTakenToday = false
                    ),
                    MedicineEntity(
                        name = "فيتامين د3 (Vitamin D3)",
                        purpose = "تقوية العظام والمفاصل والمناعة",
                        dosage = "كبسولة واحدة أسبوعياً أو يومياً حسب الحاجة",
                        form = "كبسولة",
                        colorHex = 0xFFD97706, // برتقالي عنبري
                        timeOfDay = "المساء",
                        scheduledTime = "07:00 م",
                        mealTiming = "بعد العشاء مع دهون صحية كالحليب أو الزيتون",
                        totalPills = 20,
                        remainingPills = 4, // تنبيه قرب النفاذ!
                        instructions = "تساعد على امتصاص الكالسيوم وحيوية الجسم",
                        doctorNotes = "فحص نسبة الفيتامين خلال 3 أشهر",
                        isTakenToday = false
                    ),
                    MedicineEntity(
                        name = "قطرة ترطيب العين (Systane)",
                        purpose = "علاج جفاف العين وراحة الرؤية",
                        dosage = "نقطتان في كل عين",
                        form = "قطرة",
                        colorHex = 0xFF0D9488, // فيروزي
                        timeOfDay = "قبل النوم",
                        scheduledTime = "09:30 م",
                        mealTiming = "قبل النوم مباشرة",
                        totalPills = 15,
                        remainingPills = 11,
                        instructions = "إغلاق العين بلطف لمدة دقيقة بعد وضع القطرة",
                        doctorNotes = "استخدامها يومياً لتقليل إجهاد القراءة",
                        isTakenToday = false
                    )
                )
                dao.insertMedicines(initialMedicines)

                // Initial daily tasks for seniors
                val initialTasks = listOf(
                    DailyTaskEntity(
                        title = "شرب كوبين من الماء الفاتر",
                        category = "ماء",
                        scheduledTime = "08:30 ص",
                        isCompleted = true,
                        note = "الترطيب الصباحي ينشط الدورة الدموية"
                    ),
                    DailyTaskEntity(
                        title = "المشي الخفيف في الحديقة أو الصالة",
                        category = "نشاط",
                        scheduledTime = "10:30 ص",
                        isCompleted = true,
                        note = "١٥ دقيقة لتحريك المفاصل وتنفس الهواء النقي"
                    ),
                    DailyTaskEntity(
                        title = "قياس ضغط الدم وتسجيل القراءة",
                        category = "قياسات",
                        scheduledTime = "02:00 م",
                        isCompleted = false,
                        note = "الجلوس باسترخاء لمدة ٥ دقائق قبل القياس"
                    ),
                    DailyTaskEntity(
                        title = "أذكار المساء والاستغفار",
                        category = "عبادة",
                        scheduledTime = "05:30 م",
                        isCompleted = false,
                        note = "راحة للقلب وطمأنينة للنفس"
                    ),
                    DailyTaskEntity(
                        title = "الاتصال بالابن محمد والاطمئنان عليه",
                        category = "عائلة",
                        scheduledTime = "06:30 م",
                        isCompleted = false,
                        note = "ونس الأسرة والتواصل اليومي"
                    )
                )
                dao.insertTasks(initialTasks)

                // Initial Caregivers
                val initialCaregivers = listOf(
                    CaregiverEntity(
                        name = "محمد (الابن الأكبر)",
                        relationship = "الابن",
                        phone = "0501234567",
                        isEmergencyContact = true,
                        notifyOnMissedDose = true
                    ),
                    CaregiverEntity(
                        name = "مريم (الابنة)",
                        relationship = "الابنة",
                        phone = "0509876543",
                        isEmergencyContact = true,
                        notifyOnMissedDose = true
                    ),
                    CaregiverEntity(
                        name = "د. عادل (طبيب القلب والأسرة)",
                        relationship = "الطبيب المعالج",
                        phone = "0555123456",
                        isEmergencyContact = false,
                        notifyOnMissedDose = false
                    )
                )
                dao.insertCaregivers(initialCaregivers)

                // Initial Health Vitals
                val initialVitals = listOf(
                    HealthVitalEntity(
                        type = "ضغط الدم",
                        value1 = "120",
                        value2 = "80",
                        statusText = "ممتاز ومثالي",
                        recordedAt = "اليوم 08:30 ص"
                    ),
                    HealthVitalEntity(
                        type = "سكر الدم (صائم)",
                        value1 = "98",
                        value2 = null,
                        statusText = "طبيعي وممتاز",
                        recordedAt = "اليوم 07:45 ص"
                    )
                )
                initialVitals.forEach { dao.insertVital(it) }

                // Initial Medical Profile (السجل الطبي للمستخدم)
                dao.saveMedicalProfile(MedicalProfileEntity())
            }
        }
    }
}
