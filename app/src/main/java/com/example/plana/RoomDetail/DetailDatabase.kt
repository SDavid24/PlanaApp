package com.example.plana.RoomDetail

import android.content.Context
import androidx.room.*

@Database(entities = [DetailEntity::class], version = 4, exportSchema = false )
@TypeConverters(TaskListConverter::class)
abstract class DetailDatabase : RoomDatabase() {


    /**firstly, connect the database to the Dao*/

    abstract fun detailDao(): DetailDao

    companion object{

        @Volatile
        private var INSTANCE: DetailDatabase? = null

        fun getInstance(context: Context): DetailDatabase {

            synchronized(this){
                var instance = INSTANCE

                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DetailDatabase::class.java,
                        "overview_database"
                    ).fallbackToDestructiveMigration().build()

                    // Assign INSTANCE to the newly created database.
                    INSTANCE = instance
                }

                // Return instance; smart cast to be non-null.
                return instance
            }
        }
    }


}