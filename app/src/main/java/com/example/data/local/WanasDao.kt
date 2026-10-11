package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.CaregiverEntity
import com.example.data.model.DailyTaskEntity
import com.example.data.model.HealthVitalEntity
import com.example.data.model.MedicalProfileEntity
import com.example.data.model.MedicineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WanasDao {

    // --- Medicines ---
    @Query("SELECT * FROM medicines WHERE isActive = 1 ORDER BY scheduledTime ASC")
    fun getAllMedicines(): Flow<List<MedicineEntity>>

    @Query("SELECT * FROM medicines WHERE id = :id")
    suspend fun getMedicineById(id: Long): MedicineEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicine(medicine: MedicineEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicines(medicines: List<MedicineEntity>)

    @Update
    suspend fun updateMedicine(medicine: MedicineEntity)

    @Delete
    suspend fun deleteMedicine(medicine: MedicineEntity)

    @Query("UPDATE medicines SET isTakenToday = :taken, lastTakenTime = :time, remainingPills = CASE WHEN :taken = 1 AND remainingPills > 0 THEN remainingPills - 1 ELSE remainingPills END WHERE id = :id")
    suspend fun markMedicineTaken(id: Long, taken: Boolean, time: String?)

    @Query("UPDATE medicines SET isTakenToday = 0, lastTakenTime = null")
    suspend fun resetDailyDoses()

    // --- Daily Tasks ---
    @Query("SELECT * FROM daily_tasks ORDER BY isCompleted ASC, scheduledTime ASC")
    fun getAllTasks(): Flow<List<DailyTaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: DailyTaskEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<DailyTaskEntity>)

    @Update
    suspend fun updateTask(task: DailyTaskEntity)

    @Delete
    suspend fun deleteTask(task: DailyTaskEntity)

    @Query("UPDATE daily_tasks SET isCompleted = :completed WHERE id = :id")
    suspend fun setTaskCompleted(id: Long, completed: Boolean)

    // --- Caregivers ---
    @Query("SELECT * FROM caregivers ORDER BY isEmergencyContact DESC, name ASC")
    fun getAllCaregivers(): Flow<List<CaregiverEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCaregiver(caregiver: CaregiverEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCaregivers(caregivers: List<CaregiverEntity>)

    @Update
    suspend fun updateCaregiver(caregiver: CaregiverEntity)

    @Delete
    suspend fun deleteCaregiver(caregiver: CaregiverEntity)

    // --- Health Vitals ---
    @Query("SELECT * FROM health_vitals ORDER BY id DESC LIMIT 20")
    fun getRecentVitals(): Flow<List<HealthVitalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVital(vital: HealthVitalEntity): Long

    @Delete
    suspend fun deleteVital(vital: HealthVitalEntity)

    // --- Medical Profile (السجل الطبي) ---
    @Query("SELECT * FROM medical_profiles WHERE id = 1")
    fun getMedicalProfile(): Flow<MedicalProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMedicalProfile(profile: MedicalProfileEntity)
}
