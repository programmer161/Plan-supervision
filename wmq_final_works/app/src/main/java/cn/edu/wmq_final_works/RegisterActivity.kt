package cn.edu.wmq_final_works

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import cn.edu.wmq_final_works.ui.dashboard.DashboardViewModel
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val dpHelper = MyDatabaseHelper(this,"PlanStore.db",3)
        val db = dpHelper.writableDatabase
        linearLayout3.getBackground().setAlpha(80)
        val viewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        button_register.setOnClickListener {
            if (editText_userName.text.toString() == "" || editText_passWord.text.toString() == ""){
                Toast.makeText(this,"用户名和密码均不能为空",Toast.LENGTH_LONG).show()
            }else{
                var time_plan1: String = ""
                viewModel.times.observe(this, Observer {
                    time_plan1 = it[Calendar.YEAR].toString()+it[Calendar.MONTH].toString()+it[Calendar.DAY_OF_MONTH].toString()+it[Calendar.HOUR_OF_DAY].toString()+it[Calendar.MINUTE].toString()+it[Calendar.SECOND].toString()
                })
                val values = ContentValues().apply {
                    put("idUser",time_plan1)
                    put("username",editText_userName.text.toString())
                    put("password",editText_passWord.text.toString())
                }
                db.insert("user",null,values)
                Toast.makeText(this,"用户信息注册成功",Toast.LENGTH_LONG).show()
                val intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
