package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.ConsultationBookingEntity
import com.example.data.model.FinancialRecordEntity
import com.example.data.model.ServiceOrderEntity

@Database(
    entities = [
        ServiceOrderEntity::class,
        ConsultationBookingEntity::class,
        FinancialRecordEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun consultationDao(): ConsultationDao
    abstract fun financialDao(): FinancialDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "maktab_alwatan_db"
                ).fallbackToDestructiveMigration()
                 .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
