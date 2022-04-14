package com.example.plana.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.plana.Activities.DetailActivity
import com.example.plana.Activities.OverviewActivity
import com.example.plana.R
import com.example.plana.RoomDetail.DetailApp
import com.example.plana.RoomDetail.DetailDao
import com.example.plana.RoomDetail.DetailEntity
import kotlinx.android.synthetic.main.item_rv_overview.view.*
import kotlin.collections.ArrayList


class OverviewAdapter(
    val context: Context,
    val list: ArrayList<DetailEntity>,
    val deleteListener : (id:Int)-> Unit

) : RecyclerView.Adapter <OverviewAdapter.OverviewViewHolder>(){
    private var onClickListener: OnClickListener? = null

    inner class OverviewViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var id : TextView
        var category: TextView
        var description: TextView
        var image: ImageView
        var menu: TextView

        init {
            id = view.findViewById(R.id.ovTaskID)
            image = view.findViewById(R.id.rv_overview_image)

            category = view.findViewById(R.id.tvCategory)
            description = view.findViewById(R.id.ovTaskNumber)
            menu = view.findViewById(R.id.textViewOptions)

            menu.setOnClickListener {
                popupMenus(
                    it
                ) }
        }

        private fun popupMenus(view: View?) {

            val position = list[adapterPosition]
            val int = id.text.toString().toInt()
            //val model = list[positioning]
            val popupMenus = PopupMenu(context, view)
            popupMenus.inflate(R.menu.options_menu)
            popupMenus.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.delete -> {
                        Toast.makeText(context , "Delete clicked" , Toast.LENGTH_SHORT).show()
                    }
                    // in the same way you can implement others
                    R.id.item_2 -> {
                        Toast.makeText(context , "Item 2 clicked" , Toast.LENGTH_SHORT).show()
                         //true
                    }
                    R.id.item_3 -> {
                        Toast.makeText(context,
                            "item clicked", Toast.LENGTH_SHORT).show()
                         //true
                    }
                }
                 false
            }

            popupMenus.show()
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menu = popup.get(popupMenus)
            menu.javaClass.getDeclaredMethod("setForceShowIcon",Boolean::class.java)
                .invoke(menu,true)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OverviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_rv_overview, parent, false)
        return OverviewViewHolder(view)
    }


    override fun onBindViewHolder(holder: OverviewViewHolder, position: Int) {

        val model = list[position]

        if(holder is OverviewViewHolder){
            holder.itemView.ovTaskID.text = model.id.toString()
            holder.itemView.rv_overview_image.setImageResource(model.image!!).toString()

            holder.itemView.tvCategory.text = model.category

            holder.itemView.ovTaskNumber.text = model.taskList.size.toString()

            when {
                holder.itemView.ovTaskNumber.text.toString().toInt() == 0 -> {
                    holder.itemView.ovTaskText.text = "task"
                }
                holder.itemView.ovTaskNumber.text.toString().toInt() == 1 -> {
                    holder.itemView.ovTaskText.text = "task"
                }
                else -> {
                    holder.itemView.ovTaskText.text = "tasks"
                }

            }


            //Finally, add an onclickListener to the item to make it go to the Detail Activity
/*
            holder.itemView.setOnClickListener{

                if(onClickListener != null) {
                    onClickListener!!.onClick(position, model)

                }


*/


            //Click listener to bring out the delete dialog instead
            holder.itemView.setOnClickListener {
                deleteListener.invoke(model.id)
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * A function to bind the onclickListener.
     */
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }


    /**Create an interface for onclickListener*/
    interface OnClickListener{
        fun onClick(position: Int, model: DetailEntity)
    }
}

