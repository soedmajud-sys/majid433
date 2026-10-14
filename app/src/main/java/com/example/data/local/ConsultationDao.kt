package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.ConsultationBookingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsultationDao {
    @Query("SELECT * FROM consultation_bookings ORDER BY id DESC")
    fun getAllBookings(): Flow<List<ConsultationBookingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: ConsultationBookingEntity): Long

    @Delete
    suspend fun deleteBooking(booking: ConsultationBookingEntity)

    @Query("SELECT COUNT(*) FROM consultation_bookings")
    suspend fun getCount(): Int
}
