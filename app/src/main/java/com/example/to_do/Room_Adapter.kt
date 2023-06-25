package com.example.to_do
import android.app.Dialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.annotations.NotNull


class Room_Adapter(private val mList: List<Contact>) : RecyclerView.Adapter<Room_Adapter.ViewHolder>() {

   lateinit var  mListener :onItemClickListener

    interface onItemClickListener {
        fun onItemClick(task_id: String,name:String)
    }
    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }


    private lateinit var mListeneredittask: onItemClickListeneredittask

    interface onItemClickListeneredittask {
        fun onItemClickedittask(id:String,name:String)
    }
    fun setOnItemClickedittask(listener3: onItemClickListeneredittask) {
        mListeneredittask = listener3
    }

    class ViewHolder(  itemView: View,listener: onItemClickListener,listener3:onItemClickListeneredittask) : RecyclerView.ViewHolder(itemView){
        var textView = itemView.findViewById<TextView>(R.id.your_task)
        var deletebutton=itemView.findViewById<ImageView>(R.id.Delete_button)
        var id_show=itemView.findViewById<TextView>(R.id.id_text_view)
        var edittask=itemView.findViewById<ImageView>(R.id.edit_first_screen_task)

        init {
            edittask.setOnClickListener {
                listener3.onItemClickedittask(id_show.text.toString(),textView.text.toString())
            }
        }
        init {
            deletebutton.setOnClickListener {
                listener.onItemClick(id_show.text.toString(),textView.text.toString())

            }
        }
        init {
            itemView.setOnClickListener {
                val name:String
                name=textView.text.toString()
                val id_showw:String
                id_showw=id_show.text.toString()
                val intent=Intent(itemView.context,NextActivity::class.java)
                intent.putExtra("task_name",name.toString())
                intent.putExtra("task_id_background",id_showw.toString())
                //passing adapter position in second activity class
                intent.putExtra("adapter_position",adapterPosition.toString())
                itemView.context.startActivity(intent)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        val view:View = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_list_layout, parent, false)
        return ViewHolder(view,mListener,mListeneredittask)

    }
    override fun onBindViewHolder( holder: ViewHolder, position: Int) {
        val article=mList[position]
        holder.textView.text=article.name
        holder.id_show.text= article.id.toString()

    }
    override fun getItemCount(): Int {
     return mList.size
    }
}



