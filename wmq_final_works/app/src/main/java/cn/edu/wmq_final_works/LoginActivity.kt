package cn.edu.wmq_final_works

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

var idUser: String = ""
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val dpHelper = MyDatabaseHelper(this,"PlanStore.db",3)
        val db = dpHelper.writableDatabase
        linearLayout3.getBackground().setAlpha(80)
        button_register.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
            editText_userName.text = Editable.Factory.getInstance().newEditable("")
            editText_passWord.text = Editable.Factory.getInstance().newEditable("")
        }
        button_login.setOnClickListener {
            if (editText_userName.text.toString() == "" || editText_passWord.text.toString() == ""){
                Toast.makeText(this,"用户名和密码均不能为空",Toast.LENGTH_LONG).show()
            }else{
                val cursor = db.query("user",null,"username=? and password=?",
                    arrayOf(editText_userName.text.toString(),editText_passWord.text.toString()),null,null,null)
                if (cursor.moveToFirst()){
                    idUser = cursor.getString(cursor.getColumnIndex("idUser"))
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(this,"不存在该用户信息，请先进行注册后才进行登录",Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
