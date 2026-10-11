package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SosRed
import com.example.ui.viewmodel.UserRoleMode

@Composable
fun WanasTopBar(
    roleMode: UserRoleMode,
    isSpeaking: Boolean,
    currentFontScale: Float,
    onToggleRole: () -> Unit,
    onToggleVoice: () -> Unit,
    onFontScaleChange: (Float) -> Unit,
    onEmergencySosClick: () -> Unit
) {
    var fontMenuExpanded by remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp,
        shadowElevation = 3.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Title and identity
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "وَنَس",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "وَنَس",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            // Mode Pill
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (roleMode == UserRoleMode.SENIOR)
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                        else
                                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
                                    )
                                    .clickable { onToggleRole() }
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = roleMode.titleArabic,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (roleMode == UserRoleMode.SENIOR)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.secondary
                                )
                            }
                        }

                        Text(
                            text = "رفيقك الذكي ديما معاك",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Action controls: TTS Voice, Font Size, Emergency SOS
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Voice Reader Button
                    IconButton(
                        onClick = onToggleVoice,
                        modifier = Modifier
                            .testTag("voice_toggle_button")
                            .clip(CircleShape)
                            .background(
                                if (isSpeaking) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.primaryContainer
                            )
                    ) {
                        Icon(
                            imageVector = if (isSpeaking) Icons.Default.Stop else Icons.Default.RecordVoiceOver,
                            contentDescription = "القارئ الصوتي",
                            tint = if (isSpeaking) Color.White else MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Font Size Adjuster
                    Box {
                        IconButton(
                            onClick = { fontMenuExpanded = true },
                            modifier = Modifier
                                .testTag("font_size_button")
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FormatSize,
                                contentDescription = "تغيير حجم الخط",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        DropdownMenu(
                            expanded = fontMenuExpanded,
                            onDismissRequest = { fontMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("خط عادي (١٠٠٪)", fontSize = 16.sp) },
                                onClick = {
                                    onFontScaleChange(1.0f)
                                    fontMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("خط كبير لكبار السن (١٢٠٪)", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                                onClick = {
                                    onFontScaleChange(1.2f)
                                    fontMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("خط فائق الكبر (١٤٠٪)", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold) },
                                onClick = {
                                    onFontScaleChange(1.4f)
                                    fontMenuExpanded = false
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Emergency Quick SOS Button
                    Box(
                        modifier = Modifier
                            .testTag("top_bar_sos_button")
                            .clip(RoundedCornerShape(20.dp))
                            .background(SosRed)
                            .clickable { onEmergencySosClick() }
                            .padding(horizontal = 10.dp, vertical = 7.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Emergency,
                                contentDescription = "نداء طوارئ",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "طوارئ",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
