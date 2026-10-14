package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ServiceOrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("SELECT * FROM service_orders ORDER BY createdAtFormatted DESC")
    fun getAllOrders(): Flow<List<ServiceOrderEntity>>

    @Query("SELECT * FROM service_orders WHERE id = :orderId LIMIT 1")
    suspend fun getOrderById(orderId: String): ServiceOrderEntity?

    @Query("SELECT * FROM service_orders WHERE clientPhone = :phone ORDER BY createdAtFormatted DESC")
    fun getOrdersByPhone(phone: String): Flow<List<ServiceOrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: ServiceOrderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(orders: List<ServiceOrderEntity>)

    @Update
    suspend fun updateOrder(order: ServiceOrderEntity)

    @Query("SELECT COUNT(*) FROM service_orders")
    suspend fun getCount(): Int
}
