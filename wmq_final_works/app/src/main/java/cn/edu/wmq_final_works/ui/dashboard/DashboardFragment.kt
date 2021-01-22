package cn.edu.wmq_final_works.ui.dashboard

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import cn.edu.wmq_final_works.MainActivity
import cn.edu.wmq_final_works.MyDatabaseHelper
import cn.edu.wmq_final_works.R
import cn.edu.wmq_final_works.idUser
import cn.edu.wmq_final_works.ui.test.BlankFragment
import cn.edu.wmq_final_works.ui.test.BlankViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.reflect.Type
import java.text.FieldPosition
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class DashboardFragment :AppCompatActivity(){
    companion object {
        fun newInstance() = DashboardFragment()
    }

    private lateinit var viewModel: DashboardViewModel
    lateinit var Type:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_dashboard)
        //设置总体布局的透明度
        linearLayout.getBackground().setAlpha(80)
        val dbHelper = MyDatabaseHelper(this, "PlanStore.db", 3)
        val db = dbHelper.writableDatabase
        linearLayout.getBackground().setAlpha(80)
        val listItems = ArrayList<Map<String,Any>>()
        viewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        var flag_plan :String = ""
        val cursor_state = db.query("plan4",null,null,null,null,null,null)
        if (cursor_state.moveToFirst()){
            flag_plan = cursor_state.getString(cursor_state.getColumnIndex("state"))
        }
        var save_delete: String = ""
        var idUserOther: String = idUser
        if(flag_plan == "2"){
            val get_intent = getIntent()
            val id1 = get_intent.getStringExtra("id1")
            idUserOther = get_intent.getStringExtra("idUser").toString()
            save_delete = id1.toString()
            val cursor = db.query("plan1",null,"id1=?", arrayOf(id1),null,null,null)
            if (cursor.moveToFirst()){
                val type = cursor.getString(cursor.getColumnIndex("type"))
                if (type == "学习"){
                    radioButton_study.isChecked = true
                }else if(type == "会议"){
                    radioButton_meeting.isChecked = true
                }else if (type == "出行"){
                    radioButton_travel.isChecked = true
                }else if (type == "生日"){
                    radioButton_birthday.isChecked = true
                }else if (type == "纪念日"){
                    radioButton_mark.isChecked = true
                }else{
                    radioButton_other.isChecked = true
                }
                textView_show_start_date.text = cursor.getString(cursor.getColumnIndex("startTime"))
                textView_show_end_date.text = cursor.getString(cursor.getColumnIndex("endTime"))
                viewModel.times.observe(this, Observer {
                    textView_alarm.text = String.format("%02d:%02d",it[Calendar.HOUR_OF_DAY],it[Calendar.MINUTE])
                })
            }
            val cursor1 = db.query("plan3",null,"id1=?", arrayOf(id1),null,null,null)
            var listItem = HashMap<String,Any>()
            if (cursor1.moveToFirst()){
                do {
                    var id2 = cursor1.getString(cursor1.getColumnIndex("id2"))
                    var cursor2 = db.query("plan2",null,"id2=?", arrayOf(id2),null,null,"time")
                    if (cursor2.moveToFirst()){
                        val listItem_null = HashMap<String,Any>()
                        listItem["item"] = cursor2.getString(cursor2.getColumnIndex("item"))
                        listItem["time"] = cursor2.getString(cursor2.getColumnIndex("time"))
                        listItem["progress"] = cursor2.getString(cursor2.getColumnIndex("progress"))
                        listItems.add(listItem)
                        listItem = listItem_null
                    }
                }while (cursor1.moveToNext())
            }
            show_listView(listItems)
        }else{
            //展示当前的日期时间
            viewModel.times.observe(this, Observer {
                textView_show_start_date.text = String.format("%02d-%02d-%02d",it[Calendar.YEAR],it[Calendar.MONTH],it[Calendar.DAY_OF_MONTH])
                textView_show_end_date.text = String.format("%02d-%02d-%02d",it[Calendar.YEAR],it[Calendar.MONTH],it[Calendar.DAY_OF_MONTH])
                textView_alarm.text = String.format("%02d:%02d",it[Calendar.HOUR_OF_DAY],it[Calendar.MINUTE])
            })
        }
        var position_plan : Int = -1
        textView_start_date.setOnClickListener {
            pick_time(textView_start_date)
        }
        textView_end_date.setOnClickListener {
            pick_time(textView_end_date)
        }
        textView_alarm.setOnClickListener {
            pick_time(textView_alarm)
        }
        //相关确认状态的相关显示
        button_confirm.setOnClickListener {
            if (button_confirm.text == "已完成"){
                Toast.makeText(this,"已经确认完成，不能再对该按钮进行操作",Toast.LENGTH_LONG).show()
                //当确认完成后设置该相关确认按钮为不可操作
                button_confirm.isEnabled = false
            }
            button_confirm.text = "已完成"
        }
        //当按下此按钮后进行动态的添加该组件,同时相关的数据暂时先存在本地，当按下保存后才存入到数据库中
        button_add.setOnClickListener {
//            获取到所增加的数据
            val listItem = HashMap<String,Any>()
            val confirm_sate: String
            listItem["item"] = editText.text.toString()
            listItem["time"] = textView_alarm.text.toString()
            if (button_confirm.text.toString()=="确认完成"){
                confirm_sate = "未完成"
            }else{
                confirm_sate = button_confirm.text.toString()
            }
            listItem["progress"] = confirm_sate
            listItems.add(listItem)
            show_plan()
            show_listView(listItems)
        }
        //listView中进行监听其所选择了哪个item的相关位置
        ListView.setOnItemClickListener { parent, view, position, id ->
            position_plan = position
            editText.setText(listItems[position]["item"].toString())
            textView_alarm.text = listItems[position]["time"].toString()
            button_confirm.text = listItems[position]["progress"].toString()
        }
        //当按下这个按钮选择相关的信息后进行相关内容的删除
        button_delete.setOnClickListener {
            if (position_plan != -1){
                listItems.removeAt(position_plan)
            }else{
                Toast.makeText(this,"请选择所要进行删除的数据",Toast.LENGTH_LONG).show()
            }
            position_plan = -1
            show_plan()
            show_listView(listItems)
        }
        //当按下这个按钮后对相关的信息内容进行保存
        //将相关的数据存在数据库中
        button_save.setOnClickListener {
            //对相关的数据进行保存时为了不出现相关的错误可以先删除相关的数据后在进行保存
            if (save_delete != ""){
                db.delete("userPlan","idPlan=?", arrayOf(save_delete))
                db.delete("plan1","id1=?", arrayOf(save_delete))
                val cursor_plan2 = db.query("plan3",null,"id1=?", arrayOf(save_delete),null,null,null)
                if (cursor_plan2.moveToFirst()){
                    do {
                        val id2 = cursor_plan2.getString(cursor_plan2.getColumnIndex("id2"))
                        db.delete("plan2","id2=?", arrayOf(id2))
                    }while (cursor_plan2.moveToNext())
                }
                db.delete("plan3","id1=?", arrayOf(save_delete))
            }
            //获取到标签的相关类型
            if (radioButton_study.isChecked){
                Type = radioButton_study.text.toString()
            }else if(radioButton_meeting.isChecked){
                Type = radioButton_meeting.text.toString()
            }else if(radioButton_travel.isChecked){
                Type = radioButton_travel.text.toString()
            }else if(radioButton_birthday.isChecked){
                Type = radioButton_birthday.text.toString()
            }else if(radioButton_mark.isChecked){
                Type = radioButton_mark.text.toString()
            }else{
                Type = radioButton_other.text.toString()
            }
            //将相关的基础数据存放到plan1中
            var time_plan1: String
            var time_plan2: String
            viewModel.times.observe(this, Observer {
                time_plan1 = it[Calendar.YEAR].toString()+it[Calendar.MONTH].toString()+it[Calendar.DAY_OF_MONTH].toString()+it[Calendar.HOUR_OF_DAY].toString()+it[Calendar.MINUTE].toString()+it[Calendar.SECOND].toString()
                //将相关的计划事项数据存放在plan2中
                var flag_state: Int = 0
                for (i in 1..listItems.size){
                    time_plan2 = it[Calendar.YEAR].toString()+it[Calendar.MONTH].toString()+it[Calendar.DAY_OF_MONTH].toString()+it[Calendar.HOUR_OF_DAY].toString()+it[Calendar.MINUTE].toString()+it[Calendar.SECOND].toString()+i.toString()
                    if (listItems[i-1]["progress"] == "未完成"){
                        flag_state = 1
                    }
                    val values2 = ContentValues().apply {
                        put("id2",time_plan2)
                        put("item",listItems[i-1]["item"].toString())
                        put("time",listItems[i-1]["time"].toString())
                        put("progress",listItems[i-1]["progress"].toString())
                    }
                    db.insert("plan2",null,values2)
                    //将plan1与plan2中的相关数据进行关联
                    val values3 = ContentValues().apply {
                        put("id1",time_plan1)
                        put("id2",time_plan2)
                    }
                    db.insert("plan3",null,values3)
                }
                //将该计划的基础数据存放在plan1中
                val state: String
                if(flag_state == 1){
                    state = "未完成"
                }else{
                    state = "已完成"
                }
                val values1 = ContentValues().apply {
                    put("id1",time_plan1)
                    put("type",Type)
                    put("startTime",textView_show_start_date.text.toString())
                    put("endTime",textView_show_end_date.text.toString())
                    put("state",state)
                }
                db.insert("plan1",null,values1)
                //此时所进行存储的用户需要进行相关的判断，看其所传的用户值
                val values_user_plan = ContentValues().apply {
                    put("idUser", idUserOther)
                    put("idPlan",time_plan1)
                }
                db.insert("userPlan",null,values_user_plan)
                val values_state = ContentValues().apply {
                    put("state","0")
                }
                db.update("plan4",values_state,"id=?", arrayOf("1"))
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                //1. 存当前是需要跳转到哪个fragment status 2
                //2. 跳转到三个fragment的父亲activity （mainactivity）
            })

        }
        //当按下这个按钮后可以对相关的内容进行修改
        button_edit.setOnClickListener {
            val listitem_plan2 = HashMap<String,Any>()
            listitem_plan2["item"] = editText.text.toString()
            listitem_plan2["time"] = textView_alarm.text.toString()
            listitem_plan2["progress"] = button_confirm.text.toString()
            if (position_plan != -1){
                listItems[position_plan] = listitem_plan2
            }else{
                Toast.makeText(this,"请选择所要进行修改的数据",Toast.LENGTH_LONG).show()
            }
            position_plan = -1
            show_plan()
            show_listView(listItems)
        }
    }
    //更新listView
    fun show_listView(listItems: ArrayList<Map<String,Any>>){
        val simpleAdapter = SimpleAdapter(this, listItems,R.layout.plan_item, arrayOf("item","time","progress"),
            intArrayOf(R.id.textView, R.id.textView2, R.id.textView3))
        ListView.adapter = simpleAdapter
    }
    //相关列表项回到初始状态
    fun show_plan(){
        editText.text = null
        viewModel.times.observe(this, Observer {
            textView_alarm.text = String.format("%02d:%02d",it[Calendar.HOUR_OF_DAY],it[Calendar.MINUTE])
        })
        button_confirm.text = "确认完成"
    }
    //实现日期和时间相关选择的函数
    fun pick_time(view: View){
        when(view.id){
            //日期选择器
            R.id.textView_start_date -> {
                viewModel.times.observe(this, Observer {
                    var start_year = it[Calendar.YEAR]
                    var start_month = it[Calendar.MONTH]
                    var start_day = it[Calendar.DAY_OF_MONTH]
                    val datePickerDialog = DatePickerDialog(this,
                        DatePickerDialog.OnDateSetListener{_,year,month,dayOfMonth ->
                            start_year = year
                            start_month = month
                            start_day = dayOfMonth
                            textView_show_start_date.text = String.format("%02d-%02d-%02d",year,month+1,dayOfMonth)
                        },
                        start_year,start_month,start_day
                    )
                    datePickerDialog.show()
                })
            }
            R.id.textView_end_date -> {
                viewModel.times.observe(this, Observer {
                    var start_year = it[Calendar.YEAR]
                    var start_month = it[Calendar.MONTH]
                    var start_day = it[Calendar.DAY_OF_MONTH]
                    val datePickerDialog = DatePickerDialog(this,
                        DatePickerDialog.OnDateSetListener{_,year,month,dayOfMonth ->
                            start_year = year
                            start_month = month
                            start_day = dayOfMonth
                            textView_show_end_date.text = String.format("%02d-%02d-%02d",year,month+1,dayOfMonth)
                        },
                        start_year,start_month,start_day
                    )
                    datePickerDialog.show()
                })
            }
            R.id.textView_alarm -> {
                viewModel.times.observe(this, Observer {
                    var endHour = it[Calendar.HOUR_OF_DAY]
                    var endMinute = it[Calendar.MINUTE]
                    val timePickerDialog = TimePickerDialog(this,
                        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                            endHour = hourOfDay
                            endMinute = minute
                            textView_alarm.text = String.format("%02d:%02d",hourOfDay,minute)
                        },
                        endHour, endMinute,true
                    )
                    timePickerDialog.show()
                })
            }
        }
    }
}