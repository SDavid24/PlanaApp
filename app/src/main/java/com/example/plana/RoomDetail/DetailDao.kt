package com.example.plana.RoomDetail

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DetailDao {

    @Insert
    suspend fun insert(detailEntity : DetailEntity)

    @Update
    suspend fun update(detailEntity: DetailEntity)

    @Delete
    suspend fun delete(detailEntity:DetailEntity)

    @Query("SELECT * FROM `detail-table`")
    fun fetchAllTaskCategory(): Flow<List<DetailEntity>>

    @Query("SELECT * FROM `detail-table` where id =:id")
    fun fetchTaskCategoryById(id: Int): Flow<DetailEntity>

    @Query("SELECT EXISTS (SELECT 1 FROM `detail-table` WHERE Category = :category)")
    fun exists(category: String): Boolean

}