package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrackChanges
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AlWatanConstants
import com.example.data.model.PlatformGuide
import com.example.data.model.ServiceCategory
import com.example.data.model.ServiceItem
import com.example.ui.AppScreen
import com.example.ui.components.ContactIntents
import com.example.ui.components.ExecutiveHeader
import com.example.ui.components.QuickContactBar
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.SapAccentBlue
import com.example.ui.theme.TechTertiary

@Composable
fun HomeScreen(
    featuredServices: List<ServiceItem>,
    platformGuides: List<PlatformGuide>,
    onNavigate: (AppScreen) -> Unit,
    onSelectService: (ServiceItem) -> Unit,
    onRequestNewOrder: () -> Unit,
    user: com.google.firebase.auth.FirebaseUser? = null,
    isAuthLoading: Boolean = false,
    onSignInClick: () -> Unit = {},
    onSignOutClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen_lazy_column")
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            ExecutiveHeader(
                onOpenCredentials = { onNavigate(AppScreen.OFFICIAL_CREDENTIALS) }
            )
        }

        // Firebase Cloud Sync & Google Auth Banner
        item {
            com.example.ui.components.GoogleSignInBanner(
                user = user,
                isLoading = isAuthLoading,
                onSignInClick = onSignInClick,
                onSignOutClick = onSignOutClick
            )
        }

        // Hero Image Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .testTag("hero_banner_card"),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_hero_banner),
                        contentDescription = "بانر الحلول الرقمية ونظم ساب",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.2f),
                                        Color.Black.copy(alpha = 0.85f)
                                    )
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Surface(
                            color = GreenPrimary,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "تخصص ساب & المنصات الحكومية",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "منظومة رقمية شاملة لحلول الأعمال والأنظمة المؤسسية",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "ربط وتكامل ساب مع الفوترة الإلكترونية ZATCA وإدارة كافة المنصات",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        )
                    }
                }
            }
        }

        // Quick Direct Contact Bar
        item {
            QuickContactBar()
        }

        // Executive Supervisor Official Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("supervisor_official_card")
                    .clickable { onNavigate(AppScreen.OFFICIAL_CREDENTIALS) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, GoldSecondary.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(GoldSecondary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = GoldSecondary,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "المالك والمشرف العام",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = GoldSecondary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = GreenPrimary.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = "مستقل معتمد",
                                    color = GreenPrimary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Text(
                            text = AlWatanConstants.OWNER_FULL_NAME,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )

                        Text(
                            text = "وثيقة العمل الحر: ${AlWatanConstants.FREELANCE_DOC_NO} (وزارة الموارد البشرية)",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "عرض التفاصيل",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Key Performance Metrics
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("metrics_row"),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricItem(
                    number = "+1,850",
                    label = "معاملة منجزة",
                    icon = Icons.Default.CheckCircle,
                    color = GreenPrimary,
                    modifier = Modifier.weight(1f)
                )
                MetricItem(
                    number = "+45",
                    label = "مشروع ساب (SAP)",
                    icon = Icons.Default.Dns,
                    color = SapAccentBlue,
                    modifier = Modifier.weight(1f)
                )
                MetricItem(
                    number = "100%",
                    label = "امتثال حكومي",
                    icon = Icons.Default.Security,
                    color = GoldSecondary,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Quick Navigation Grid
        item {
            Text(
                text = "بوابات المنصة الرئيسية",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PortalCard(
                        title = "المنصات الحكومية",
                        subtitle = "قوى، مُدد، مقيم، الزكاة، بلدي، أبشر",
                        icon = Icons.Default.AccountBalance,
                        tintColor = GreenPrimary,
                        onClick = { onNavigate(AppScreen.SERVICES) },
                        modifier = Modifier.weight(1f),
                        testTag = "portal_gov_button"
                    )

                    PortalCard(
                        title = "تخصص ساب (SAP)",
                        subtitle = "تطبيق وحلول S/4HANA والفوترة ZATCA",
                        icon = Icons.Default.Dns,
                        tintColor = SapAccentBlue,
                        onClick = { onNavigate(AppScreen.SAP_SYSTEMS) },
                        modifier = Modifier.weight(1f),
                        testTag = "portal_sap_button"
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PortalCard(
                        title = "متابعة الطلبات",
                        subtitle = "استعلام مباشر برقم المعاملة",
                        icon = Icons.Default.TrackChanges,
                        tintColor = TechTertiary,
                        onClick = { onNavigate(AppScreen.ORDER_TRACKER) },
                        modifier = Modifier.weight(1f),
                        testTag = "portal_tracker_button"
                    )

                    PortalCard(
                        title = "حجز استشارة المشرف",
                        subtitle = "جلسة استشارية مع أ. ماجد العميري",
                        icon = Icons.Default.DateRange,
                        tintColor = GoldSecondary,
                        onClick = { onNavigate(AppScreen.CONSULTATION) },
                        modifier = Modifier.weight(1f),
                        testTag = "portal_consultation_button"
                    )
                }
            }
        }

        // Call to action button to submit order
        item {
            Button(
                onClick = onRequestNewOrder,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("home_submit_new_order_button"),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "تقديم طلب معاملة جديدة فوراً",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
        }

        // Featured Services Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "الخدمات الأكثر طلباً",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                OutlinedButton(
                    onClick = { onNavigate(AppScreen.SERVICES) },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.testTag("view_all_services_button")
                ) {
                    Text("عرض الكل", fontSize = 12.sp)
                }
            }
        }

        items(featuredServices) { service ->
            ServiceCard(
                service = service,
                onClick = { onSelectService(service) },
                testTag = "featured_service_${service.id}"
            )
        }

        // Government Platforms Knowledge Strip
        item {
            Text(
                text = "دليل المنصات الحكومية الرسمية",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "نقدم الدعم والمساندة الفنية والربط المباشر مع جميع هذه المنصات",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(platformGuides) { guide ->
                    Card(
                        modifier = Modifier
                            .width(240.dp)
                            .testTag("guide_card_${guide.platformName}")
                            .clickable {
                                ContactIntents.openUrl(context, guide.portalUrl)
                            },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = guide.platformName,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = GreenPrimary
                                    )
                                )
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = GreenPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = guide.authority,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = guide.purpose,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 2,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "زيارة البوابة الرسمية ↗",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }

        // Bottom Footer with Intellectual Property & Rights
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(AppScreen.OFFICIAL_CREDENTIALS) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "كافة الحقوق محفوظة © لمنصة الحلول الرقمية - مكتب الوطن",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "بموجب وثيقة العمل الحر رقم FL-827973875 | النطاق الرسمي: maktab-alwatan.com",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MetricItem(
    number: String,
    label: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = number,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun PortalCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    tintColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String
) {
    Card(
        modifier = modifier
            .testTag(testTag)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(tintColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tintColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )
        }
    }
}

@Composable
fun ServiceCard(
    service: ServiceItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag(testTag)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = service.platformName,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                if (service.badge != null) {
                    Surface(
                        color = GoldSecondary.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = service.badge,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = GoldSecondary,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = service.title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = service.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "مدة الإنجاز: ${service.processingDuration}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )

                Text(
                    text = "تفاصيل الخدمة والتقديم ←",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}
