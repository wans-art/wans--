package com.example.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CaregiverDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, relation: String, phone: String, isEmergency: Boolean) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var relation by remember { mutableStateOf("الابن") }
    var phone by remember { mutableStateOf("") }
    var isEmergency by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "إضافة فرد من العائلة / مرافق",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("الاسم الكريم (مثال: أحمد)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("caregiver_name_input"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = relation,
                    onValueChange = { relation = it },
                    label = { Text("صلة القرابة (الابن، الابنة، الزوجة، الممرض)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("رقم الهاتف أو الواتساب") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("caregiver_phone_input"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isEmergency,
                        onCheckedChange = { isEmergency = it }
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("تعيين كرقم طوارئ واستغاثة سريع (SOS)")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && phone.isNotBlank()) {
                        onConfirm(name, relation, phone, isEmergency)
                    }
                },
                modifier = Modifier.testTag("save_caregiver_button")
            ) {
                Text("حفظ جهة الاتصال", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}

@Composable
fun AddVitalDialog(
    onDismiss: () -> Unit,
    onConfirm: (type: String, val1: String, val2: String?, status: String) -> Unit
) {
    var type by remember { mutableStateOf("ضغط الدم") }
    var val1 by remember { mutableStateOf("120") }
    var val2 by remember { mutableStateOf("80") }
    var status by remember { mutableStateOf("طبيعي وممتاز") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "تسجيل قياس صحي جديد",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = type,
                    onValueChange = { type = it },
                    label = { Text("نوع القياس (ضغط الدم، سكر الدم، نبض)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = val1,
                        onValueChange = { val1 = it },
                        label = { Text("القيمة الأولى") },
                        modifier = Modifier.weight(1f)
                    )
                    if (type.contains("ضغط")) {
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = val2,
                            onValueChange = { val2 = it },
                            label = { Text("القيمة الثانية") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = status,
                    onValueChange = { status = it },
                    label = { Text("الحالة (طبيعي وممتاز، مرتفع قليلاً، منخفض)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(type, val1, if (type.contains("ضغط")) val2 else null, status)
                }
            ) {
                Text("تسجيل القياس", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}
