package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AlWatanConstants
import com.example.data.model.ConsultationBookingEntity
import com.example.ui.components.ContactIntents
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.GreenPrimary

@Composable
fun ConsultationScreen(
    consultations: List<ConsultationBookingEntity>,
    onBookConsultation: (
        name: String,
        phone: String,
        email: String,
        type: String,
        date: String,
        time: String,
        meetingType: String,
        notes: String
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var clientName by remember { mutableStateOf("") }
    var clientPhone by remember { mutableStateOf("") }
    var clientEmail by remember { mutableStateOf("") }
    var consultationType by remember { mutableStateOf("استشارة نظم ساب والتحول الرقمي") }
    var selectedDate by remember { mutableStateOf("2026/10/16 (الخميس)") }
    var selectedTime by remember { mutableStateOf("11:00 ص") }
    var meetingType by remember { mutableStateOf("اجتماع مرئي افتراضي") }
    var notes by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val consultationTypes = listOf(
        "استشارة نظم ساب والتحول الرقمي",
        "استشارة المنصات والامتثال الحكومي",
        "استشارة تأسيس وحلول المنشآت",
        "تكامل الفوترة الإلكترونية ZATCA"
    )

    val dates = listOf(
        "2026/10/15 (الأربعاء)",
        "2026/10/16 (الخميس)",
        "2026/10/18 (السبت)",
        "2026/10/19 (الأحد)",
        "2026/10/20 (الاثنين)"
    )

    val timeSlots = listOf("10:00 ص", "11:30 ص", "01:30 م", "04:00 م", "07:00 م")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("consultation_lazy_column")
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "حجز استشارة الإدارة والمشرف العام",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "جلسة استشارية متخصصة ومباشرة مع الأستاذ ماجد سعود العميري",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Supervisor Bio Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("supervisor_bio_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, GoldSecondary.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(GreenPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(34.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = AlWatanConstants.OWNER_FULL_NAME,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "معتمد",
                                    tint = GoldSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Text(
                                text = "مستقل ومطور معتمد | رقم الوثيقة: ${AlWatanConstants.FREELANCE_DOC_NO}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = GreenPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "وزارة الموارد البشرية والتنمية الاجتماعية - تخصص ساب (SAP)",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "نقدم استشارات رفيعة المستوى تشمل تحليل الفجوات الرقمية للمنشآت، تخطيط موارد المؤسسات، الامتثال مع منظومة قوى ومدد، والربط التقني المباشر مع هيئة الزكاة والضريبة والجمارك.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                ContactIntents.dialPhone(context, AlWatanConstants.OFFICIAL_PHONES.first())
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("اتصال مباشر", fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                ContactIntents.openWhatsApp(
                                    context,
                                    AlWatanConstants.OFFICIAL_PHONES.first(),
                                    "السلام عليكم أستاذ ماجد، أود التنسيق بشأن موعد استشارة تقنية وحكومية"
                                )
                            },
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF25D366)
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("تنسيق واتساب", fontSize = 12.sp, color = Color.White)
                        }
                    }
                }
            }
        }

        // Booking Form Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("booking_form_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "نموذج حجز موعد الاستشارة",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = clientName,
                        onValueChange = { clientName = it },
                        label = { Text("الاسم الكريم / ممثل المنشأة *") },
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("consultation_input_name")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = clientPhone,
                        onValueChange = { clientPhone = it },
                        label = { Text("رقم الجوال لتأكيد الحضور *") },
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                        placeholder = { Text("05xxxxxxxx") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("consultation_input_phone")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = clientEmail,
                        onValueChange = { clientEmail = it },
                        label = { Text("البريد الإلكتروني لإرسال الرابط والتفاصيل") },
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        placeholder = { Text("name@company.com") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("consultation_input_email")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "موضوع ومجال الاستشارة:",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    consultationTypes.forEach { type ->
                        val isSelected = consultationType == type
                        Surface(
                            onClick = { consultationType = type },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            border = if (isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.DateRange,
                                    contentDescription = null,
                                    tint = if (isSelected) GreenPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = type,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "صيغة الاجتماع:",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            "اجتماع مرئي افتراضي" to Icons.Default.Videocam,
                            "حضور بمقر المكتب" to Icons.Default.LocationOn
                        ).forEach { (format, icon) ->
                            val isSelected = meetingType == format
                            Surface(
                                onClick = { meetingType = format },
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                border = if (isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null,
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp),
                                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = format,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "التاريخ المفضل:",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(dates) { date ->
                            val isSelected = selectedDate == date
                            Surface(
                                onClick = { selectedDate = date },
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.padding(vertical = 4.dp)
                            ) {
                                Text(
                                    text = date,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "التوقيت المتاح:",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        timeSlots.forEach { slot ->
                            val isSelected = selectedTime == slot
                            Surface(
                                onClick = { selectedTime = slot },
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) GoldSecondary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = slot,
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("نبذة عن المشكلة أو المشروع المطلوب استشارته") },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMessage ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (clientName.isBlank() || clientPhone.isBlank()) {
                                errorMessage = "يرجى كتابة الاسم ورقم الجوال"
                            } else {
                                errorMessage = null
                                onBookConsultation(
                                    clientName.trim(),
                                    clientPhone.trim(),
                                    clientEmail.trim(),
                                    consultationType,
                                    selectedDate,
                                    selectedTime,
                                    meetingType,
                                    notes.trim()
                                )
                                showSuccessDialog = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("submit_consultation_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("تأكيد حجز موعد الاستشارة", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Confirmed Bookings List
        if (consultations.isNotEmpty()) {
            item {
                Text(
                    text = "مواعيد الاستشارات المحجوزة (${consultations.size})",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            items(consultations) { booking ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, GreenPrimary.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = booking.consultationType,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Surface(
                                color = GreenPrimary.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = booking.status,
                                    color = GreenPrimary,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "الموعد: ${booking.preferredDate} - الساعة ${booking.preferredTime}",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Text(
                            text = "الصيغة: ${booking.meetingType} | المشرف: ${booking.supervisor}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
