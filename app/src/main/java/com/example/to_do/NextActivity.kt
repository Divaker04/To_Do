package com.example.to_do
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.TimePickerDialog
import android.app.TimePickerDialog.BUTTON_POSITIVE
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class NextActivity : AppCompatActivity() {
    var input: String = ""
    lateinit var textView: TextView
    lateinit var recyclerView: RecyclerView
    lateinit var enter_text: AutoCompleteTextView
    lateinit var second_screen_save: ImageView
    lateinit var back_screen:ImageView
    lateinit var secondAdapter: Second_Adapter
    lateinit var remind_text:TextView
    lateinit var picker:MaterialTimePicker
    lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    lateinit var time_show_Display:TextView
    lateinit var cancel_alarm:TextView

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next)
        back_screen=findViewById(R.id.back_home_screen)


        remind_text=findViewById(R.id.remind_me_text)
        val cal = Calendar.getInstance()
        var message2 = intent.getStringExtra("task_id_background")
        val task_name = intent.getStringExtra("task_name")
       // val editask=findViewById<ImageView>(R.id.edit_next_screen_task)

      /*  editask.setOnClickListener {
            editfirstscreentask_insecondscreen(task_name.toString())
        }*/


        createNotificationchannel()
        // Set Reminder in task
        val datetimedata=Room.databaseBuilder(applicationContext,Date_Time_Database::class.java,"datetimetablee")
            .allowMainThreadQueries()
            .build()

        val data1 =datetimedata.DateTime_Dao().getdatetimedata(message2!!)
         //  time_show_Display.setText(data1.toString())
        recyclerView = findViewById(R.id.date_time_recycler_view)
        val adapterr = DatetimeAdapter(data1)
        recyclerView.adapter = adapterr
        recyclerView.layoutManager = LinearLayoutManager(this@NextActivity)

        var date:String
        remind_text.setOnClickListener {
            GlobalScope.launch {
                datetimedata.DateTime_Dao().deleteByUiddatetime(message2.toLong())
            }
              val dialog=DatePickerDialog(this,R.style.TimePickerTheme)
            dialog.show()
            dialog.getDatePicker().setMinDate(System.currentTimeMillis())

            dialog.setOnDateSetListener { datePicker, i, i2, i3 ->

                cal.set(Calendar.DATE, i3)
                cal.set(Calendar.MONTH, i2)
                cal.set(Calendar.YEAR, i)
                val currenttime = Calendar.getInstance()
                val startHour = currenttime.get(Calendar.HOUR_OF_DAY)
                val startminute = currenttime.get(Calendar.MINUTE)
                val i2=i2+1
                date=i3.toString()+"/"+i2.toString()+"/"+i.toString()+"  "

                TimePickerDialog(this,R.style.TimePickerTheme, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        cal.set(Calendar.MINUTE, minute)
                        cal.set(Calendar.SECOND, 0)
                    date=(date+hourOfDay+":"+minute)
                    Toast.makeText(this, "Alarm set successfully", Toast.LENGTH_SHORT).show()
                    alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
                        val intent = Intent(this, MyReceiver::class.java)
                        intent.putExtra("message name",task_name)//pass data to receiver class and show this data to notification
                    intent.putExtra("message_id",message2)
                    //set data in text view for user see time and date
                    val flags = when {
                            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE
                            else -> FLAG_UPDATE_CURRENT
                        }

                    // pass message2 because i want to set alarm in diffrent diffrent task this message2 is id of task in database
                    if (message2 != null) {
                        pendingIntent = PendingIntent.getBroadcast(this@NextActivity, message2.toInt(), intent, flags)

                        GlobalScope.launch {
                            datetimedata.DateTime_Dao().insertdatetimeintable(Date_Time_Data(0,message2.toString(),date))
                        }
                        recreate()
                    }
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pendingIntent)
                    }, startHour, startminute, false).show()

            }
        }

        // move to back screen (Home Screen)
        back_screen.setOnClickListener {
            val intent=Intent(this@NextActivity,MainActivity::class.java)
            startActivity(intent)
        }

        val adapter_position=intent.getStringExtra("adapter_position")
        val database2 = Room.databaseBuilder(applicationContext, SecondDataBase::class.java, "SecondContactDB")
            .allowMainThreadQueries()
            .build()
        val data = database2.secondScreenDao().getdatasecondscreen(message2.toString())
        recyclerView = findViewById(R.id.second_screen_recycler_view)
        var adapter = Second_Adapter(data)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter.setOnItemClickListeneredit(object :Second_Adapter.onItemClickListeneredit{
            override fun onItemClickedit(name: String,taskk_id: String) {
               // Toast.makeText(this@NextActivity,"khus ho ja",Toast.LENGTH_SHORT).show()
                val dialog1=Dialog(this@NextActivity)
                dialog1.setContentView(R.layout.edit_task_dialog_screen)
                val edit_task_yes=dialog1.findViewById<TextView>(R.id.edit_task_yes_button)
                val edit_task_no=dialog1.findViewById<TextView>(R.id.edit_task_no_button)
                dialog1.show()
                edit_task_no.setOnClickListener {
                    dialog1.dismiss()
                }

                //Click on edit task
                edit_task_yes.setOnClickListener {
                    dialog1.dismiss()
                    val dialog=Dialog(this@NextActivity)
                    dialog.setContentView(R.layout.enter_updated_task_dialog)
                    val update_task_input=dialog.findViewById<EditText>(R.id.enter_updated_task)
                    val saveupdated_task=dialog.findViewById<ImageView>(R.id.edit_task_button_save)
                    val text_view_updated_task=dialog.findViewById<TextView>(R.id.enter_updated_task_text_view)
                    text_view_updated_task.setText("Your task '$name"+"' will be permanently edited..")
                    dialog.show()

                    update_task_input.addTextChangedListener {
                        saveupdated_task.setBackgroundResource(R.drawable.add_task_background22222)
                        if (update_task_input.text.length > 0) {
                         saveupdated_task.setBackgroundResource(R.drawable.add_second_screen_save)
                        }
                    }

                    saveupdated_task.setOnClickListener {

                        val updatedata: String = update_task_input.text.toString()
                        if (updatedata.length==0) {
                            Toast.makeText(this@NextActivity,"Please enter your updated task",Toast.LENGTH_SHORT).show()
                            }
                        else{
                            GlobalScope.launch {
                                database2.secondScreenDao()
                                    .updatetable(updatedata, taskk_id.toLong())
                            }
                            dialog.dismiss()
                            recreate()
                        }
                    }
                }
            }
        })
        val checkboxdatabase = Room.databaseBuilder(applicationContext,CheckedboxDatabase::class.java, "Checkedbox_tabl")
            .allowMainThreadQueries()
            .build()
        adapter.setOnItemClickListenercheckbox(object :Second_Adapter.onItemClickListenercheckbox {
            override fun onItemClickcheckbox(task_id: String, checkBox: CheckBox, name: String,  textView: TextView) {
                if (checkBox.isChecked) {
                     val ss = SpannableString(name.toString())
                    val strikethroughSpan = StrikethroughSpan()
                    ss.setSpan(strikethroughSpan, 0, name.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    textView.setText(ss)
                    GlobalScope.launch {
                        checkboxdatabase.CheckedboxDao().inserContact(Checked_box_Data(0, task_id))
                    }
                }
                if (checkBox.isChecked.not()) {
                    textView.setText(name.toString())

                    GlobalScope.launch {
                        checkboxdatabase.CheckedboxDao().deleteByUid(task_id.toLong())
                    }
                }
                }
        }
        )
        adapter.setOnItemClickListener(object :Second_Adapter.onItemClickListener{

            override fun onItemClick(task_id: String,name:String) {
                val dialog=Dialog(this@NextActivity)
                dialog.setContentView(R.layout.alert_dialog)
                val cancel_button=dialog.findViewById<TextView>(R.id.alert_cancel_button)
                val okdeletebutton=dialog.findViewById<TextView>(R.id.alert_ok_button)
                val setdeletetext=dialog.findViewById<TextView>(R.id.delete_set_text)
               // setdeletetext.text=name.toString()
                setdeletetext.setText("'$name"+"' will be permanently deleted.")
                dialog.show()
                cancel_button.setOnClickListener {
                    dialog.dismiss()
                }
                okdeletebutton.setOnClickListener {
                    GlobalScope.launch {
                        database2.secondScreenDao().deleteByUid(task_id.toLong())
                    }
                    dialog.dismiss()
                    recreate()
                }
            }
        })
        enter_text = findViewById(R.id.second_screen_edittext)
        second_screen_save = findViewById(R.id.second_screen_save)
      //  enter_text.addTextChangedListener()
        enter_text.addTextChangedListener {
            second_screen_save.setBackgroundResource(R.drawable.add_task_background22222)
           // val items= listOf("hello","good","bad","anurag")
            if (enter_text.text.toString().length >0) {
               // second_screen_save.setBackgroundColor(R.drawable.add_task_background22)
                second_screen_save.setBackgroundResource(R.drawable.add_second_screen_save)

                    val recomendation=MyService.newsInstance.searchrecomendation(enter_text.text.toString())
                    recomendation.enqueue(object : Callback<ResPonse_class> {
                        override fun onFailure(call: Call<ResPonse_class>, t: Throwable) {
                            Log.d("RESPONSE_DATA", "Error in fetching data",)
                        }
                        override fun onResponse(call: Call<ResPonse_class>, response: Response<ResPonse_class>) {
                            val news = response.body()
                            if (news != null){
                                Log.d("RESPONSE_DATA", news.toString())
                                val autoComplete:AutoCompleteTextView=findViewById(R.id.second_screen_edittext)
                                val adapter= ArrayAdapter(this@NextActivity,R.layout.list_layout,news)
                                autoComplete.setAdapter(adapter)
                              //  divaker(news)
                            }
                        }
                    })
            }
        }
        textView = findViewById<TextView?>(R.id.next_screen_task).apply {
            text = task_name
        }


        second_screen_save.setOnClickListener {
            input = enter_text.text.toString()
            if (input.isNotEmpty()) {
                enter_text.setText("")
                // Toast.makeText(this@NextActivity, adapter_position, Toast.LENGTH_SHORT).show()
                GlobalScope.launch {
                    database2.secondScreenDao()
                        .insertdatasecondscreen(SecondData(0, message2.toString(), input))
                }
            }
            if (input.isNotEmpty()) {
                recreate()
                }
            }
        }

  /*  private fun editfirstscreentask_insecondscreen(toString: String) {
        Toast.makeText(this@NextActivity,toString,Toast.LENGTH_SHORT).show()
        val dialog=Dialog(this@NextActivity)
        dialog.setContentView(R.layout.edit_task_dialog_screen)
        val no_button=dialog.findViewById<TextView>(R.id.edit_task_no_button)
        val yes_button=dialog.findViewById<TextView>(R.id.edit_task_yes_button)
        dialog.show()
        no_button.setOnClickListener {
            dialog.dismiss()
        }
        yes_button.setOnClickListener {
            val dialog=Dialog(this@NextActivity)
            dialog.setContentView(R.layout.enter_updated_task_dialog)
            dialog.show()
            val edit_text_value=dialog.findViewById<EditText>(R.id.enter_updated_task)
            val image_button=dialog.findViewById<ImageView>(R.id.edit_task_button_save)
            val textView=dialog.findViewById<TextView>(R.id.enter_updated_task_text_view)

            image_button.setOnClickListener {
                val text=edit_text_value.text.toString()
                Toast.makeText(this@NextActivity,text,Toast.LENGTH_SHORT).show()
                val database = Room.databaseBuilder(applicationContext, ContactDatabase::class.java, "contactDB")
                    .allowMainThreadQueries()
                    .build()
                if (edit_text_value.toString().length == 0) {
                    Toast.makeText(this@NextActivity, "Please enter your updated task", Toast.LENGTH_SHORT).show()
                } else {
                    GlobalScope.launch {
                        database.contactDao().updatetable(edit_text_value.text.toString(), toString)
                    }
                    dialog.dismiss()
                    recreate()
                }
            }
        }
    }*/

    private fun divaker(news: ResPonse_class) {
        val autoComplete:AutoCompleteTextView=findViewById(R.id.second_screen_edittext)
        val adapter= ArrayAdapter(this@NextActivity,R.layout.list_layout,news)
        autoComplete.setAdapter(adapter)
    }

    private fun createNotificationchannel() {
    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
        val name:CharSequence="Channel Alarm"
        val description="channel for android"
        val importance=NotificationManager.IMPORTANCE_HIGH
        val channel=NotificationChannel("divaker",name,importance)
        channel.description=description
        val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
    }
}
