package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AlWatanConstants
import com.example.data.model.ServiceItem
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.StatusApproved
import com.example.ui.theme.StatusPending
import com.example.ui.theme.StatusProcessing
import com.example.ui.theme.StatusWaitClient

object ContactIntents {
    fun dialPhone(context: Context, phoneNumber: String) {
        try {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "تعذر فتح تطبيق الهاتف", Toast.LENGTH_SHORT).show()
        }
    }

    fun openWhatsApp(context: Context, phoneNumber: String, message: String = "") {
        try {
            val cleanPhone = phoneNumber.replace("+", "").replace(" ", "").trim()
            val formatted = if (cleanPhone.startsWith("0")) "966" + cleanPhone.substring(1) else cleanPhone
            val url = "https://api.whatsapp.com/send?phone=$formatted&text=${Uri.encode(message)}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "تعذر فتح تطبيق واتساب", Toast.LENGTH_SHORT).show()
        }
    }

    fun sendEmail(context: Context, email: String, subject: String = "") {
        try {
            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email")).apply {
                putExtra(Intent.EXTRA_SUBJECT, subject)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "تعذر فتح تطبيق البريد الإلكتروني", Toast.LENGTH_SHORT).show()
        }
    }

    fun openUrl(context: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "تعذر فتح الرابط", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun ExecutiveHeader(
    onOpenCredentials: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("executive_header_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
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
                            .background(
                                Brush.linearGradient(
                                    listOf(GreenPrimary, GoldSecondary)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Business,
                            contentDescription = "شعار مكتب الوطن",
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = AlWatanConstants.OFFICE_NAME,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                        Text(
                            text = AlWatanConstants.APP_TITLE,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                        )
                    }
                }

                Surface(
                    onClick = onOpenCredentials,
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.testTag("verify_badge_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "موثق رسمياً",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "وثيقة معتمدة",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = GreenPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "إشراف الأستاذ: ${AlWatanConstants.OWNER_FULL_NAME} | رخصة: ${AlWatanConstants.FREELANCE_DOC_NO}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun QuickContactBar(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val primaryPhone = AlWatanConstants.OFFICIAL_PHONES.first()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("quick_contact_bar"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = "قنوات التواصل والمكتب الرقمي المباشر",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Call
                OutlinedButton(
                    onClick = { ContactIntents.dialPhone(context, primaryPhone) },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("action_call_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "اتصال",
                        modifier = Modifier.size(16.dp),
                        tint = GreenPrimary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("اتصال", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.width(6.dp))

                // WhatsApp
                Button(
                    onClick = {
                        ContactIntents.openWhatsApp(
                            context,
                            primaryPhone,
                            "السلام عليكم، أود الاستفسار عن خدمات مكتب الوطن للحلول الرقمية ونظم ساب"
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1.1f)
                        .testTag("action_whatsapp_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "واتساب",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("واتساب", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Email
                OutlinedButton(
                    onClick = {
                        ContactIntents.sendEmail(
                            context,
                            AlWatanConstants.OFFICIAL_EMAIL,
                            "استفسار بشأن منصة الحلول الرقمية"
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("action_email_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "البريد",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("البريد", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Cloud Portal
                OutlinedButton(
                    onClick = { ContactIntents.openUrl(context, AlWatanConstants.OFFICIAL_DOMAIN) },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("action_website_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = "الموقع",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("الموقع", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: String, modifier: Modifier = Modifier) {
    val (bgColor, textColor) = when (status) {
        "قيد المراجعة" -> Color(0xFFFFF3E0) to StatusPending
        "جاري المعالجة والتدقيق", "جاري المعالجة" -> Color(0xFFE1F5FE) to StatusProcessing
        "في مرحلة الاعتماد" -> Color(0xFFF3E5F5) to Color(0xFF7B1FA2)
        "مكتمل بنجاح", "مكتمل" -> Color(0xFFE8F5E9) to StatusApproved
        "بانتظار متطلبات من العميل", "بانتظار العميل" -> Color(0xFFFCE4EC) to StatusWaitClient
        else -> Color(0xFFEEEEEE) to Color(0xFF424242)
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(textColor)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = status,
                color = textColor,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
fun ServiceOrderSubmissionDialog(
    preselectedService: ServiceItem?,
    onDismiss: () -> Unit,
    onSubmit: (clientName: String, clientPhone: String, clientType: String, serviceTitle: String, platformName: String, category: String, notes: String, isUrgent: Boolean) -> Unit
) {
    var clientName by remember { mutableStateOf("") }
    var clientPhone by remember { mutableStateOf("") }
    var clientType by remember { mutableStateOf("مؤسسة فردية") }
    var serviceTitle by remember { mutableStateOf(preselectedService?.title ?: "") }
    var platformName by remember { mutableStateOf(preselectedService?.platformName ?: "المنصات الحكومية") }
    var categoryName by remember { mutableStateOf(preselectedService?.category?.displayName ?: "المنصات الحكومية") }
    var notes by remember { mutableStateOf("") }
    var isUrgent by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val clientTypes = listOf("أفراد", "مؤسسة فردية", "شركة")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "تقديم طلب معاملة جديدة",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Start
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "سيتم تسجيل طلبك مباشرة في نظام مكتب الوطن تحت إشراف أ. ماجد سعود العميري وإصدار رقم تتبع فوري.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = clientName,
                    onValueChange = { clientName = it },
                    label = { Text("اسم العميل / اسم المنشأة *") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_client_name")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = clientPhone,
                    onValueChange = { clientPhone = it },
                    label = { Text("رقم الجوال للتواصل وتلقي التحديثات *") },
                    singleLine = true,
                    placeholder = { Text("05xxxxxxxx") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_client_phone")
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "نوع الكيان:",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    clientTypes.forEach { type ->
                        val isSelected = clientType == type
                        Surface(
                            onClick = { clientType = type },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                            border = if (isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = type,
                                modifier = Modifier.padding(vertical = 8.dp),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = serviceTitle,
                    onValueChange = { serviceTitle = it },
                    label = { Text("الخدمة المطلوبة *") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_service_title")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("تفاصيل وملاحظات إضافية") },
                    maxLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_notes")
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "معاملة عاجلة (خلال 24 ساعة)",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "أولوية قصوى للمعاملات الطارئة ورخص العمل",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = isUrgent,
                        onCheckedChange = { isUrgent = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = GreenPrimary
                        ),
                        modifier = Modifier.testTag("switch_urgent")
                    )
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (clientName.isBlank() || clientPhone.isBlank() || serviceTitle.isBlank()) {
                        errorMessage = "يرجى تعبئة الحقول الإلزامية (*)"
                    } else {
                        onSubmit(
                            clientName.trim(),
                            clientPhone.trim(),
                            clientType,
                            serviceTitle.trim(),
                            platformName,
                            categoryName,
                            notes.trim(),
                            isUrgent
                        )
                    }
                },
                modifier = Modifier.testTag("submit_order_button")
            ) {
                Text("إرسال واعتماد الطلب")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("cancel_order_button")
            ) {
                Text("إلغاء")
            }
        }
    )
}

@Composable
fun ServiceDetailDialog(
    service: ServiceItem,
    onDismiss: () -> Unit,
    onRequestNow: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    tint = GreenPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = service.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "المنصة المستهدفة: ${service.platformName}",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = service.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "نطاق العمل والإنجاز:",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                service.detailedScope.forEach { scope ->
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text("• ", color = GreenPrimary, fontWeight = FontWeight.Bold)
                        Text(
                            text = scope,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "المستندات المطلوبة:",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = GoldSecondary
                    )
                )
                service.requiredDocs.forEach { doc ->
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text("✔ ", color = GoldSecondary, fontSize = 11.sp)
                        Text(
                            text = doc,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "مدة الإنجاز: ${service.processingDuration}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "الرسوم: ${service.officialFeeGuide}",
                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onRequestNow,
                modifier = Modifier.testTag("detail_request_button")
            ) {
                Text("طلب هذه الخدمة الآن")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إغلاق")
            }
        }
    )
}
