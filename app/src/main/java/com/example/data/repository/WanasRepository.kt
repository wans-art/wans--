package com.example.data.repository

import com.example.data.local.WanasDao
import com.example.data.model.CaregiverEntity
import com.example.data.model.DailyTaskEntity
import com.example.data.model.HealthVitalEntity
import com.example.data.model.MedicalProfileEntity
import com.example.data.model.MedicineEntity
import com.example.data.model.MedicineGuideItem
import kotlinx.coroutines.flow.Flow

class WanasRepository(private val dao: WanasDao) {

    val allMedicines: Flow<List<MedicineEntity>> = dao.getAllMedicines()
    val allTasks: Flow<List<DailyTaskEntity>> = dao.getAllTasks()
    val allCaregivers: Flow<List<CaregiverEntity>> = dao.getAllCaregivers()
    val recentVitals: Flow<List<HealthVitalEntity>> = dao.getRecentVitals()
    val medicalProfile: Flow<MedicalProfileEntity?> = dao.getMedicalProfile()

    suspend fun saveMedicalProfile(profile: MedicalProfileEntity) = dao.saveMedicalProfile(profile)

    suspend fun insertMedicine(medicine: MedicineEntity): Long = dao.insertMedicine(medicine)
    suspend fun updateMedicine(medicine: MedicineEntity) = dao.updateMedicine(medicine)
    suspend fun deleteMedicine(medicine: MedicineEntity) = dao.deleteMedicine(medicine)
    suspend fun markMedicineTaken(id: Long, taken: Boolean, time: String?) =
        dao.markMedicineTaken(id, taken, time)
    suspend fun resetDailyDoses() = dao.resetDailyDoses()

    suspend fun insertTask(task: DailyTaskEntity): Long = dao.insertTask(task)
    suspend fun setTaskCompleted(id: Long, completed: Boolean) = dao.setTaskCompleted(id, completed)
    suspend fun deleteTask(task: DailyTaskEntity) = dao.deleteTask(task)

    suspend fun insertCaregiver(caregiver: CaregiverEntity): Long = dao.insertCaregiver(caregiver)
    suspend fun deleteCaregiver(caregiver: CaregiverEntity) = dao.deleteCaregiver(caregiver)

    suspend fun insertVital(vital: HealthVitalEntity): Long = dao.insertVital(vital)

    /**
     * Senior Medicine Knowledge Guide (دليل معرفة الأدوية لكبار السن).
     * Clear, simple, reassuring Arabic explanations of common medications.
     */
    fun getMedicineKnowledgeBase(): List<MedicineGuideItem> = listOf(
        MedicineGuideItem(
            title = "أدوية ضغط الدم (مثل كونكور، نورفاسك، كوزار)",
            category = "القلب وضغط الدم",
            commonNames = "Concor, Norvasc, Cozaar, Exforge",
            purpose = "تساعد في توسيع الأوعية الدموية وتهدئة نبض القلب لحمايته من الإجهاد والوقاية من الجلطات.",
            howToTake = "تؤخذ عادة في الصباح بعد الإفطار في نفس الموعد يومياً مع كوب ماء وفير.",
            seniorPrecautions = "تجنب النهوض المفاجئ من الفراش أو الكرسي لتفادي الدوار، وافحص ضغطك أسبوعياً.",
            warningSign = "إذا شعرت بدوار مستمر أو نبض شديد البطء استشر طبيبك فوراً."
        ),
        MedicineGuideItem(
            title = "أدوية السكري ومنظمات السكر (مثل غلوكوفاج، جانوفيا)",
            category = "السكري والتمثيل الغذائي",
            commonNames = "Glucophage, Metformin, Januvia, Diamicron",
            purpose = "تساعد خلايا الجسم على استخدام الإنسولين بفعالية للحفاظ على معدل سكر طبيعي ومتوازن.",
            howToTake = "يُفضل تناوله في منتصف وجبة الطعام أو بعدها مباشرة لتقليل اضطرابات الجهاز الهضمي.",
            seniorPrecautions = "احرص على تناول وجباتك بانتظام وتجنب تفويت الوجبات لتجنب هبوط السكر.",
            warningSign = "في حال التعرق الشديد، الرعشة، أو الدوخة تناول فوراً نصف كوب عصير أو ملعقة عسل."
        ),
        MedicineGuideItem(
            title = "مميعات الدم والوقاية من الجلطات (مثل الأسبرين، بلافيكس)",
            category = "سيولة الدم",
            commonNames = "Aspirin Protect, Plavix, Xarelto",
            purpose = "تمنع تخثر الصفائح الدموية وتحافظ على تدفق الدم بسلاسة داخل الشرايين التاجية والدماغية.",
            howToTake = "تؤخذ حبة واحدة بعد الوجبة الرئيسية مباشرة مع كوب ماء كامل.",
            seniorPrecautions = "تجنب أخذ مسكنات قوية مثل البروفين دون استشارة لتفادي تهيج المعدة أو النزيف.",
            warningSign = "ملاحظة أي نزيف في اللثة أو كدمات مفاجئة تتطلب مراجعة الطبيب لتعديل الجرعة."
        ),
        MedicineGuideItem(
            title = "أدوية الدهون والكوليسترول (مثل لبيتور، كريستور)",
            category = "الدهون والدهون الثلاثية",
            commonNames = "Lipitor, Atorvastatin, Crestor, Rosuvastatin",
            purpose = "تخفض مستوى الكوليسترول الضار في الكبد وتحمي جدران الشرايين من التصلب والتضيق.",
            howToTake = "تؤخذ مساءً أو قبل النوم لأن الكبد يصنع معظم الكوليسترول أثناء فترة النوم.",
            seniorPrecautions = "الابتعاد عن شرب عصير الجريب فروت لأنه قد يزيد من تركيز الدواء في الدم.",
            warningSign = "آلام عضلية شديدة غير معتادة تستوجب التواصل مع الطبيب."
        ),
        MedicineGuideItem(
            title = "فيتامين د ومكملات الكالسيوم (تقوية المفاصل والعظام)",
            category = "المفاصل والمناعة",
            commonNames = "Vitamin D3, Caltrate, Osteocare",
            purpose = "تقوية كثافة العظام والوقاية من الهشاشة ودعم قوة عضلات الساقين لتوازن أفضل أثناء المشي.",
            howToTake = "تؤخذ مع وجبة تحتوي على نسبة دهون صحية (كالحليب، البيض، أو زيت الزيتون).",
            seniorPrecautions = "التعرض لأشعة شمس الصباح المعتدلة يومياً لمدة ١٥ دقيقة يعزز الفائدة بشكل كبير.",
            warningSign = "لا تتناول جرعات مضاعفة دون فحص دوري لمستوى الفيتامين والكالسيوم في الدم."
        ),
        MedicineGuideItem(
            title = "أدوية حموضة ومعدة (مثل أوميبرازول، نيكسيوم)",
            category = "المعدة والجهاز الهضمي",
            commonNames = "Omeprazole, Nexium, Controloc",
            purpose = "تهدئة إفراز حمض المعدة وحماية جدار المعدة من أثر الأدوية الأخرى والحموضة.",
            howToTake = "تؤخذ صباحاً على الريق قبل تناول وجبة الإفطار بنصف ساعة إلى ٤٥ دقيقة.",
            seniorPrecautions = "لا تسحق الكبسولة أو تمضغها، بل ابتلعها كاملة مع الماء.",
            warningSign = "الشعور بألم حارق يمتد إلى الصدر بعد الأكل."
        )
    )
}
