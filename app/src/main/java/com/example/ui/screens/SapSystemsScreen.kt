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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import com.example.data.model.AlWatanConstants
import com.example.data.model.SapModuleInfo
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.SapAccentBlue

@Composable
fun SapSystemsScreen(
    sapModules: List<SapModuleInfo>,
    onBookConsultation: () -> Unit,
    onRequestSapProject: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedModuleForDetail by remember { mutableStateOf<SapModuleInfo?>(null) }

    // Estimator State
    val selectedModulesForEstimation = remember { mutableStateListOf("FI", "SD", "MM") }
    var includeZatcaIntegration by remember { mutableStateOf(true) }
    var companySize by remember { mutableStateOf("متوسطة (50-250 موظف)") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("sap_systems_lazy_column")
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))

            // Dedicated Header for SAP Specialty
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("sap_specialty_header_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = SapAccentBlue.copy(alpha = 0.08f)
                ),
                border = BorderStroke(1.dp, SapAccentBlue.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            color = SapAccentBlue,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "تخصص ساب معتمد (SAP Certified)",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.Dns,
                            contentDescription = null,
                            tint = SapAccentBlue,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "برمجة وتطبيق نظم المعلومات - تخصص ساب (SAP)",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = SapAccentBlue
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "تحت إدارة وإشراف: الأستاذ ${AlWatanConstants.OWNER_FULL_NAME}\nوثيقة العمل الحر الرسمية: ${AlWatanConstants.FREELANCE_DOC_NO}\nصادرة من وزارة الموارد البشرية والتنمية الاجتماعية بالتخصص المعتمد في برمجة نظم المعلومات وساب.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onBookConsultation,
                            shape = RoundedCornerShape(10.dp),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = SapAccentBlue
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("sap_book_consultation_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("حجز استشارة ساب", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = onRequestSapProject,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("sap_request_project_button")
                        ) {
                            Text("طلب مشروع جديد", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // ZATCA Phase 2 E-Invoicing Highlight Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("sap_zatca_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = GreenPrimary.copy(alpha = 0.08f)
                ),
                border = BorderStroke(1.dp, GreenPrimary.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(GreenPrimary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ReceiptLong,
                            contentDescription = null,
                            tint = GreenPrimary,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "الربط والتكامل للفوترة الإلكترونية (ZATCA Phase 2)",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = GreenPrimary
                            )
                        )
                        Text(
                            text = "ربط مباشر بين نظام ساب وهيئة الزكاة، توليد أختام CSID و UBL 2.1 ورمز الاستجابة السريع QR المعتمد.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Core SAP Modules Breakdown
        item {
            Text(
                text = "وحدات وحلول ساب المعتمدة (SAP ERP Modules)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "اضغط على أي وحدة لاستعراض نطاق العمل والميزات التقنية",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        items(sapModules) { module ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("sap_module_card_${module.code}")
                    .clickable { selectedModuleForDetail = module },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(SapAccentBlue.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = module.code,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = SapAccentBlue
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = module.nameArabic,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "(${module.nameEnglish})",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = module.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2
                        )
                    }

                    Text(
                        text = "تفاصيل ←",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = SapAccentBlue,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        // SAP Project Cost & Timeline Estimator (Interactive Tool)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("sap_estimator_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Calculate,
                            contentDescription = null,
                            tint = GoldSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "حاسبة تقدير مشاريع نظم ساب والربط الرقمي",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "اختر الوحدات المطلوبة لحساب المدة التقديرية ومتطلبات المشروع الموصى بها:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "الوحدات المطلوبة:",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("FI", "CO", "MM", "SD", "HCM", "ABAP").forEach { code ->
                            val isChecked = selectedModulesForEstimation.contains(code)
                            Surface(
                                onClick = {
                                    if (isChecked) selectedModulesForEstimation.remove(code)
                                    else selectedModulesForEstimation.add(code)
                                },
                                shape = RoundedCornerShape(8.dp),
                                color = if (isChecked) SapAccentBlue else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = code,
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    color = if (isChecked) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // ZATCA toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = includeZatcaIntegration,
                            onCheckedChange = { includeZatcaIntegration = it },
                            colors = CheckboxDefaults.colors(checkedColor = GreenPrimary)
                        )
                        Text(
                            text = "تضمين الربط مع الفوترة الإلكترونية ZATCA المرحلة 2",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Calculation Summary
                    val baseWeeks = selectedModulesForEstimation.size * 3 + if (includeZatcaIntegration) 2 else 0
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "التقدير الأولي للمشروع:",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "• المدة الزمنية المقدرة: $baseWeeks - ${baseWeeks + 4} أسابيع",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "• عدد الوحدات: ${selectedModulesForEstimation.size} وحدات + ${if (includeZatcaIntegration) "بوابة فاتورة ZATCA" else "بدون ربط ضريبي"}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "• الإشراف المباشر: الأستاذ ماجد سعود العميري وفريق مهندسي ساب",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onBookConsultation,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("اعتماد ومناقشة التقدير في استشارة مجانية")
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Detail Dialog for selected SAP module
    selectedModuleForDetail?.let { module ->
        AlertDialog(
            onDismissRequest = { selectedModuleForDetail = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SapAccentBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = module.code,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = module.nameArabic,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = module.nameEnglish,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = module.description,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "المحاور والمكونات الرئيسية للوحدة:",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = SapAccentBlue
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    module.highlights.forEach { item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = GreenPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = item,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "يتم التكوين والبرمجة والتكامل الكامل وفق أفضل ممارسات SAP Best Practices العالمية.",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(8.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedModuleForDetail = null
                        onBookConsultation()
                    }
                ) {
                    Text("طلب استشارة لهذه الوحدة")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedModuleForDetail = null }) {
                    Text("إغلاق")
                }
            }
        )
    }
}
