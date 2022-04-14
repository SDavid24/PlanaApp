package com.example.plana.Activities

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plana.Adapters.DetailAdapter
import com.example.plana.R
import com.example.plana.RoomDetail.DetailApp
import com.example.plana.RoomDetail.DetailDao
import com.example.plana.RoomDetail.DetailEntity
import com.example.plana.RoomDetail.TaskList
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.add_task_dialog.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    private val rv_detailing = mutableListOf<TaskList>()
    var detailActivityModel: DetailEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val detailDao = (application as DetailApp).db.detailDao()

        if (intent.hasExtra(OverviewActivity.EXTRA_TASK_DETAILS)) {

            detailActivityModel = intent.getSerializableExtra(
                OverviewActivity.EXTRA_TASK_DETAILS
            ) as DetailEntity

            supportActionBar?.elevation = 0F    //code to remove shadow beneath the action bar

            supportActionBar?.setHomeButtonEnabled(true)

            /** to call the set and customize the action bar**/
            setSupportActionBar(toolbar_detail_task)
            val actionbar = supportActionBar
            if (actionbar != null) {
                actionbar.setDisplayHomeAsUpEnabled(true)
            }

            /** click listener for the back button on the toolbar*/
            toolbar_detail_task.setNavigationOnClickListener {
                onBackPressed()
            }

            //Sets the page image to the category's image that's clicked on
            detail_page_image.setImageResource(detailActivityModel!!.image!!)

            //Sets the page header to the name of the category's that's clicked on
            detail_page_header.text = detailActivityModel!!.category

            //Brings in the ID of the category into this activity which can
            //be used for further purposes
            objectID.text = detailActivityModel!!.id.toString()

        }

        val trueValue = objectID.text.toString()
        Log.i("New Id", trueValue) //Displays the ID of the category that's clicked on in the log

        actionBar?.setDisplayShowTitleEnabled(false)   //code to remove title rom the action bar

        rv_detail.setHasFixedSize(true)

        /*Event clickListener for the Fab button. It opens up the Add Task dialog*/
        fabAddTask.setOnClickListener {
            addCategoryDialog(activityObjectID(), detailDao)
        }

        /**Coroutine that calculates the amount of data inserted
         * into a category and displays it immediately */
        lifecycleScope.launch{
            detailDao.fetchTaskCategoryById(activityObjectID()).collect {
                //Initializing the taskList to the  original taskList of the chosen category
                val taskList = it.taskList
                taskCount(taskList)  //applying the taskCount function
            }
        }

        //calling the setup recycler view function
        setupListOfDataIntoRecyclerView(detailActivityModel?.taskList!! as ArrayList<TaskList>, detailDao)


    }

    /**
     * Function is used show the list of inserted data.
     */
    private fun setupListOfDataIntoRecyclerView(
        list : ArrayList<TaskList>,
        detailDao: DetailDao

    ) {

        val newTaskList = list

        if (newTaskList.isNotEmpty()) {

            Log.i("Checker", "This line makes sense")
            rv_detail.visibility = View.VISIBLE
            blankPageText.visibility = View.GONE

            // Set the LayoutManager that this RecyclerView will use.
            rv_detail.layoutManager = LinearLayoutManager(this)

            // Adapter class is initialized and list is passed in the param.
            val detailAdapter = DetailAdapter(this, detailDao)
            // adapter instance is set to the recyclerview to inflate the items.
            rv_detail.adapter = detailAdapter
            detailAdapter.setListData(newTaskList)

            taskText.text = "${detailAdapter.items.count()} Tasks"

            Log.i("Lists of tasks for ${detailActivityModel!!.category}", newTaskList.toString())

        } else {
            Log.i("Checker", "This line DOESN'T make sense")

            rv_detail.visibility = View.GONE
            blankPageText.visibility = View.VISIBLE
            Log.i("Guess what?", "Task is empty!")

        }
    }

    private fun addCategoryDialog(id: Int, detailDao : DetailDao) {
        val addDialog = Dialog(this, R.style.Theme_Dialog)
        addDialog.setContentView(R.layout.add_task_dialog)
        addDialog.show()
        addDialog.setCancelable(false)  //to prevent dismissing the dialog when outside of it clicked


        /**Event listener for Add Task button*/
        addDialog.tvAddTASK.setOnClickListener {

            val task = addDialog.etAddTaskName.text.toString()
            val taskList = mutableListOf<TaskList>()

            if (task.isEmpty()) {
                Toast.makeText(applicationContext,
                    "Task cannot be blank", Toast.LENGTH_SHORT).show()
            }else {
                taskList.add(TaskList(task))
                //Adding the entry to the task list of the category in question
                detailActivityModel?.taskList?.add(TaskList(task))

                //Using coroutine to update the entry in the database
                lifecycleScope.launch {
                    detailDao.update(detailActivityModel!!)
                   // taskAmountUpdate(detailDao)  //updating the task amount in the database
                }

                Toast.makeText(applicationContext, "Task added", Toast.LENGTH_SHORT).show()
                addDialog.etAddTaskName.text.clear()  //Clear the textEdit space after adding entry

                //Update recycler view immediately after an entry
                setupListOfDataIntoRecyclerView(detailActivityModel!!.taskList as ArrayList<TaskList>, detailDao)

                addDialog.dismiss()  //dismiss the dialog

            }

        }

        /**Event listener for Cancel button*/
        addDialog.tvCancel.setOnClickListener {
            addDialog.dismiss() //dismiss the dialog
        }

    }

    /**Method to display the exact amount of tasks*/
    private fun taskCount(taskList: MutableList<TaskList>) : Int{
        //Initializing count to count function which COUNTS the number of tasks entry in a category
        val count : Int = taskList.count()
        taskNumber.text  = count.toString()
        //detailActivityModel!!.taskAmount = taskNumber.text.toString().toInt() + 1

        detailActivityModel!!.taskAmount = taskList.count() + 1 //equating taskAmount from the entity to the the taskList count

        //Conditional to display the correct word(task) regarding the amount of tasks
        if(count == 0 || count == 1) {
            taskText.text = getString(R.string.singular_task)

        }else{
            taskText.text = getString(R.string.plural_tasks)
        }

        return count
    }

    fun activityObjectID(): Int {
        return objectID.text.toString().toInt()
    }

    /**Method to Delete the details in a  using an Alert Dialog*/
    fun deleteRecordDialog(id:Int, detailDao: DetailDao) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setTitle("Delete Record")

        builder.setIcon(android.R.drawable.ic_dialog_alert)

        /**This decides what should happen when we click on the "Yes" button*/
        builder.setPositiveButton("Yes") { dialogInterface, _ ->

            lifecycleScope.launch {
                detailActivityModel!!.taskList.removeAt(id)
                detailDao.update(detailActivityModel!!)
            }

            Toast.makeText(
                applicationContext,
                "Record deleted successfully", Toast.LENGTH_LONG
            ).show()
            dialogInterface.dismiss()
        }

        /**This decides what should happen when we click on the "No" button*/
        builder.setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        builder.show()

    }

    /**Method to update the task amount in the database*/
/*
    fun taskAmountUpdate(detailDao: DetailDao){
        //detailActivityModel!!.taskAmount = taskList.size


        lifecycleScope.launch{
            Log.e("Name of Thread:","${Thread.currentThread().name}")
            detailDao.fetchTaskCategoryById(detailActivityModel!!.id).collect{
                //Initializing the taskList to the  original taskList of the chosen category
                val taskList = it.taskList
                it.taskAmount = taskList.size

                // overviewTaskCount(taskList)  //applying the taskCount function
            }
        }


    }
*/

}