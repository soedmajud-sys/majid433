package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AlWatanConstants
import com.example.data.model.FinancialRecordEntity
import com.example.data.model.ServiceOrderEntity
import com.example.ui.components.StatusBadge
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.GreenPrimary

@Composable
fun AdminPortalScreen(
    isAuthenticated: Boolean,
    authError: String?,
    allOrders: List<ServiceOrderEntity>,
    financialRecords: List<FinancialRecordEntity>,
    onAuthenticate: (String) -> Boolean,
    onLogout: () -> Unit,
    onUpdateOrderStatus: (orderId: String, newStatus: String, notes: String) -> Unit,
    onCreateFinancialRecord: (type: String, clientName: String, clientTaxOrId: String, serviceTitle: String, amount: Double, notes: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var passcodeInput by remember { mutableStateOf("") }
    var orderToUpdate by remember { mutableStateOf<ServiceOrderEntity?>(null) }
    var showCreateRecordDialog by remember { mutableStateOf(false) }

    if (!isAuthenticated) {
        // Login Gate
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp)
                .testTag("admin_login_container"),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(GreenPrimary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AdminPanelSettings,
                    contentDescription = null,
                    tint = GreenPrimary,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "بوابة الإدارة الحصرية للمشرف",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )

            Text(
                text = "خاصة بإدارة وإشراف الأستاذ ${AlWatanConstants.OWNER_FULL_NAME}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "أدخل رمز الدخول الإداري المعتمد:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = passcodeInput,
                        onValueChange = { passcodeInput = it },
                        label = { Text("رمز الدخول (ADMIN_ACCESS_KEY)") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_passcode_input")
                    )

                    if (authError != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = authError,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { onAuthenticate(passcodeInput) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("admin_login_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.LockOpen, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("تسجيل الدخول للوحة الإدارة", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    } else {
        // Authenticated Admin Dashboard
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .testTag("admin_dashboard_lazy_column")
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "لوحة التحكم الإدارية",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "المشرف: ${AlWatanConstants.OWNER_FULL_NAME} | وثيقة: ${AlWatanConstants.FREELANCE_DOC_NO}",
                            style = MaterialTheme.typography.labelSmall,
                            color = GreenPrimary
                        )
                    }

                    OutlinedButton(
                        onClick = onLogout,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("admin_logout_button")
                    ) {
                        Icon(imageVector = Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("خروج", fontSize = 12.sp)
                    }
                }
            }

            // Quick Stats Row
            item {
                val totalFinancial = financialRecords.sumOf { it.totalAmount }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AdminStatCard(
                        title = "الطلبات الحالية",
                        value = "${allOrders.size}",
                        icon = Icons.Default.Description,
                        color = GreenPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    AdminStatCard(
                        title = "السجلات المالية",
                        value = "${financialRecords.size}",
                        icon = Icons.Default.Receipt,
                        color = GoldSecondary,
                        modifier = Modifier.weight(1f)
                    )
                    AdminStatCard(
                        title = "إجمالي المبالغ",
                        value = "${totalFinancial.toInt()} ر.س",
                        icon = Icons.Default.AttachMoney,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1.2f)
                    )
                }
            }

            // Actions Bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { showCreateRecordDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("admin_create_invoice_button")
                    ) {
                        Icon(imageVector = Icons.Default.PostAdd, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("إصدار عقد / فاتورة / سند", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Active Orders Management
            item {
                Text(
                    text = "إدارة المعاملات وتحديث الحالات (${allOrders.size})",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "اضغط على أي معاملة لتحديث حالتها أو كتابة ملاحظات تتبع للعميل",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            items(allOrders) { order ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { orderToUpdate = order },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = order.id,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = GreenPrimary
                                )
                            )
                            StatusBadge(status = order.status)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${order.serviceTitle} - ${order.clientName} (${order.clientPhone})",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "ملاحظات التتبع: ${order.trackingNotes}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "تعديل الحالة ✎",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }

            // Financial Records List
            item {
                Text(
                    text = "سجل الفواتير والعقود المعتمدة (${financialRecords.size})",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            items(financialRecords) { record ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${record.type} | ${record.referenceNumber}",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = GoldSecondary
                                )
                            )
                            Surface(
                                color = GreenPrimary.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = record.status,
                                    color = GreenPrimary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = record.clientName,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = record.serviceTitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "المبلغ: ${record.amount} ر.س + ضريبة: ${record.taxAmount} ر.س",
                                style = MaterialTheme.typography.labelSmall
                            )
                            Text(
                                text = "الإجمالي: ${record.totalAmount} ر.س",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
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

    // Update Status Dialog
    orderToUpdate?.let { order ->
        var selectedStatus by remember { mutableStateOf(order.status) }
        var notesText by remember { mutableStateOf(order.trackingNotes) }

        val statusOptions = listOf(
            "قيد المراجعة",
            "جاري المعالجة والتدقيق",
            "في مرحلة الاعتماد",
            "مكتمل بنجاح",
            "بانتظار متطلبات من العميل"
        )

        AlertDialog(
            onDismissRequest = { orderToUpdate = null },
            title = {
                Text(
                    text = "تحديث حالة المعاملة: ${order.id}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "الخدمة: ${order.serviceTitle}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "اختر الحالة الجديدة:",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )

                    statusOptions.forEach { status ->
                        val isSelected = selectedStatus == status
                        Surface(
                            onClick = { selectedStatus = status },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                        ) {
                            Text(
                                text = status,
                                modifier = Modifier.padding(10.dp),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = notesText,
                        onValueChange = { notesText = it },
                        label = { Text("ملاحظات التحديث للعميل") },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateOrderStatus(order.id, selectedStatus, notesText.trim())
                        orderToUpdate = null
                    }
                ) {
                    Text("حفظ وتحديث")
                }
            },
            dismissButton = {
                TextButton(onClick = { orderToUpdate = null }) {
                    Text("إلغاء")
                }
            }
        )
    }

    // Create Financial Record Dialog
    if (showCreateRecordDialog) {
        var recordType by remember { mutableStateOf("فاتورة ضريبية") }
        var clientName by remember { mutableStateOf("") }
        var clientTaxOrId by remember { mutableStateOf("") }
        var serviceTitle by remember { mutableStateOf("") }
        var amountStr by remember { mutableStateOf("") }
        var notes by remember { mutableStateOf("") }
        var errorMsg by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = { showCreateRecordDialog = false },
            title = {
                Text(
                    text = "إصدار مستند مالي رسمي",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("فاتورة ضريبية", "عقد إلكتروني", "سند قبض").forEach { type ->
                            val isSelected = recordType == type
                            Surface(
                                onClick = { recordType = type },
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) GreenPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = type,
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    textAlign = TextAlign.Center,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = clientName,
                        onValueChange = { clientName = it },
                        label = { Text("اسم العميل / المنشأة *") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = clientTaxOrId,
                        onValueChange = { clientTaxOrId = it },
                        label = { Text("الرقم الضريبي أو السجل التجاري") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = serviceTitle,
                        onValueChange = { serviceTitle = it },
                        label = { Text("وصف الخدمة أو المشروع *") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = amountStr,
                        onValueChange = { amountStr = it },
                        label = { Text("المبلغ الأساسي (ر.س) *") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("شروط وملاحظات إضافية") },
                        maxLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (errorMsg != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = errorMsg ?: "", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amount = amountStr.toDoubleOrNull()
                        if (clientName.isBlank() || serviceTitle.isBlank() || amount == null || amount <= 0) {
                            errorMsg = "يرجى إدخال اسم العميل والخدمة والمبلغ بشكل صحيح"
                        } else {
                            onCreateFinancialRecord(
                                recordType,
                                clientName.trim(),
                                clientTaxOrId.trim(),
                                serviceTitle.trim(),
                                amount,
                                notes.trim()
                            )
                            showCreateRecordDialog = false
                        }
                    }
                ) {
                    Text("إصدار واعتماد")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateRecordDialog = false }) {
                    Text("إلغاء")
                }
            }
        )
    }
}

@Composable
fun AdminStatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = color))
            Text(text = title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp)
        }
    }
}
