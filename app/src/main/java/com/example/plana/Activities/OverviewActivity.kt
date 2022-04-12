package com.example.plana.Activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
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
import com.example.plana.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_drawer.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_overview.*
import kotlinx.android.synthetic.main.item_rv_overview.*
import kotlinx.android.synthetic.main.nav_activity_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class OverviewActivity : AppCompatActivity(){

    // view binding for the activity
    private var _binding: ActivityMainBinding? = null

    lateinit var toggle: ActionBarDrawerToggle
    private val detailActivityModel: DetailEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ///_binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.nav_activity_main)

        setSupportActionBar(overview_toolbar)
        val detailDao = (application as DetailApp).db.detailDao()
        //val detailActivityModel: DetailEntity? = null

        //    var categoryID = detailActivityModel!!.id
       // val overviewDao = (application as OverviewApp).db.overviewDao()

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

       // categoryID = ovCategoryID()

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.elevation = 0F //To remove the shadow beneath the activity toolbar

        rv_overview.setHasFixedSize(true)

        /**Functionality to configure the drawer layout and Navigation view*/

        val drawerLayout : DrawerLayout = findViewById(R.id.nav_drawer_layout)
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

        // call addRecord with detailDao
        addCategory(5, detailDao)

      //  var categoryIndex = detailActivity.


        /*lifecycleScope.launch{
            detailDao.fetchTaskCategoryById(ovCategoryID()).collect{
                //Initializing the taskList to the  original taskList of the chosen category
                val taskList = it.taskList
                overviewTaskCount(taskList)  //applying the taskCount function
            }
        }*/


        /**Coroutine that helps the room database setup the data into the recyclerview */
        lifecycleScope.launch{
            detailDao.fetchAllTaskCategory().collect {

               // taskHeaderAmount.text =
                val list = ArrayList(it)

             //   val taskList = list.
             //   taskCount(taskList)

                generateDummyList(list, detailDao)
            }
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
    private fun generateDummyList(
        overviewList: ArrayList<DetailEntity>, detailDao: DetailDao
    ) : ArrayList<DetailEntity>
    {

        rv_overview.layoutManager = LinearLayoutManager(applicationContext)
        rv_overview.setHasFixedSize(true)

        val overviewAdapter = OverviewAdapter(applicationContext, overviewList
        )
         /*{ deleteId ->
            deleteCategory(deleteId, detailDao!!)
        }*/
        rv_overview.adapter = overviewAdapter

       // ovTaskNumber.text = (detailActivityModel!!.taskList.size).toString()
      //  ovTaskNumber.text = (detailActivityModel!!.taskList.size).toString()

        /**method to ensure every row in the recyclerview that's clicked
         * links to the detail page*/
        overviewAdapter.setOnClickListener(object : OverviewAdapter.OnClickListener{
            override fun onClick(position: Int, model: DetailEntity) {
                val intent = Intent(this@OverviewActivity,
                    DetailActivity::class.java)

                intent.putExtra(EXTRA_TASK_DETAILS, model )
                startActivity(intent)
            }

        })


        Log.i("Recyclerview:", "GenerateDummyList Launched")

        rv_overview.setOnClickListener {
            val intent = Intent(this@OverviewActivity,
                DetailActivity::class.java)

            startActivity(intent)
        }

/*
        // This binds the edit feature class to recyclerview
        val editSwipeHandler = object : SwipeToEditCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // This below Calls the adapter function when it is swiped for edit
                val adapter = rv_detail.adapter as OverviewAdapter
                adapter.notifyEditItem(this@OverviewActivity,
                    viewHolder.adapterPosition, ADD_ACTIVITY_REQUEST_CODE)
            }
        }
        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(rv_overview)

        // This binds the delete feature class to recyclerview)
        val deleteSwipeHandler = object  : SwipeToDeleteCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // This below Calls the adapter function when it is swiped for delete
                val adapter = rv_overview.adapter as OverviewAdapter
                adapter.removeAt(viewHolder.adapterPosition)

                getTodoList() // Gets the latest list from the local database after item being delete from it.
            }
        }
        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(rv_overview)*/

        return overviewList
    }
/*
    fun ovCategoryID(): Int {
        val mIndex =
    }*/


    /**Method to insert the details in a row using*/
     fun addCategory(size: Int, detailDao: DetailDao) {

        for (i in 0 until size) {

            val image = when (i % 6) {
                0 -> R.drawable.yellowsun
                1 -> R.drawable.bluecalendarcopy
                2 -> R.drawable.blackboycopy
                3 -> R.drawable.newsuitcasecopy3
                else -> R.drawable.blueshopcartcopy
            }

            val category = when (i % 6) {
                0 -> "Today"
                1 -> "Planned"
                2 -> "Personal"
                3 -> "Work"
                else -> "Shopping"
            }

           val taskAmountDB =
               //taskAmount().toString().toInt()


                when (i % 6) {
                    0 -> 0
                    1 -> 0
                    2 -> 0
                    3 -> 0
                    else -> 0
                }

            val initTaskList = mutableListOf<TaskList>() //Initializing the taskList to a mutable empty list

           /**Inserting into dataBase using coroutine*/
            lifecycleScope.launch {
                detailDao.insert(DetailEntity(
                    image = image, category = category,
                    taskAmount = taskAmountDB,
                    taskList = initTaskList
                ))

            }

        }

    }



    fun deleteCategory(id: Int, detailDao: DetailDao){
        //val id = ovTaskID.text.toString().toInt()
        val image = rv_overview_image
        val taskAmount = ovTaskNumber
        val initTaskList = mutableListOf<TaskList>().toString().toInt()
        lifecycleScope.launch{
            detailDao.delete(DetailEntity(id))

            detailActivityModel!!.taskAmount!!.minus(1)
            //employeeDao.delete((id))

        }

    }

/*
    private fun taskAmount() {
        lifecycleScope.launch{
            detailDao.fetchTaskCategoryById(detailActivityModel!!.id).collect{
                //Initializing the taskList to the original taskList of the chosen category
                val taskList = it.taskList
                overviewTaskCount(taskList)  //applying the taskCount function
            }
        }
    }
*/

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
