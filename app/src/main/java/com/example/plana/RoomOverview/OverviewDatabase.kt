package com.example.plana.RoomOverview

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [OverviewEntity::class], version = 1, exportSchema = false )

abstract class OverviewDatabase() : RoomDatabase() {

    /**firstly, connect the database to the Dao*/

    abstract fun overviewDao(): OverviewDao

    companion object{

        @Volatile
        private var INSTANCE: OverviewDatabase? = null

        fun getInstance(context: Context): OverviewDatabase {

            synchronized(this){
                var instance = INSTANCE

                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        OverviewDatabase::class.java,
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
