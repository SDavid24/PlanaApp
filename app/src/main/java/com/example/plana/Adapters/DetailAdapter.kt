package com.example.plana.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Dao
import com.example.plana.R
import com.example.plana.RoomDetail.DetailDao
import com.example.plana.RoomDetail.DetailDatabase
import com.example.plana.RoomDetail.DetailEntity
import com.example.plana.RoomDetail.TaskList
import kotlinx.android.synthetic.main.item_rv_detail.view.*
import kotlinx.android.synthetic.main.item_rv_overview.view.*
import java.nio.file.Paths.get

class DetailAdapter(

   // private val updateListener : (id:Int)-> Unit,
    private val deleteListener : (id:Int)-> Unit,

): RecyclerView.Adapter<DetailAdapter.DetailViewHolder>() {
   // val arrayTaskList = ArrayList<DetailEntity>()
    val arrayTaskList = ArrayList<TaskList>()

    var items  = mutableListOf<TaskList>()
    //val ivDelete = view.ivDelete

     /*fun onClick(v: View?) {
        val position = absoluteAdapterPosition
        if (position != RecyclerView.NO_POSITION) {
            listener.onItemClick(position)
        }
    }*/

    class DetailViewHolder(view: View ): RecyclerView.ViewHolder(view) {
        var taskList = view.tvTaskDetail

        var ivDelete = view.ivDelete
/*
        fun bind(data: TaskList) {
            taskList.text = data.tasks

        }*/

    }

    fun setListData(data: ArrayList<TaskList>) {
        this.items = data

        //This notifies  the UI that a change in the tasklists has occurred and updates it immediately
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_rv_detail, parent, false)
        return DetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailAdapter.DetailViewHolder, position: Int) {
        val model = items[position]

      //  val detailEntity : DetailEntity? = null

        if(holder is DetailViewHolder) {
            holder.itemView.detailTaskID.text = getItemId(position).toString()

            holder.itemView.tvTaskDetail.text =  model.tasks

            holder.ivDelete.setOnClickListener {
                //deleteListener.invoke(model.tasks[getItemId(position).toInt()].toInt())
               // deleteListener.invoke(model.tasks[position].toString().toInt())
               // deleteListener.invoke(model.toString().toInt())
            }
            //holder.bind(items[position])

        }

    }

    override fun getItemCount(): Int {
        return items.size
    }


/*
    fun removeAt(position: Int) {

        val dbHandler = DetailDatabase
        val isDeleted = DetailDatabase(items[position])

        if (isDeleted > 0) {
            exampleList.removeAt(position)
            notifyItemRemoved(position)
        }
    }
*/



}


