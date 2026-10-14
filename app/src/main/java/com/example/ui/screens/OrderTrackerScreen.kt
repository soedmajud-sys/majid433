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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AlWatanConstants
import com.example.data.model.ServiceOrderEntity
import com.example.ui.components.StatusBadge
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.GreenPrimary

@Composable
fun OrderTrackerScreen(
    allOrders: List<ServiceOrderEntity>,
    trackedOrder: ServiceOrderEntity?,
    searchAttempted: Boolean,
    onSearchOrder: (String) -> Unit,
    onRequestNewOrder: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchInput by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("order_tracker_lazy_column")
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "نظام متابعة الطلبات والمعاملات",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "تتبع فوري لمراحل إنجاز المعاملات الحكومية ومشاريع نظم ساب",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Search Input Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("order_search_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Text(
                        text = "الاستعلام المباشر:",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = searchInput,
                        onValueChange = { searchInput = it },
                        placeholder = { Text("أدخل رقم المعاملة (مثال: WTN-2026-1048) أو الجوال") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "بحث")
                        },
                        trailingIcon = {
                            if (searchInput.isNotEmpty()) {
                                IconButton(onClick = { searchInput = "" }) {
                                    Icon(imageVector = Icons.Default.Clear, contentDescription = "مسح")
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_tracking_number")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = { onSearchOrder(searchInput) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("button_search_order"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.TrackChanges, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("استعلام عن حالة الطلب", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Active Tracked Order Result
        if (trackedOrder != null) {
            item {
                Text(
                    text = "نتيجة الاستعلام عن المعاملة:",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = GreenPrimary
                    )
                )
            }

            item {
                OrderFullDetailCard(order = trackedOrder)
            }
        } else if (searchAttempted && searchInput.isNotBlank()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "لم يتم العثور على معاملة مطابقة لرقم البحث",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "يرجى التحقق من صحة رقم المعاملة (WTN-2026-XXXX) أو رقم الجوال المدخل عند التقديم.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }

        // History of All Orders
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "سجل المعاملات الحالية (${allOrders.size})",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Button(
                    onClick = onRequestNewOrder,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.testTag("tracker_submit_new_button")
                ) {
                    Text("+ طلب جديد", fontSize = 12.sp)
                }
            }
        }

        items(allOrders) { order ->
            OrderSummaryCard(order = order)
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun OrderFullDetailCard(order: ServiceOrderEntity) {
    val stageIndex = when (order.status) {
        "قيد المراجعة" -> 1
        "جاري المعالجة والتدقيق", "جاري المعالجة" -> 2
        "في مرحلة الاعتماد" -> 3
        "مكتمل بنجاح", "مكتمل" -> 4
        else -> 2
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("order_full_detail_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.5.dp, GreenPrimary.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = order.id,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = GreenPrimary
                    )
                )
                StatusBadge(status = order.status)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = order.serviceTitle,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "العميل: ${order.clientName} (${order.clientType})",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "المنصة: ${order.platformName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Stage Visual Stepper
            Text(
                text = "مراحل سير المعاملة:",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StageStep(
                    stepNumber = 1,
                    label = "استلام الطلب",
                    isCompleted = stageIndex >= 1,
                    isCurrent = stageIndex == 1
                )
                StageStep(
                    stepNumber = 2,
                    label = "التدقيق والمطابقة",
                    isCompleted = stageIndex >= 2,
                    isCurrent = stageIndex == 2
                )
                StageStep(
                    stepNumber = 3,
                    label = "مراجعة المنصة",
                    isCompleted = stageIndex >= 3,
                    isCurrent = stageIndex == 3
                )
                StageStep(
                    stepNumber = 4,
                    label = "الاعتماد النهائي",
                    isCompleted = stageIndex >= 4,
                    isCurrent = stageIndex == 4
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "آخر تحديث من فريق العمل:",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = order.trackingNotes,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "تاريخ التقديم: ${order.createdAtFormatted}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "الإنجاز المتوقع: ${order.estimatedCompletion}",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    tint = GoldSecondary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "تحت متابعة وإشراف مباشر من أ. ${AlWatanConstants.OWNER_FULL_NAME}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = GoldSecondary,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
fun StageStep(
    stepNumber: Int,
    label: String,
    isCompleted: Boolean,
    isCurrent: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(72.dp)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(
                    when {
                        isCurrent -> GoldSecondary
                        isCompleted -> GreenPrimary
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted && !isCurrent) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Text(
                    text = "$stepNumber",
                    color = if (isCompleted || isCurrent) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                color = if (isCurrent) GoldSecondary else MaterialTheme.colorScheme.onSurface
            ),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            maxLines = 2
        )
    }
}

@Composable
fun OrderSummaryCard(order: ServiceOrderEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("order_item_${order.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
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

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = order.serviceTitle,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "العميل: ${order.clientName} | ${order.platformName}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = order.trackingNotes,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "التاريخ: ${order.createdAtFormatted}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (order.isUrgent) {
                    Surface(
                        color = MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "عاجلة",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
