package com.example.plana.Activities

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.iterator
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plana.Adapters.OverviewAdapter
import com.example.plana.R
import com.example.plana.RoomDetail.DetailApp
import com.example.plana.RoomDetail.DetailDao
import com.example.plana.RoomDetail.DetailEntity
import com.example.plana.RoomDetail.TaskList
import com.example.plana.databinding.ActivityOverviewBinding
import com.example.plana.databinding.AddCategoryDialogBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_drawer.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_overview.*
import kotlinx.android.synthetic.main.add_category_dialog.*
import kotlinx.android.synthetic.main.item_rv_overview.*
import kotlinx.android.synthetic.main.nav_activity_main.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class OverviewActivity : AppCompatActivity(){

    // view binding for the activity
    private lateinit var binding: ActivityOverviewBinding

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(binding.root)
        setContentView(R.layout.nav_activity_main)

        setSupportActionBar(overview_toolbar)
        val detailDao = (application as DetailApp).db.detailDao()
        val detailActivityModel: DetailEntity? = null


        /** to call the set and customize the action bar**/
        setSupportActionBar(overview_toolbar)
        val actionbar = supportActionBar

        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
            // actionbar.title = "Add Task"
        }

        for(i in rv_overview){

            lifecycleScope.launch{
                Log.e("Name of Thread:","${Thread.currentThread().name}")
                detailDao.fetchTaskCategoryById(detailActivityModel!!.id).collect{
                    //Initializing the taskList to the  original taskList of the chosen category
                    val taskList = it.taskList
                    overviewTaskCount(taskList)  //applying the taskCount function
                }
            }
        }

        supportActionBar?.elevation = 0F //To remove the shadow beneath the activity toolbar

        rv_overview.setHasFixedSize(true)

        /**Functionality to configure the drawer layout and Navigation view*/
        val drawerLayout : DrawerLayout = findViewById(R.id.nav_drawer_layout)
        //val drawerLayout : DrawerLayout = binding.R.id.nav_drawer_layout
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        /**OnclickListener for the hamburger menu*/
        navView.setNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.nav_home -> {
                    onBackPressed()
                    Toast.makeText(
                        applicationContext,
                        "Clicked Home", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_settings -> Toast.makeText(
                    applicationContext,
                    "Clicked Settings", Toast.LENGTH_SHORT).show()
                R.id.nav_login -> Toast.makeText(
                    applicationContext,
                    "Clicked Login", Toast.LENGTH_SHORT).show()
                R.id.nav_share -> Toast.makeText(
                    applicationContext,
                    "Clicked Share", Toast.LENGTH_SHORT).show()
                R.id.nav_rate -> Toast.makeText(
                    applicationContext,
                    "Clicked Rate", Toast.LENGTH_SHORT).show()
            }

            true
        }


        /**Coroutine that helps the room database setup the data into the recyclerview */
        lifecycleScope.launch{
            detailDao.fetchAllTaskCategory().collect {
                val list = ArrayList(it)

                generateRecyclerview(list, detailDao)
            }
        }

        //Click listener for Floating action Button which adds the Category
        fabAddCategory.setOnClickListener {
            addCategoryDialog(detailDao)
        }
    }

    /**Method to display the exact amount of tasks*/
    private fun overviewTaskCount(taskList: MutableList<TaskList>) : Int{
        //Initializing count to count function which COUNTS the number of tasks entry in a category
        val count : Int = taskList.count()
        ovTaskNumber.text = count.toString()

        //Conditional to display the correct word(task) regarding the amount of tasks
        if(count == 0 || count == 1) {
            taskText.text = getString(R.string.singular_task)

        }else{
            taskText.text = getString(R.string.plural_tasks)
        }

        return count
    }

    /** Method to set up the recyclerViewList on the screen*/
    private fun generateRecyclerview(
        overviewList: ArrayList<DetailEntity>, detailDao: DetailDao
    ) : ArrayList<DetailEntity>
    {

        rv_overview.layoutManager = LinearLayoutManager(applicationContext)
        rv_overview.setHasFixedSize(true)

        val overviewAdapter = OverviewAdapter(applicationContext, overviewList
        )
        //Calling the delete record dialog function here
        {
            deleteId ->
            deleteRecordDialog(deleteId, detailDao)
        }

        rv_overview.adapter = overviewAdapter

        /**method to ensure every row in the recyclerview that's clicked
         * links to the detail page*/
        overviewAdapter.setOnClickListener(object : OverviewAdapter.OnClickListener{
            override fun onClick(position: Int, model: DetailEntity) {

                //links to the detail page
/*
                val intent = Intent(this@OverviewActivity,
                    DetailActivity::class.java)

                intent.putExtra(EXTRA_TASK_DETAILS, model )
                startActivity(intent)*/

            }
        })

        return overviewList
    }

    /**Method to insert the details in a row using a Dialog picker*/
    fun addCategoryDialog(detailDao: DetailDao) {
        val binding = AddCategoryDialogBinding.inflate(layoutInflater)
        val addDialog = Dialog(this, R.style.Theme_Dialog)
        //addDialog.setContentView(R.layout.add_task_dialog)
        addDialog.setContentView(binding.root)
        addDialog.show()
        addDialog.setCancelable(false)

        //Initialising the category array in the strings file
        val category = resources.getStringArray(R.array.Category)
        //Joining the array so it can come out in the dropdown_item format(Like recyclerView stuff)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, category)

        //Joining the autoCompleteTextView which is the dropdown edit text with the adapter
        binding.autoCompleteTextView.setAdapter(arrayAdapter) //

        /**Determining what should happen if the customer clicks on yes after picking a category which in this case is to Add a category to the Database and also display in the RecyclerView
         */
        addDialog.tvAddCategory.setOnClickListener {
            val initTaskList =
                mutableListOf<TaskList>() //Initializing the taskList to a mutable empty list

            /**A conditional that firstly checks the Category column in the Room Database
             *  if a chosen category is already present before it either adds it to
             *  the database or rejects it.*/
            when {
                //For the Today category
                addDialog.autoCompleteTextView.text.toString() == "Today" -> {
                    lifecycleScope.launch(IO) {
                        //Initialising the exists() from the DetailDao
                        val exists = detailDao.exists(addDialog.autoCompleteTextView.text.toString())

                        //Check if the category(or string) already exists
                        if(exists){
                            runOnUiThread {
                                Toast.makeText(this@OverviewActivity,
                                    " This Category already exists",
                                    Toast.LENGTH_LONG).show()
                            }

                        }else {

                            //Inserts the category into the Database
                            detailDao.insert(
                                DetailEntity(
                                    image = R.drawable.yellowsun, category = "Today",
                                    taskAmount = 0,
                                    taskList = initTaskList
                                )
                            )
                            addDialog.dismiss()
                        }
                    }
                }

                //For the Personal category
                addDialog.autoCompleteTextView.text.toString() == "Personal" -> {
                    lifecycleScope.launch(IO) {
                        //Initialising the exists() from the DetailDao
                        val exists = detailDao.exists(addDialog.autoCompleteTextView.text.toString())

                        //Check if the category(or string) already exists
                        if(exists){
                            runOnUiThread {
                                Toast.makeText(this@OverviewActivity,
                                    " This Category already exists",
                                    Toast.LENGTH_LONG).show()
                            }

                        }else {
                            //Inserts the category into the Database
                            detailDao.insert(
                                DetailEntity(
                                    image = R.drawable.blackboycopy, category = "Personal",
                                    taskAmount = 0,
                                    taskList = initTaskList
                                )
                            )
                            addDialog.dismiss()
                        }
                    }

                }

                //For the Planned category
                addDialog.autoCompleteTextView.text.toString() == "Planned" -> {
                    lifecycleScope.launch(IO) {
                        //Initialising the exists() from the DetailDao
                        val exists = detailDao.exists(addDialog.autoCompleteTextView.text.toString())

                        //Check if the category(or string) already exists
                        if(exists){
                            runOnUiThread {
                                Toast.makeText(this@OverviewActivity,
                                    " This Category already exists",
                                    Toast.LENGTH_LONG).show()
                            }
                        }else {
                            //Inserts the category into the Database
                            detailDao.insert(
                                DetailEntity(
                                    image = R.drawable.bluecalendarcopy, category = "Planned",
                                    taskAmount = 0,
                                    taskList = initTaskList
                                )
                            )
                            addDialog.dismiss()

                        }
                    }
                }

                //For the Work category
                addDialog.autoCompleteTextView.text.toString() == "Work" -> {

                    lifecycleScope.launch(IO) {
                        //Initialising the exists() from the DetailDao

                        val exists = detailDao.exists(addDialog.autoCompleteTextView.text.toString())

                        //Check if the category(or string) already exists
                        if(exists){
                            runOnUiThread {
                                Toast.makeText(this@OverviewActivity,
                                    " This Category already exists",
                                    Toast.LENGTH_LONG).show()
                            }

                        }else {
                            //Inserts the category into the Database
                            detailDao.insert(
                                DetailEntity(
                                    image = R.drawable.newsuitcasecopy3, category = "Work",
                                    taskAmount = 0,
                                    taskList = initTaskList
                                )
                            )
                            addDialog.dismiss()
                        }
                    }
                }

                //For the Shopping category
                addDialog.autoCompleteTextView.text.toString() == "Shopping" -> {

                    lifecycleScope.launch(IO) {

                        //Initialising the exists() from the DetailDao
                        val exists = detailDao.exists(addDialog.autoCompleteTextView.text.toString())

                        //Check if the category(or string) already exists
                        if(exists){
                            runOnUiThread {
                                Toast.makeText(this@OverviewActivity,
                                    " This Category already exists",
                                    Toast.LENGTH_LONG).show()
                            }

                        }else {
                            //Inserts the category into the Database
                            detailDao.insert(
                                DetailEntity(
                                    image = R.drawable.blueshopcartcopy, category = "Shopping",
                                    taskAmount = 0,
                                    taskList = initTaskList
                                )
                            )
                            addDialog.dismiss()
                        }
                    }
                }

                //if a null option is by any means picked
                else -> {
                    Toast.makeText(this,
                        "No valid category picked",
                        Toast.LENGTH_LONG).show()
                }
            }
        }

        /**What happens when you click on the Negative button*/
        addDialog.tvCancel.setOnClickListener {
                addDialog.dismiss()

            }

    }


    /**Needs attention!!!
     * Delete dialog to display when an item is clicked on in the recycler view
     * And also tries to carry out the delete whole  category function*/
    fun deleteRecordDialog( id:Int, detailDao: DetailDao) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this, R.style.AlertDialogTheme)
        builder.setCancelable(false)
        builder.setTitle("Delete Record")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        /**This decides what should happen when we click on the "Yes" button*/
        builder.setPositiveButton("Yes") { dialogInterface, _ ->

            lifecycleScope.launch {
                detailDao.delete(DetailEntity(id)) //The main that is in charge of deleting the category
                Toast.makeText(
                    applicationContext,
                    "Record deleted successfully", Toast.LENGTH_LONG
                ).show()
            }
            dialogInterface.dismiss()
        }

        /**This decides what should happen when we click on the "No" button*/
        builder.setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        builder.show()
    }


    /**method to make the hamburger button responsive when clicked*/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**Method to prevent closing of the activity if the drawer is open and back is pressed*/
    override fun onBackPressed() {
        if (nav_drawer_layout.isDrawerOpen(GravityCompat.START)){
            nav_drawer_layout.closeDrawer(GravityCompat.START)
        } else{
            super.onBackPressed()
        }
    }

    /** It is called when the activity which launched with the request code and
    //  expecting a result from the launched activity.)
    // Call Back method  to get the Message from other Activity*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DETAIL_ACTIVITY_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
              //  generateDummyList(5, overviewDao)
            }else{
                Log.e("Activity", "Cancelled or back pressed" )
            }
        }
    }

    companion object {
        val DETAIL_ACTIVITY_REQUEST_CODE = 1
        val ADD_ACTIVITY_REQUEST_CODE = 2
        val EXTRA_TASK_DETAILS = "extra task details"
    }

}
