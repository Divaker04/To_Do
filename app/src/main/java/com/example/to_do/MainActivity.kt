package com.example.to_do

import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    var input: String = ""
    lateinit var save: ImageView
    lateinit var editText: EditText
    lateinit var recyclerVieww: RecyclerView
    lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var button2 = findViewById<FloatingActionButton>(R.id.task_button)
        val message = intent?.getStringExtra("task_name")
       firebasepushnotificationtoken()

        val database = Room.databaseBuilder(applicationContext,ContactDatabase::class.java, "contactDB")
                .allowMainThreadQueries()
                .build()
          showdata()

        val data1 = database.contactDao().getContact()
        val adapter = Room_Adapter(data1)
        recyclerVieww = findViewById(R.id.recycler_view)
        recyclerVieww.adapter=adapter


        adapter.setOnItemClickedittask(object :Room_Adapter.onItemClickListeneredittask{
            override fun onItemClickedittask(id_show:String,taskname:String) {
              //  Toast.makeText(this@MainActivity,id_show,Toast.LENGTH_SHORT).show()
                val dialog=Dialog(this@MainActivity)
                dialog.setContentView(R.layout.edit_task_dialog_screen)
                var ok_buttonn=dialog.findViewById<TextView>(R.id.edit_task_yes_button)
                var no_button=dialog.findViewById<TextView>(R.id.edit_task_no_button)
                dialog.show()
                no_button.setOnClickListener {
                    dialog.dismiss()
                }
                ok_buttonn.setOnClickListener {
                    dialog.dismiss()
                   val dialog2=Dialog(this@MainActivity)
                    dialog2.setContentView(R.layout.enter_updated_task_dialog)
                    val updatetask_name=dialog2.findViewById<EditText>(R.id.enter_updated_task)
                    val save_edit_task=dialog2.findViewById<ImageView>(R.id.edit_task_button_save)
                    val text_view_updated_task=dialog2.findViewById<TextView>(R.id.enter_updated_task_text_view)
                    text_view_updated_task.setText("Your task '$taskname"+"' will be permanently edited..")
                    dialog2.show()

                    updatetask_name.addTextChangedListener {
                        save_edit_task.setBackgroundResource(R.drawable.add_task_background22222)
                        if (updatetask_name.text.length > 0) {
                            save_edit_task.setBackgroundResource(R.drawable.add_second_screen_save)
                        }
                    }

                    save_edit_task.setOnClickListener {
                        val tasktext=updatetask_name.text.toString()
                        GlobalScope.launch {
                            database.contactDao().updatetable(tasktext,id_show)
                        }
                        dialog2.dismiss()
                        recreate()
                    }

                }
            }

        })

        adapter.setOnItemClickListener(object : Room_Adapter.onItemClickListener {
            override fun onItemClick(task_id: String,name:String) {
              //  Toast.makeText(this@MainActivity,position,Toast.LENGTH_LONG).show()
                val dialog=Dialog(this@MainActivity)
                dialog.setContentView(R.layout.alert_dialog)
                val settext=dialog.findViewById<TextView>(R.id.delete_set_text)
                //settext.text=name.toString()

                settext.setText("'$name"+"' will be permanently deleted.")
                val cancel=dialog.findViewById<TextView>(R.id.alert_cancel_button)
                val delete=dialog.findViewById<TextView>(R.id.alert_ok_button)
                dialog.show()
                cancel.setOnClickListener {
                    dialog.dismiss()
                }
                delete.setOnClickListener {
                    alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
                    val intent = Intent(this@MainActivity, MyReceiver::class.java)
                    val flags = when {
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                        else -> PendingIntent.FLAG_UPDATE_CURRENT
                    }
                    // pass message because i want to set alarm in diffrent diffrent task this message is id of task in database
                    if (task_id != null) {
                        pendingIntent = PendingIntent.getBroadcast(this@MainActivity, task_id.toInt(), intent, flags)
                    }
                    alarmManager.cancel(pendingIntent)
                   // Toast.makeText(this@MainActivity,"yyyy",Toast.LENGTH_SHORT).show()
                    GlobalScope.launch {
                        database.contactDao().deleteByUid(task_id.toLong())
                    }
                    dialog.dismiss()
                        recreate()
                }
                }
        })
        button2.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            dialog.setContentView(R.layout.add_task)

            dialog.getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            dialog.show()
            editText = dialog.findViewById(R.id.new_task)!!
            save = dialog.findViewById(R.id.save)!!

            editText.addTextChangedListener {
                save.setBackgroundResource(R.drawable.add_task_background22222)
                if(editText.text.length>0 && editText.text.isNotBlank()){
                    save.setBackgroundResource(R.drawable.add_second_screen_save)
                }
            }

            save.setOnClickListener {
                input = editText.text.toString()
                dialog.dismiss()
                if (input >= 1.toString()|| input.isNotEmpty()) {
                    GlobalScope.launch {
                        database.contactDao().inserContact(Contact(0, input))
                    }
                    recreate()
                }
            }
        }
    }

    private fun firebasepushnotificationtoken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TokenDetail", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            Log.d("TOKEN", token)
        })
    }

    private fun showdata() {
        val database = Room.databaseBuilder(applicationContext, ContactDatabase::class.java, "contactDB")
            .allowMainThreadQueries()
            .build()
        val data = database.contactDao().getContact()
        recyclerVieww = findViewById(R.id.recycler_view)
        val adapter = Room_Adapter(data)
        recyclerVieww.adapter = adapter
        recyclerVieww.layoutManager = LinearLayoutManager(this@MainActivity)
    }

}

