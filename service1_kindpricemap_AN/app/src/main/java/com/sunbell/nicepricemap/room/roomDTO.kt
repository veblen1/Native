package com.sunbell.nicepricemap.room

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

// 현재 위치 DB
@Entity(tableName = "current_location")
data class CheckStore(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val name: String,
    val went: Boolean,
)

// DAO
@Dao
interface UserLocationDao {

}

// Room 데이터베이스
@Database(entities = [CheckStore::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userLocationDao(): UserLocationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "BangRangDB"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}