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
import com.example.plana.RoomDetail.DetailDao
import com.example.plana.RoomDetail.DetailEntity
import kotlinx.android.synthetic.main.item_rv_overview.view.*
import kotlin.collections.ArrayList


class OverviewAdapter(
    val context: Context,
    val list: ArrayList<DetailEntity>,
    //private val deleteListener : (id:Int)-> Unit

) : RecyclerView.Adapter <OverviewAdapter.OverviewViewHolder>(){
    private var onClickListener: OnClickListener? = null
    var items  = ArrayList<DetailEntity>()


    private var detailDao : DetailDao? = null
    private var detailEntity : DetailEntity? = null
    val overviewActivity = OverviewActivity()
    //val deleteCategory = overviewActivity.deleteCategory()

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

/*
                        if(context is OverviewActivity){
                            context.deleteRecordDialog(int, detailDao!!)
                        }*/
/*
                        if(context is OverviewActivity){
                            context.deleteCategory(positioning, detailDao!!)
                        }*/

                        //al deleteCategory = overviewActivity.deleteCategory()
                            /*lifecycleScope.launch{
                                detailDao.delete(DetailEntity(id))

                                //employeeDao.delete((id))

                            }*/

                        //var id = deleteListener.invoke(model.id).toString().toInt()
                        //overviewActivity.deleteCategory(id, detailDao!!)
                        Toast.makeText(context , "Delete clicked" , Toast.LENGTH_SHORT).show()

                        // here are the logic to delete an item from the list

                        /*val tempLang = exampleList[position]
                          exampleList.remove(tempLang)
                          adapter.notifyDataSetChanged()*/

                         //true
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


            //Finally, add an onclickListener to the item
            holder.itemView.setOnClickListener{

                if(onClickListener != null) {
                    onClickListener!!.onClick(position, model)

                }


/*
                if(context is OverviewActivity){
                    context.deleteRecordDialog(position, detailDao!!)
                    //notifyItemRemoved(position)

                    notifyDataSetChanged()

                }
*/

            }

        }

        fun popupMenus(view: View?) {

            //val position = list[adapterPosition]
            val popupMenus = PopupMenu(context, view)
            popupMenus.inflate(R.menu.options_menu)
            popupMenus.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.delete -> {

                        //deleteListener.invoke(model.id)
                        Toast.makeText(context , "Delete clicked" , Toast.LENGTH_SHORT).show()

                        // here are the logic to delete an item from the list
                        /*  val tempLang = exampleList[position]
                          exampleList.remove(tempLang)
                          adapter.notifyDataSetChanged()*/

                        true
                    }
                    // in the same way you can implement others
                    R.id.item_2 -> {
                        Toast.makeText(context , "Item 2 clicked" , Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.item_3 -> {
                        Toast.makeText(context,
                            "item clicked", Toast.LENGTH_SHORT).show()
                        true
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

    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * A function to bind the onclickListener.
     */
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

/*
    fun notifyEditItem(activity: Activity, adapterPosition: Int, addTodoListRequestCode: Int) {
        val intent = Intent(context, DetailActivity::class.java )
        intent.putExtra(OverviewActivity.EXTRA_TASK_DETAILS, list[adapterPosition])
        activity.startActivityForResult(intent,addTodoListRequestCode)

        notifyItemChanged(adapterPosition)
    }

    fun removeAt(adapterPosition: Int) {

        val dbHandler = OverviewDatabase(context)
        val isDeleted = dbHandler.deleteTask(list[adapterPosition])
        if (isDeleted > 1){
            list.removeAt(adapterPosition)
            notifyItemRemoved(adapterPosition)
        }

    }*/


    /**Create an interface for onclickListener*/
    interface OnClickListener{
        fun onClick(position: Int, model: DetailEntity)
    }
}

