package com.example.to_do

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

class Second_Adapter(private val mList: List<SecondData>) : RecyclerView.Adapter<Second_Adapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(task_id: String,name:String)
    }
    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }



    private lateinit var mListenerchechbox: onItemClickListenercheckbox

    interface onItemClickListenercheckbox {
        fun onItemClickcheckbox(task_id: String,checkBox:CheckBox,name:String,textView: TextView)
    }
    fun setOnItemClickListenercheckbox(listener3: onItemClickListenercheckbox) {
        mListenerchechbox = listener3
    }


    private lateinit var mListeneredit: onItemClickListeneredit

    interface onItemClickListeneredit {
        fun onItemClickedit(name:String,task_id: String)

    }
    fun setOnItemClickListeneredit(listener2: onItemClickListeneredit) {
        mListeneredit = listener2
    }

    class ViewHolder(itemView: View, listener: onItemClickListener, listener2: onItemClickListeneredit, listener3:onItemClickListenercheckbox) : RecyclerView.ViewHolder(itemView){
        var textView = itemView.findViewById<TextView>(R.id.your_task)
        var deletebutton=itemView.findViewById<ImageView>(R.id.second_screen_delete_textview)
        var task_id=itemView.findViewById<TextView>(R.id.testing_id)
        var editText=itemView.findViewById<ImageView>(R.id.edit_task)
        val checkbox=itemView.findViewById<CheckBox>(R.id.to_do_checkbox)
        init {
            checkbox.setOnCheckedChangeListener { compoundButton, b ->
                val text:String=textView.text.toString()
                listener3.onItemClickcheckbox(task_id.text.toString(),checkbox,textView.text.toString(),textView)

            }
        }
      init {
            editText.setOnClickListener {
              listener2.onItemClickedit(textView.text.toString(),task_id.text.toString())
            }
        }

        init {
            deletebutton.setOnClickListener {
                listener.onItemClick(task_id.text.toString(),textView.text.toString())
            }
        }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.secondscreen_list_layout, parent, false)
        return ViewHolder(view,mListener,mListeneredit,mListenerchechbox)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = mList[position]
        holder.textView.text = article.second_screen_name
        holder.task_id.text = article.id.toString()

        // Use for Check box
        divaker()
        val checkboxdatabase = Room.databaseBuilder(holder.itemView.context,CheckedboxDatabase::class.java,"Checkedbox_tabl")
            .allowMainThreadQueries()
            .build()
        val size=checkboxdatabase.CheckedboxDao().getdatetimedata().size
        if(size!=0) {
              for (i in 0..size-1 ) {
            val data = checkboxdatabase.CheckedboxDao().getdatetimedata().get(i).name
            if (holder.task_id.text == "$data"){
                holder.checkbox.isChecked = true}
        }
        }
    }

    private fun divaker() {

    }

    override fun getItemCount(): Int {
        return mList.size
    }
}























/*
 /*  val checkboxdatabase = Room.databaseBuilder(holder.itemView.context,CheckedboxDatabase::class.java, "Checkedbox_tabl")
            .allowMainThreadQueries()
            .build()
       val data4= checkboxdatabase.CheckedboxDao().getdatetimedata()
        //holder.textView.text=data4.get(position).name.toString()
        val i:Int=0
        for (i in 1 ..mList.size){
        if (data4.get(position).name==holder.task_id.text) {
            holder.checkbox.isChecked = true
        }
        }*/
        val checkboxdatabase = Room.databaseBuilder(holder.itemView.context, CheckedboxDatabase::class.java, "Checkedbox_tabl")
            .allowMainThreadQueries()
            .build()
        val data1=checkboxdatabase.CheckedboxDao().getdatetimedata().size
        val data4=checkboxdatabase.CheckedboxDao().getdatetimedata()

       // holder.textView.text=data4.get(position).name.toString()

        for (i in 1..data1) {

        val ll=data4.get(i).name.toString()

         //   val data2=checkboxdatabase.CheckedboxDao().getdatetimedata().get(i).name
            if (holder.task_id.text == "${ll}" ) {
                holder.checkbox.isChecked = true
            }
        }
 */