package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MedicalProfileEntity
import com.example.ui.theme.SosRed
import com.example.ui.theme.SosRedContainer
import com.example.ui.theme.SosRedOnContainer
import com.example.ui.viewmodel.WanasViewModel

@Composable
fun MedicalProfileScreen(
    viewModel: WanasViewModel,
    profile: MedicalProfileEntity,
    fontScale: Float,
    onFontScaleChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showEditDialog by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
        ) {
            // Header: Screen Title + Voice Reader + Quick Font Scale Adjuster
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MedicalInformation,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "السجل الطبي للمستخدم",
                                        fontSize = (20 * fontScale).sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Text(
                                        text = "بياناتك الصحية المهمة والأطباء في مكان واحد",
                                        fontSize = (13 * fontScale).sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                    )
                                }
                            }

                            // Read Aloud Voice Button
                            IconButton(
                                onClick = { viewModel.speakMedicalProfile() },
                                modifier = Modifier
                                    .testTag("speak_medical_profile_button")
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "استمع لملفك الطبي",
                                    tint = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Quick Accessibility Font Zoom Bar for Seniors
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.FormatSize,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "حجم الخط لكبار السن:",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Button(
                                    onClick = { onFontScaleChange(1.0f) },
                                    modifier = Modifier.height(36.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (fontScale == 1.0f) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                    contentPadding = PaddingValues(horizontal = 10.dp)
                                ) {
                                    Text(
                                        "عادي A",
                                        color = if (fontScale == 1.0f) Color.White else MaterialTheme.colorScheme.onSurface,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Button(
                                    onClick = { onFontScaleChange(1.2f) },
                                    modifier = Modifier.height(36.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (fontScale == 1.2f) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                    contentPadding = PaddingValues(horizontal = 10.dp)
                                ) {
                                    Text(
                                        "كبير A+",
                                        color = if (fontScale == 1.2f) Color.White else MaterialTheme.colorScheme.onSurface,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Button(
                                    onClick = { onFontScaleChange(1.4f) },
                                    modifier = Modifier.height(36.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (fontScale == 1.4f) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                    contentPadding = PaddingValues(horizontal = 10.dp)
                                ) {
                                    Text(
                                        "فائق A++",
                                        color = if (fontScale == 1.4f) Color.White else MaterialTheme.colorScheme.onSurface,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
            }

            // Section 1: Basic Personal Info Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "المعلومات الشخصية الأساسية",
                                    fontSize = (18 * fontScale).sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            // Blood type prominent badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(SosRed)
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Bloodtype,
                                        contentDescription = "فصيلة الدم",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = profile.bloodType,
                                        color = Color.White,
                                        fontSize = (18 * fontScale).sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Patient Name
                        Text(
                            text = profile.fullName,
                            fontSize = (22 * fontScale).sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "العمر: ${profile.age}",
                            fontSize = (16 * fontScale).sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Hospital & Insurance
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalHospital,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = profile.hospitalName,
                                fontSize = (14 * fontScale).sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Badge,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = profile.nationalIdOrInsurance,
                                fontSize = (14 * fontScale).sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = null,
                                tint = SosRed,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "طوارئ العائلة: ${profile.emergencyPhone}",
                                fontSize = (14 * fontScale).sp,
                                fontWeight = FontWeight.Bold,
                                color = SosRed
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
            }

            // Section 2: Critical Allergies (الحساسية تجاه الأدوية أو الأطعمة)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SosRedContainer),
                    border = androidx.compose.foundation.BorderStroke(2.dp, SosRed),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = SosRed,
                                modifier = Modifier.size(26.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "⚠️ الحساسية تجاه الأدوية والأطعمة (حرج)",
                                fontSize = (18 * fontScale).sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = SosRedOnContainer
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White)
                                .padding(14.dp)
                        ) {
                            Text(
                                text = profile.allergies,
                                fontSize = (17 * fontScale).sp,
                                fontWeight = FontWeight.Bold,
                                color = SosRed,
                                lineHeight = (24 * fontScale).sp
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "يجب إطلاع أي طبيب أو صيدلي على هذه الحساسيات فوراً قبل تناول أي علاج جديد.",
                            fontSize = (12 * fontScale).sp,
                            color = SosRedOnContainer.copy(alpha = 0.85f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
            }

            // Section 3: Chronic Diseases (الأمراض المزمنة)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Vaccines,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "الأمراض المزمنة والتشخيص الطبي",
                                fontSize = (18 * fontScale).sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Display chronic conditions as high-contrast chips
                        val conditionsList = profile.chronicConditions.split("،", ",").map { it.trim() }.filter { it.isNotBlank() }
                        conditionsList.forEach { condition ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .padding(horizontal = 14.dp, vertical = 10.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primary)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = condition,
                                        fontSize = (16 * fontScale).sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        }

                        if (profile.dietaryRestrictions.isNotBlank()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "🥗 الحمية الموصى بها: ${profile.dietaryRestrictions}",
                                fontSize = (14 * fontScale).sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        if (profile.generalNotes.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "📝 إرشادات عامة: ${profile.generalNotes}",
                                fontSize = (14 * fontScale).sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
            }

            // Section 4: Treating Doctors & Contacts (الأطباء المعالجون)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocalHospital,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "الأطباء المعالجون وأرقام التواصل",
                                fontSize = (18 * fontScale).sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Doctor 1
                        DoctorContactItem(
                            name = profile.doctor1Name,
                            specialty = profile.doctor1Specialty,
                            phone = profile.doctor1Phone,
                            fontScale = fontScale,
                            onCallClick = { viewModel.callDoctor(context, profile.doctor1Phone, profile.doctor1Name) }
                        )

                        // Doctor 2 if available
                        if (profile.doctor2Name.isNotBlank()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            DoctorContactItem(
                                name = profile.doctor2Name,
                                specialty = profile.doctor2Specialty,
                                phone = profile.doctor2Phone,
                                fontScale = fontScale,
                                onCallClick = { viewModel.callDoctor(context, profile.doctor2Phone, profile.doctor2Name) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Section 5: Action Buttons (Share Record & Edit Record)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Big Edit Button
                    Button(
                        onClick = { showEditDialog = true },
                        modifier = Modifier
                            .weight(1.2f)
                            .height(56.dp)
                            .testTag("edit_medical_profile_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "تعديل السجل الطبي",
                                fontSize = (16 * fontScale).sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // Share Profile Button
                    OutlinedButton(
                        onClick = { viewModel.shareMedicalProfile(context) },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .testTag("share_medical_profile_button"),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "مشاركة الملف",
                                fontSize = (15 * fontScale).sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Edit Profile Dialog
        if (showEditDialog) {
            EditMedicalProfileDialog(
                current = profile,
                onDismiss = { showEditDialog = false },
                onSave = { updated ->
                    viewModel.updateMedicalProfile(updated)
                    showEditDialog = false
                }
            )
        }
    }
}

@Composable
private fun DoctorContactItem(
    name: String,
    specialty: String,
    phone: String,
    fontScale: Float,
    onCallClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    fontSize = (17 * fontScale).sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = specialty,
                    fontSize = (13 * fontScale).sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = phone,
                    fontSize = (14 * fontScale).sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Big Call Button for Seniors
            Button(
                onClick = onCallClick,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .height(48.dp)
                    .testTag("call_doctor_button_${phone}")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "اتصال",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "اتصال",
                        color = Color.White,
                        fontSize = (14 * fontScale).sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun EditMedicalProfileDialog(
    current: MedicalProfileEntity,
    onDismiss: () -> Unit,
    onSave: (MedicalProfileEntity) -> Unit
) {
    var fullName by remember { mutableStateOf(current.fullName) }
    var age by remember { mutableStateOf(current.age) }
    var bloodType by remember { mutableStateOf(current.bloodType) }
    var hospitalName by remember { mutableStateOf(current.hospitalName) }
    var nationalId by remember { mutableStateOf(current.nationalIdOrInsurance) }
    var emergencyPhone by remember { mutableStateOf(current.emergencyPhone) }
    var chronicConditions by remember { mutableStateOf(current.chronicConditions) }
    var allergies by remember { mutableStateOf(current.allergies) }
    var doctor1Name by remember { mutableStateOf(current.doctor1Name) }
    var doctor1Specialty by remember { mutableStateOf(current.doctor1Specialty) }
    var doctor1Phone by remember { mutableStateOf(current.doctor1Phone) }
    var doctor2Name by remember { mutableStateOf(current.doctor2Name) }
    var doctor2Specialty by remember { mutableStateOf(current.doctor2Specialty) }
    var doctor2Phone by remember { mutableStateOf(current.doctor2Phone) }
    var dietaryRestrictions by remember { mutableStateOf(current.dietaryRestrictions) }
    var generalNotes by remember { mutableStateOf(current.generalNotes) }

    val bloodTypes = listOf("O+", "A+", "B+", "AB+", "O-", "A-", "B-", "AB-")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "تعديل السجل الطبي",
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
                Text("المعلومات الأساسية", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("الاسم الكامل") },
                    modifier = Modifier.fillMaxWidth().testTag("edit_fullname_input")
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = age,
                        onValueChange = { age = it },
                        label = { Text("العمر") },
                        modifier = Modifier.weight(1f).testTag("edit_age_input")
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = emergencyPhone,
                        onValueChange = { emergencyPhone = it },
                        label = { Text("هاتف الطوارئ") },
                        modifier = Modifier.weight(1f).testTag("edit_emergency_phone_input")
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text("فصيلة الدم:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    bloodTypes.take(4).forEach { bType ->
                        val isSelected = bloodType == bType
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) SosRed else MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { bloodType = bType }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = bType,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = hospitalName,
                    onValueChange = { hospitalName = it },
                    label = { Text("المستشفى أو المركز الصحي المتابع") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = nationalId,
                    onValueChange = { nationalId = it },
                    label = { Text("رقم التأمين الطبي أو الهوية") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))
                Text("⚠️ الحساسيات والأمراض المزمنة", fontWeight = FontWeight.Bold, color = SosRed)
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = allergies,
                    onValueChange = { allergies = it },
                    label = { Text("الحساسية تجاه الأدوية أو الأطعمة") },
                    modifier = Modifier.fillMaxWidth().testTag("edit_allergies_input")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = chronicConditions,
                    onValueChange = { chronicConditions = it },
                    label = { Text("الأمراض المزمنة (مفصولة بفاصلة)") },
                    modifier = Modifier.fillMaxWidth().testTag("edit_chronic_input")
                )

                Spacer(modifier = Modifier.height(14.dp))
                Text("الأطباء المعالجون", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = doctor1Name,
                    onValueChange = { doctor1Name = it },
                    label = { Text("اسم الطبيب الأول") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = doctor1Specialty,
                    onValueChange = { doctor1Specialty = it },
                    label = { Text("تخصص الطبيب الأول") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = doctor1Phone,
                    onValueChange = { doctor1Phone = it },
                    label = { Text("رقم هاتف الطبيب الأول") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = doctor2Name,
                    onValueChange = { doctor2Name = it },
                    label = { Text("اسم الطبيب الثاني (اختياري)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = doctor2Specialty,
                    onValueChange = { doctor2Specialty = it },
                    label = { Text("تخصص الطبيب الثاني") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = doctor2Phone,
                    onValueChange = { doctor2Phone = it },
                    label = { Text("رقم هاتف الطبيب الثاني") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = dietaryRestrictions,
                    onValueChange = { dietaryRestrictions = it },
                    label = { Text("الحمية الغذائية الموصى بها") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = generalNotes,
                    onValueChange = { generalNotes = it },
                    label = { Text("ملاحظات طبية عامة") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        current.copy(
                            fullName = fullName,
                            age = age,
                            bloodType = bloodType,
                            hospitalName = hospitalName,
                            nationalIdOrInsurance = nationalId,
                            emergencyPhone = emergencyPhone,
                            chronicConditions = chronicConditions,
                            allergies = allergies,
                            doctor1Name = doctor1Name,
                            doctor1Specialty = doctor1Specialty,
                            doctor1Phone = doctor1Phone,
                            doctor2Name = doctor2Name,
                            doctor2Specialty = doctor2Specialty,
                            doctor2Phone = doctor2Phone,
                            dietaryRestrictions = dietaryRestrictions,
                            generalNotes = generalNotes
                        )
                    )
                },
                modifier = Modifier.testTag("save_medical_profile_button")
            ) {
                Text("حفظ السجل الطبي", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}
