package com.example.plana.RoomDetail

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

@Entity(tableName = "detail-table")
data class DetailEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "Image") var image: Int? = null,
    @ColumnInfo(name = "Task amount ") var taskAmount: Int? = null,
    @ColumnInfo(name = "Category") var category: String = "",
    @ColumnInfo(name = "Task list") var taskList: MutableList<TaskList> = mutableListOf<TaskList>()
) : Serializable

data class TaskList(val tasks : String): Serializable

class TaskListConverter{
    @TypeConverter
    fun listToJson(value: MutableList<TaskList>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String): MutableList<TaskList>? {

        return if (value.isEmpty()){
            emptyList<TaskList>().toMutableList()
        }else{
            val listType = object : TypeToken<MutableList<TaskList>>() {}.type
            Gson().fromJson(value,listType)
        }
    }
}
