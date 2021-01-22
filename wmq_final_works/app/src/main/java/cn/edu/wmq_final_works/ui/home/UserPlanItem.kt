package cn.edu.wmq_final_works.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SimpleAdapter
import android.widget.Toast
import cn.edu.wmq_final_works.MyDatabaseHelper
import cn.edu.wmq_final_works.R
import cn.edu.wmq_final_works.idUser
import cn.edu.wmq_final_works.ui.dashboard.PlanDetailsActivity
import kotlinx.android.synthetic.main.activity_user_plan_item.*

class UserPlanItem : AppCompatActivity() {

    var listItems = ArrayList<Map<String,Any>>()
    var listItem = HashMap<String,Any>()
    var idUserOther: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_plan_item)
        val intent = getIntent()
        linearLayout2.getBackground().setAlpha(80)
        idUserOther = intent.getStringExtra("idUser").toString()
        textView_username.text = intent.getStringExtra("username").toString()
        val dbHelper = MyDatabaseHelper(this,"PlanStore.db",3)
        val db = dbHelper.writableDatabase
        show()
        button_search.setOnClickListener {
            var value: String = editText.text.toString()
            if (value == ""){
                val list = ArrayList<Map<String,Any>>()
                if (listItems.size > 0){
                    listItems = list
                }
                val cursor_user = db.query("userPlan",null,"idUser=?", arrayOf(idUserOther),null,null,null)
                if (cursor_user.moveToFirst()){
                    do{
                        val id1 = cursor_user.getString(cursor_user.getColumnIndex("idPlan"))
                        val cursor = db.query("plan1",null,"id1=?", arrayOf(id1),null,null,"startTime")
                        if (cursor.moveToFirst()){
                            do{
                                val listItem_null = HashMap<String,Any>()
                                listItem["id1"] = cursor.getString(cursor.getColumnIndex("id1"))
                                listItem["type"] = cursor.getString(cursor.getColumnIndex("type"))
                                listItem["startTime"] = cursor.getString(cursor.getColumnIndex("startTime"))
                                listItem["endTime"] = cursor.getString(cursor.getColumnIndex("endTime"))
                                listItem["state"] = cursor.getString(cursor.getColumnIndex("state"))
                                listItems.add(listItem)
                                listItem = listItem_null
                            }while (cursor.moveToNext())
                        }
                    }while (cursor_user.moveToNext())
                }
            }else if (value == "未完成" || value == "已完成"){
                val list = ArrayList<Map<String,Any>>()
                if (listItems.size > 0){
                    listItems = list
                }
                val cursor_user = db.query("userPlan",null,"idUser=?", arrayOf(idUserOther),null,null,null)
                if (cursor_user.moveToFirst()){
                    do{
                        val id1 = cursor_user.getString(cursor_user.getColumnIndex("idPlan"))
                        val cursor = db.query("plan1",null,"state=? and id1=?", arrayOf(value,id1),null,null,"startTime")
                        if (cursor.moveToFirst()) {
                            do {
                                val listItem_null = HashMap<String,Any>()
                                listItem["id1"] = cursor.getString(cursor.getColumnIndex("id1"))
                                listItem["type"] = cursor.getString(cursor.getColumnIndex("type"))
                                listItem["startTime"] = cursor.getString(cursor.getColumnIndex("startTime"))
                                listItem["endTime"] = cursor.getString(cursor.getColumnIndex("endTime"))
                                listItem["state"] = cursor.getString(cursor.getColumnIndex("state"))
                                listItems.add(listItem)
                                listItem = listItem_null
                            } while (cursor.moveToNext())
                        }
                    }while (cursor_user.moveToNext())
                }
            }else if (value == "学习" || value == "会议" || value == "出行" ||value == "生日" || value == "纪念日" ||value == "其他"){
                val list = ArrayList<Map<String,Any>>()
                if (listItems.size > 0){
                    listItems = list
                }
                val cursor_user = db.query("userPlan",null,"idUser=?", arrayOf(idUser),null,null,null)
                if (cursor_user.moveToFirst()){
                    do{
                        val id1 = cursor_user.getString(cursor_user.getColumnIndex("idPlan"))
                        val cursor = db.query("plan1",null,"type=? and id1=?", arrayOf(value,id1),null,null,"startTime")
                        if (cursor.moveToFirst()) {
                            do {
                                val listItem_null = HashMap<String,Any>()
                                listItem["id1"] = cursor.getString(cursor.getColumnIndex("id1"))
                                listItem["type"] = cursor.getString(cursor.getColumnIndex("type"))
                                listItem["startTime"] = cursor.getString(cursor.getColumnIndex("startTime"))
                                listItem["endTime"] = cursor.getString(cursor.getColumnIndex("endTime"))
                                listItem["state"] = cursor.getString(cursor.getColumnIndex("state"))
                                listItems.add(listItem)
                                listItem = listItem_null
                            } while (cursor.moveToNext())
                        }
                    }while (cursor_user.moveToNext())
                }
            }
            else{
                val list = ArrayList<Map<String,Any>>()
                if (listItems.size > 0){
                    listItems = list
                }
                val cursor_user = db.query("userPlan",null,"idUser=?", arrayOf(idUserOther),null,null,null)
                if (cursor_user.moveToFirst()){
                    do{
                        val id1 = cursor_user.getString(cursor_user.getColumnIndex("idPlan"))
                        val cursor = db.query("plan1",null,"startTime=? and id1=?", arrayOf(value,id1),null,null,"startTime")
                        if (cursor.moveToFirst()) {
                            do {
                                val listItem_null = HashMap<String,Any>()
                                listItem["id1"] = cursor.getString(cursor.getColumnIndex("id1"))
                                listItem["type"] = cursor.getString(cursor.getColumnIndex("type"))
                                listItem["startTime"] = cursor.getString(cursor.getColumnIndex("startTime"))
                                listItem["endTime"] = cursor.getString(cursor.getColumnIndex("endTime"))
                                listItem["state"] = cursor.getString(cursor.getColumnIndex("state"))
                                listItems.add(listItem)
                                listItem = listItem_null
                            } while (cursor.moveToNext())
                        }
                    }while (cursor_user.moveToNext())
                }
            }
            if (listItems.size == 0){
                Toast.makeText(this,"无相关数据！",Toast.LENGTH_LONG).show()
            }
            show_plan(listItems)
        }
        show_plan(listItems)
        listView.setOnItemClickListener { parent, view, position, id ->
//            var id1: String = ""
//            val cursor = db.query("plan1", arrayOf("id1"),"type=? and startTime=? and endTime=? and state=?",
//                arrayOf(listItems[position]["type"].toString(),listItems[position]["startTime"].toString(),listItems[position]["endTime"].toString(),listItems[position]["state"].toString()),null,null,null)
//            if (cursor.moveToFirst()){
//                id1 = cursor.getString(cursor.getColumnIndex("id1"))
//            }
            val intent1 = Intent(this, PlanDetailsActivity::class.java).apply {
                putExtra("id1",listItems[position]["id1"].toString())
                putExtra("idUser",idUserOther)
            }
            startActivity(intent1)
        }
    }
    fun show(){
        val list = ArrayList<Map<String,Any>>()
        if (listItems.size > 0){
            listItems = list
        }
        val dbHelper = MyDatabaseHelper(this,"PlanStore.db",3)
        val db = dbHelper.writableDatabase
        val cursor_user = db.query("userPlan",null,"idUser=?", arrayOf(idUserOther),null,null,null)
        if (cursor_user.moveToFirst()){
            do{
                val id1 = cursor_user.getString(cursor_user.getColumnIndex("idPlan"))
                val cursor = db.query("plan1",null,"id1=?", arrayOf(id1),null,null,"startTime")
                Log.d("cursor_user","${cursor.moveToFirst()}")
                if (cursor.moveToFirst()){
                    do{
                        val listItem_null = HashMap<String,Any>()
                        listItem["id1"] = cursor.getString(cursor.getColumnIndex("id1"))
                        listItem["type"] = cursor.getString(cursor.getColumnIndex("type"))
                        listItem["startTime"] = cursor.getString(cursor.getColumnIndex("startTime"))
                        listItem["endTime"] = cursor.getString(cursor.getColumnIndex("endTime"))
                        listItem["state"] = cursor.getString(cursor.getColumnIndex("state"))
                        listItems.add(listItem)
                        listItem = listItem_null
                    }while (cursor.moveToNext())
                }
            }while (cursor_user.moveToNext())
        }
    }
    fun show_plan(listItems:ArrayList<Map<String,Any>>){
        val simpleAdapter = SimpleAdapter(this, listItems,R.layout.plan_home_item, arrayOf("type","state","startTime","endTime"),
            intArrayOf(R.id.textView_type, R.id.textView_state, R.id.textView_startTime,R.id.textView_endTime))
        listView.adapter = simpleAdapter
    }
}
