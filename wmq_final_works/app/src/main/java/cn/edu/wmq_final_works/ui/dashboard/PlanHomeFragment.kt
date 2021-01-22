package cn.edu.wmq_final_works.ui.dashboard

import android.content.ContentValues
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import android.widget.Toast
import cn.edu.wmq_final_works.MyDatabaseHelper

import cn.edu.wmq_final_works.R
import cn.edu.wmq_final_works.idUser
import kotlinx.android.synthetic.main.plan_home_fragment.*
import kotlinx.android.synthetic.main.plan_home_fragment.editText


class PlanHomeFragment : Fragment() {

    companion object {
        fun newInstance() = PlanHomeFragment()
    }

    private lateinit var viewModel: PlanHomeViewModel
    var listItems = ArrayList<Map<String,Any>>()
    var listItem = HashMap<String,Any>()

    val dashboardFragment = DashboardFragment.newInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.plan_home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PlanHomeViewModel::class.java)
        // TODO: Use the ViewModel
        val dbHelper = MyDatabaseHelper(activity!!,"PlanStore.db",3)
        val db = dbHelper.writableDatabase
        linearLayout2.getBackground().setAlpha(80)
        button_add_plan.setOnClickListener {
            val values_add_plan = ContentValues().apply {
                put("state","3")
            }
            db.update("plan4",values_add_plan,"id=?", arrayOf("1"))
            val intent = Intent(context,DashboardFragment::class.java)
            startActivity(intent)
        }
        show()
        button_search.setOnClickListener {
            var value: String = editText.text.toString()
            if (value == ""){
                val list = ArrayList<Map<String,Any>>()
                if (listItems.size > 0){
                    listItems = list
                }
                val cursor_user = db.query("userPlan",null,"idUser=?", arrayOf(idUser),null,null,null)
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
                val cursor_user = db.query("userPlan",null,"idUser=?", arrayOf(idUser),null,null,null)
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
                val cursor_user = db.query("userPlan",null,"idUser=?", arrayOf(idUser),null,null,null)
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
                Toast.makeText(activity,"无相关数据！",Toast.LENGTH_LONG).show()
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
            val intent1 = Intent(activity,PlanDetailsActivity::class.java).apply {
                putExtra("id1",listItems[position]["id1"].toString())
                putExtra("idUser", idUser)
            }
            startActivity(intent1)
        }
    }
    fun show(){
        val list = ArrayList<Map<String,Any>>()
        if (listItems.size > 0){
            listItems = list
        }
        val dbHelper = MyDatabaseHelper(activity!!,"PlanStore.db",3)
        val db = dbHelper.writableDatabase
        val cursor_user = db.query("userPlan",null,"idUser=?", arrayOf(idUser),null,null,null)
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
        val simpleAdapter = SimpleAdapter(activity, listItems,R.layout.plan_home_item, arrayOf("type","state","startTime","endTime"),
            intArrayOf(R.id.textView_type, R.id.textView_state, R.id.textView_startTime,R.id.textView_endTime))
        listView.adapter = simpleAdapter
    }
}
