package com.example.plana.RoomOverview

import androidx.room.TypeConverter
import com.example.plana.RoomDetail.DetailEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object TypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun toEmployeeEntity(data:String) : MutableList<DetailEntity>? {

        return if (data.isEmpty()){
            emptyList<DetailEntity>().toMutableList()
        }else{
            val listType = object : TypeToken<MutableList<DetailEntity>>() {}.type
            gson.fromJson(data,listType)
        }
    }

    @TypeConverter
    fun fromEmployeeEntity(list: MutableList<DetailEntity>?) : String {
        return gson.toJson(list)
    }

}