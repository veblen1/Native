package com.sunbell.nicepricemap.room

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sunbell.nicepricemap.view.util.parseCsvFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// 현재 위치 DB
@Entity(tableName = "restaurant")
data class Restaurant(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val region: String,
    val district: String,
    val category: String,
    val name: String,
    val contact: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val menu1: String,
    val price1: Int,
    val menu2: String,
    val price2: Int,
    val menu3: String,
    val price3: Int,
    val visit: Boolean
)

// DAO
@Dao
interface RestaurantDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(restaurants: List<Restaurant>)

    @Query("SELECT * FROM restaurant")
    fun getAllRestaurants(): List<Restaurant>


}

// Room 데이터베이스
@Database(entities = [Restaurant::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun restaurantDao(): RestaurantDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "BangRangDB"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                val inputStream = context.assets.open("20230428.csv")
                                getDatabase(context).restaurantDao().insertAll(parseCsvFile(inputStream))
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}