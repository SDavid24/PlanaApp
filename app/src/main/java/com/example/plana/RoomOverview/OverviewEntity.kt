package com.example.plana.RoomOverview

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.plana.Models.detailModel

@Entity(tableName = "overview-table")
data class OverviewEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var image: Int,
    var category: String,
    var taskAmount: String,
   // var taskDetails: ArrayList<String>
)

