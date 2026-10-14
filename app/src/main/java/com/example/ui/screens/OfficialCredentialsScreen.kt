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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AlWatanConstants
import com.example.ui.components.ContactIntents
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.SapAccentBlue

@Composable
fun OfficialCredentialsScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("official_credentials_lazy_column")
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))

            // Document Header
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("credentials_header_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Gavel,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "مستند الملكية الفكرية والحقوق التجارية",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )

                    Text(
                        text = "Digital Solutions Platform | منصة الحلول الرقمية",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    )
                }
            }
        }

        // Owner & Official License Info
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, GoldSecondary.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = GoldSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "بيانات المالك والمطور والوثيقة الرسمية",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    InfoRow(label = "الاسم الكريم:", value = AlWatanConstants.OWNER_FULL_NAME)
                    InfoRow(label = "الهوية الوطنية:", value = AlWatanConstants.NATIONAL_ID)
                    InfoRow(label = "الصفة النظامية:", value = AlWatanConstants.LEGAL_STATUS)
                    InfoRow(label = "جهة الإصدار:", value = AlWatanConstants.ISSUING_AUTHORITY)
                    InfoRow(label = "رقم وثيقة العمل الحر:", value = AlWatanConstants.FREELANCE_DOC_NO, isHighlighted = true)
                    InfoRow(label = "التخصص المعتمد:", value = AlWatanConstants.SPECIALIZATION, isSap = true)
                }
            }
        }

        // Contact & Domain Verification
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "بيانات الاتصال والنطاق المعتمد",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "أرقام الجوال الرسمية المعتمدة:",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    AlWatanConstants.OFFICIAL_PHONES.forEach { phone ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = phone,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = GreenPrimary
                                )
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                OutlinedButton(
                                    onClick = { ContactIntents.dialPhone(context, phone) },
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("اتصال", fontSize = 11.sp)
                                }
                                Button(
                                    onClick = { ContactIntents.openWhatsApp(context, phone, "السلام عليكم") },
                                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("واتساب", fontSize = 11.sp, color = Color.White)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    InfoRow(label = "البريد الإلكتروني:", value = AlWatanConstants.OFFICIAL_EMAIL)

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { ContactIntents.openUrl(context, AlWatanConstants.OFFICIAL_DOMAIN) },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.Language, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("النطاق السحابي", fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = { ContactIntents.openUrl(context, AlWatanConstants.PRIVACY_URL) },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.PrivacyTip, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("سياسة الخصوصية", fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Intellectual Property Legal Statement
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = GreenPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "بيان حفظ الملكية الفكرية",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "بموجب وثيقة العمل الحر رقم ${AlWatanConstants.FREELANCE_DOC_NO} الصادرة من وزارة الموارد البشرية والتنمية الاجتماعية، يمتلك الأستاذ ماجد بن سعود بن فواز العميري كافة حقوق الملكية الفكرية والبرمجية الكاملة والمادية والمعنوية لمنصة الحلول الرقمية وكافة تطبيقاتها ومواقعها وخدماتها السحابية، ويحظر أي استنساخ أو تعديل أو استغلال تجاري بدون إذن خطي مسبق.",
                        style = MaterialTheme.typography.bodySmall,
                        lineHeight = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Technical Architecture Summary
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "الهيكلة التقنية والبرمجية (Technical Architecture)",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "• البنية التحتية: خوادم Node.js سحابية وقواعد بيانات MongoDB مؤمنة بشهادات SSL.",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "• حزمة التطبيق المعتمدة: com.maktabalwatan.digitalsolutions",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = SapAccentBlue,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = "• النشر المعتمد: Google Play Console (AAB) & Apple App Store (IPA)",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun InfoRow(
    label: String,
    value: String,
    isHighlighted: Boolean = false,
    isSap: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold,
                color = when {
                    isHighlighted -> GreenPrimary
                    isSap -> SapAccentBlue
                    else -> MaterialTheme.colorScheme.onSurface
                }
            )
        )
    }
}
