package cn.edu.wmq_final_works.ui.dashboard

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.SimpleAdapter
import cn.edu.wmq_final_works.MyDatabaseHelper
import cn.edu.wmq_final_works.R
import kotlinx.android.synthetic.main.activity_plan_details.*

class PlanDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan_details)
        linearLayout.getBackground().setAlpha(80)
        val intent = getIntent()
        val id1: String? = intent.getStringExtra("id1")
        val idUserOther: String? = intent.getStringExtra("idUser")
        val dbHelper = MyDatabaseHelper(this, "PlanStore.db", 3)
        val db = dbHelper.writableDatabase
        //跳转到可进行编辑的相关页面
        button.setOnClickListener {
            val values_state = ContentValues().apply {
                put("state","2")
            }
            db.update("plan4",values_state,"id=?", arrayOf("1"))
            val intent_edit = Intent(this,DashboardFragment::class.java).apply {
                putExtra("id1",id1)
                putExtra("idUser",idUserOther)
            }
            startActivity(intent_edit)
        }
        val cursor = db.query("plan1",null,"id1=?", arrayOf(id1),null,null,null)
        if (cursor.moveToFirst()){
            textView_type.text = cursor.getString(cursor.getColumnIndex("type"))
            textView_startTime.text = cursor.getString(cursor.getColumnIndex("startTime"))
            textView_endTime.text = cursor.getString(cursor.getColumnIndex("endTime"))
        }
        val cursor1 = db.query("plan3",null,"id1=?", arrayOf(id1),null,null,null)
        var listItems = ArrayList<Map<String,Any>>()
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
        val simpleAdapter = SimpleAdapter(this,listItems,R.layout.plan_item, arrayOf("item","time","progress"),
            intArrayOf(R.id.textView,R.id.textView2,R.id.textView3))
        ListView.adapter = simpleAdapter
    }
}
