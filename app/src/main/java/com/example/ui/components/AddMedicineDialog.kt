package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddMedicineDialog(
    onDismiss: () -> Unit,
    onConfirm: (
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
    ) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var purpose by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("قرص واحد") }
    var form by remember { mutableStateOf("قرص") }
    var selectedColor by remember { mutableLongStateOf(0xFF2E7D32) }
    var timeOfDay by remember { mutableStateOf("الصباح") }
    var scheduledTime by remember { mutableStateOf("08:00 ص") }
    var mealTiming by remember { mutableStateOf("بعد الأكل") }
    var totalPills by remember { mutableStateOf("30") }
    var instructions by remember { mutableStateOf("") }
    var doctorNotes by remember { mutableStateOf("") }

    val formsList = listOf("قرص", "كبسولة", "شراب", "قطرة", "حقنة")
    val timesList = listOf("الصباح", "الظهر", "المساء", "قبل النوم")
    val mealList = listOf("بعد الأكل", "قبل الأكل", "مع الأكل", "على الريق")
    val colorList = listOf(
        0xFF2E7D32, // أخضر
        0xFF0284C7, // أزرق
        0xFFD97706, // عنبري
        0xFFDC2626, // أحمر
        0xFF7C3AED, // بنفسجي
        0xFF0D9488  // فيروزي
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "إضافة دواء جديد لجدول ونس",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("اسم الدواء (مثال: كونكور 5 ملغ)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("med_name_input"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = purpose,
                    onValueChange = { purpose = it },
                    label = { Text("دواعي الاستعمال (مثال: تنظيم ضغط الدم)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("med_purpose_input"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = dosage,
                        onValueChange = { dosage = it },
                        label = { Text("الجرعة (مثال: قرص واحد)") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("med_dosage_input"),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = scheduledTime,
                        onValueChange = { scheduledTime = it },
                        label = { Text("الموعد (مثال: 08:00 ص)") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("med_time_input"),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Time of day selector chips
                Text("فترة اليوم:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    timesList.forEach { period ->
                        val isSelected = timeOfDay == period
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .clickable { timeOfDay = period }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = period,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Meal Timing
                Text("توقيت الوجبة:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    mealList.forEach { timing ->
                        val isSelected = mealTiming == timing
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.secondary
                                    else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .clickable { mealTiming = timing }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = timing,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Color Tag selection
                Text("لون تمييز العلبة/الحبة:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    colorList.forEach { colorVal ->
                        val isSelected = selectedColor == colorVal
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(colorVal))
                                .border(
                                    width = if (isSelected) 3.dp else 0.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { selectedColor = colorVal }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = instructions,
                    onValueChange = { instructions = it },
                    label = { Text("تعليمات خاصة (مثال: اشرب مع كوب ماء كامل)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = doctorNotes,
                    onValueChange = { doctorNotes = it },
                    label = { Text("ملاحظات الطبيب المعالج") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val pillsInt = totalPills.toIntOrNull() ?: 30
                        onConfirm(
                            name,
                            if (purpose.isBlank()) "علاج ومكمل صحي" else purpose,
                            dosage,
                            form,
                            selectedColor,
                            timeOfDay,
                            scheduledTime,
                            mealTiming,
                            pillsInt,
                            instructions,
                            doctorNotes
                        )
                    }
                },
                modifier = Modifier.testTag("save_medicine_button")
            ) {
                Text("حفظ الدواء", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}
