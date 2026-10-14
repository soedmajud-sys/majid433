package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.FinancialRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FinancialDao {
    @Query("SELECT * FROM financial_records ORDER BY issueDate DESC")
    fun getAllRecords(): Flow<List<FinancialRecordEntity>>

    @Query("SELECT * FROM financial_records WHERE referenceNumber = :ref LIMIT 1")
    suspend fun getRecordByRef(ref: String): FinancialRecordEntity?

    @Query("SELECT * FROM financial_records WHERE type = :type ORDER BY issueDate DESC")
    fun getRecordsByType(type: String): Flow<List<FinancialRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: FinancialRecordEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(records: List<FinancialRecordEntity>)

    @Query("SELECT COUNT(*) FROM financial_records")
    suspend fun getCount(): Int
}
