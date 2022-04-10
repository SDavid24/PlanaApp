package com.example.plana.Databases

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.plana.Models.detailModel

class Details_DBHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "Task Detail Database"
        private const val TABLE_NAME = "TaskDetail"
        private const val KEY_ID = "_id"
       // private const val KEY_CHECKBOX = "check"
        private const val KEY_TASK_DETAIL = "Task_amount"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
               // + KEY_CHECKBOX + " INTEGER DEFAULT 0"
                + KEY_TASK_DETAIL + " TEXT" + ")")
        db!!.execSQL(createTable)

    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int)  {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    /**
     * Function to read data
     */
    //method to read data
    fun viewTaskDetail(): ArrayList<detailModel> {

        val taskAmountList: ArrayList<detailModel> = ArrayList()

        val selectQuery = "SELECT  * FROM $TABLE_NAME"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
     //   var checkBox : Int
        var taskDetail: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                taskDetail = cursor.getString(cursor.getColumnIndex(KEY_TASK_DETAIL) )

                val emp = detailModel(id = id,  taskDetail = taskDetail)
                taskAmountList.add(emp)

            } while (cursor.moveToNext())
        }
        return taskAmountList
    }

    fun addTaskDetail(detModel : detailModel) : Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(KEY_ID, detModel.id)
       // contentValues.put(KEY_CHECKBOX, detModel.checkbox)
        contentValues.put(KEY_TASK_DETAIL, detModel.taskDetail)

        val success = db.insert(TABLE_NAME, null, contentValues)

        db.close()

        return success

    }

    fun updateTaskDetail(detModel : detailModel) : Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(KEY_ID, detModel.id)
        contentValues.put(KEY_TASK_DETAIL, detModel.taskDetail)

        val success = db.update(TABLE_NAME, contentValues,
            KEY_ID + "=" + detModel.id, null  )

        db.close()

        return success
    }

    /**
     * Function to delete record
     */
    fun deleteTask(detModel: detailModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, detModel.id) // EmpModelClass id
        // Deleting Row
        val success = db.delete(TABLE_NAME, KEY_ID + "=" + detModel.id, null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }

}