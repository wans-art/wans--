package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.SeniorVoiceBar
import com.example.ui.components.WanasTopBar
import com.example.ui.viewmodel.WanasScreen
import com.example.ui.viewmodel.WanasViewModel

@Composable
fun WanasMainScreen(
    viewModel: WanasViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentScreen by viewModel.currentScreen.collectAsState()
    val roleMode by viewModel.userRoleMode.collectAsState()
    val fontScale by viewModel.fontScale.collectAsState()
    val isSpeaking by viewModel.isVoiceSpeaking.collectAsState()
    val snoozedMsg by viewModel.snoozedMessage.collectAsState()

    val medicines by viewModel.medicines.collectAsState()
    val tasks by viewModel.tasks.collectAsState()
    val caregivers by viewModel.caregivers.collectAsState()
    val vitals by viewModel.vitals.collectAsState()
    val medicalProfile by viewModel.medicalProfile.collectAsState()
    val waterCups by viewModel.waterCupsDrank.collectAsState()

    // Handle back button to return to TODAY screen
    BackHandler(enabled = currentScreen != WanasScreen.TODAY) {
        viewModel.selectScreen(WanasScreen.TODAY)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column {
                WanasTopBar(
                    roleMode = roleMode,
                    isSpeaking = isSpeaking,
                    currentFontScale = fontScale,
                    onToggleRole = { viewModel.toggleRoleMode() },
                    onToggleVoice = {
                        if (isSpeaking) viewModel.stopVoice() else viewModel.speakGreeting()
                    },
                    onFontScaleChange = { viewModel.setFontScale(it) },
                    onEmergencySosClick = { viewModel.triggerEmergencyCall(context, null) }
                )

                // Voice Animated Listening Bar
                SeniorVoiceBar(
                    isSpeaking = isSpeaking,
                    onStopVoice = { viewModel.stopVoice() }
                )

                // Active Snooze Toast Bar
                AnimatedVisibility(visible = snoozedMsg != null) {
                    snoozedMsg?.let { msg ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = msg,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { viewModel.dismissSnoozeMessage() },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "إغلاق",
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("wanas_bottom_navigation"),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp
            ) {
                val screens = listOf(
                    WanasScreen.TODAY to Icons.Default.Today,
                    WanasScreen.MEDICINES to Icons.Default.Medication,
                    WanasScreen.TASKS to Icons.Default.TaskAlt,
                    WanasScreen.MEDICAL_RECORD to Icons.Default.MedicalInformation,
                    WanasScreen.FAMILY to Icons.Default.FamilyRestroom,
                    WanasScreen.GUIDE to Icons.Default.MenuBook
                )

                screens.forEach { (screen, icon) ->
                    val isSelected = currentScreen == screen
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.selectScreen(screen) },
                        icon = {
                            Icon(
                                imageVector = icon,
                                contentDescription = screen.titleArabic,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                text = screen.titleArabic,
                                fontSize = (12 * fontScale).sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (currentScreen) {
                WanasScreen.TODAY -> {
                    TodayMedicationScreen(
                        viewModel = viewModel,
                        medicines = medicines,
                        fontScale = fontScale,
                        roleMode = roleMode
                    )
                }
                WanasScreen.MEDICINES -> {
                    MedicineCabinetScreen(
                        viewModel = viewModel,
                        medicines = medicines,
                        fontScale = fontScale,
                        roleMode = roleMode
                    )
                }
                WanasScreen.TASKS -> {
                    DailyTasksScreen(
                        viewModel = viewModel,
                        tasks = tasks,
                        vitals = vitals,
                        waterCups = waterCups,
                        fontScale = fontScale,
                        roleMode = roleMode
                    )
                }
                WanasScreen.MEDICAL_RECORD -> {
                    MedicalProfileScreen(
                        viewModel = viewModel,
                        profile = medicalProfile,
                        fontScale = fontScale,
                        onFontScaleChange = { viewModel.setFontScale(it) }
                    )
                }
                WanasScreen.GUIDE -> {
                    MedicineGuideScreen(
                        viewModel = viewModel,
                        guideItems = viewModel.medicineGuide,
                        fontScale = fontScale
                    )
                }
                WanasScreen.FAMILY -> {
                    FamilyFollowUpScreen(
                        viewModel = viewModel,
                        caregivers = caregivers,
                        medicines = medicines,
                        fontScale = fontScale,
                        roleMode = roleMode
                    )
                }
            }
        }
    }
}
