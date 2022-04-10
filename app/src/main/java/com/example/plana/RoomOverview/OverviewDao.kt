package com.example.plana.RoomOverview

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface OverviewDao {

    @Insert
    suspend fun insert(overviewEntity: OverviewEntity)



    @Update
    suspend fun update(overviewEntity: OverviewEntity)

    @Delete
    suspend fun delete(overviewEntity: OverviewEntity)

    @Query("SELECT * FROM `overview-table`")
    fun fetchAllTaskCategory(): Flow<List<OverviewEntity>>

    @Query("SELECT * FROM `overview-table` where id =:id")
    fun fetchTaskCategoryById(id: Int): Flow<OverviewEntity>
}

