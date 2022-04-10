package com.example.plana.Databases

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.plana.Models.Overview_model

class Overview_DBHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "Task Overview Database"
        private const val TABLE_NAME = "Task Overview"
        private const val KEY_ID = "_id"
        private const val KEY_IMAGE = "int"
        private const val KEY_CATEGORY = "category"
        private const val KEY_TASK_AMOUNT = "Task_amount"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_IMAGE + " INTEGER PRIMARY KEY,"
                + KEY_CATEGORY + " TEXT,"
                + KEY_TASK_AMOUNT + " INTEGER" + ")")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int)  {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }


    /**
     * Function to insert data
     */
    //method to read data
    fun viewTaskCategoryAmount(): ArrayList<Overview_model> {

        val taskAmountList: ArrayList<Overview_model> = ArrayList<Overview_model>()

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
        var image: Int
        var category: String
        var taskAmount: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                image = cursor.getInt(cursor.getColumnIndex(KEY_IMAGE))
                category = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY))
                taskAmount = cursor.getString(cursor.getColumnIndex(KEY_TASK_AMOUNT))

                val emp = Overview_model(id = id, image = image, category = category, taskAmount = taskAmount)
                taskAmountList.add(emp)

            } while (cursor.moveToNext())
        }
        return taskAmountList
    }

    fun addTaskCategoryAmount(ovModel : Overview_model) : Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(KEY_ID, ovModel.id)
        contentValues.put(KEY_IMAGE, ovModel.image)
        contentValues.put(KEY_CATEGORY, ovModel.category)
        contentValues.put(KEY_TASK_AMOUNT, ovModel.taskAmount)

        val success = db.insert(TABLE_NAME, null, contentValues)

        db.close()

        return success


    }

    fun updateTaskCategoryAmount(ovModel : Overview_model) : Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(KEY_ID, ovModel.id)
        contentValues.put(KEY_IMAGE, ovModel.image)
        contentValues.put(KEY_CATEGORY, ovModel.category)
        contentValues.put(KEY_TASK_AMOUNT, ovModel.taskAmount)

        val success = db.update(TABLE_NAME, contentValues,
            KEY_ID + "=" + ovModel.id, null  )

        db.close()

        return success

    }

    /**
     * Function to delete record
     */
    fun deleteCategory(ovModel: Overview_model): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, ovModel.id) // EmpModelClass id
        // Deleting Row
        val success = db.delete(TABLE_NAME, KEY_ID + "=" + ovModel.id, null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }

}