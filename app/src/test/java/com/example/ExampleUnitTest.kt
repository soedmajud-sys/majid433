package com.example

import com.example.data.model.AlWatanConstants
import com.example.data.model.ServiceCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testAlWatanConstants() {
        assertEquals("FL-827973875", AlWatanConstants.FREELANCE_DOC_NO)
        assertEquals("1064454570", AlWatanConstants.NATIONAL_ID)
        assertEquals("00551660", AlWatanConstants.ADMIN_ACCESS_KEY)
        assertEquals("soedmajud@gmail.com", AlWatanConstants.OFFICIAL_EMAIL)
        assertEquals("https://maktab-alwatan.com", AlWatanConstants.OFFICIAL_DOMAIN)
        assertEquals("https://maktab-alwatan.com/privacy", AlWatanConstants.PRIVACY_URL)
        assertTrue(AlWatanConstants.OFFICIAL_PHONES.contains("0555614852"))
        assertTrue(AlWatanConstants.OFFICIAL_PHONES.contains("0556907115"))
        assertTrue(AlWatanConstants.OFFICIAL_PHONES.contains("0551683899"))
    }

    @Test
    fun testServiceCategories() {
        assertNotNull(ServiceCategory.GOVERNMENT)
        assertNotNull(ServiceCategory.SAP_ERP)
        assertNotNull(ServiceCategory.PRIVATE_BUSINESS)
        assertNotNull(ServiceCategory.IT_SYSTEMS)
        assertEquals("المنصات الحكومية", ServiceCategory.GOVERNMENT.displayName)
        assertEquals("برمجة نظم ساب (SAP)", ServiceCategory.SAP_ERP.displayName)
    }
}
