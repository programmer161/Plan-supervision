package cn.edu.wmq_final_works.ui.notifications

import android.content.ContentValues
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import cn.edu.wmq_final_works.MyDatabaseHelper
import cn.edu.wmq_final_works.R
import cn.edu.wmq_final_works.idUser
import kotlinx.android.synthetic.main.fragment_notifications.*

class NotificationsFragment : Fragment() {

    companion object {
        fun newInstance() = NotificationsFragment()
    }

    private lateinit var notificationsViewModel: NotificationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val dbHelper = MyDatabaseHelper(activity!!,"PlanStore.db",3)
        val db = dbHelper.writableDatabase
        linearLayout4.getBackground().setAlpha(80)
        val cursor_userName = db.query("user",null,"idUser=?", arrayOf(idUser),null,null,null)
        if (cursor_userName.moveToFirst()){
            textView_userName.text = cursor_userName.getString(cursor_userName.getColumnIndex("username"))
        }
        var listItems = ArrayList<Map<String,Any>>()
        var listItem = HashMap<String,Any>()
        var position_user: Int = -1
        //当一进入到相关的界面后则从数据库中读取该相关用户的信息
        val cursor_idUserOther_show = db.query("UserToUser",null,"idUser=?", arrayOf(idUser),null,null,null)
        if (cursor_idUserOther_show.moveToFirst()){
            do {
                var idUserOther = cursor_idUserOther_show.getString(cursor_idUserOther_show.getColumnIndex("idUserOther"))
                val cursor_idUserOther_username = db.query("user",null,"idUser=?",
                    arrayOf(idUserOther),null,null,null)
                if (cursor_idUserOther_username.moveToFirst()){
                    val listItem_null = HashMap<String,Any>()
                    listItem["idUser"] = idUserOther
                    listItem["username"] = cursor_idUserOther_username.getString(cursor_idUserOther_username.getColumnIndex("username"))
                    listItems.add(listItem)
                    listItem = listItem_null
                }
            }while (cursor_idUserOther_show.moveToNext())
            show_userItem(listItems)
        }
        button_add.setOnClickListener {
            position_user = -1
            if (editText_userName.text.toString() == "" || editText_passWord.text.toString() == ""){
                Toast.makeText(activity,"用户名和密码不能为空",Toast.LENGTH_LONG).show()
            }else{
                val listItem_null = HashMap<String,Any>()
                var cursor_idUserOther = db.query("user",null,"username=? and password=?",
                    arrayOf(editText_userName.text.toString(),editText_passWord.text.toString()),null,null,null)
                if (cursor_idUserOther.moveToFirst()){
                    val values_UserToUser = ContentValues().apply {
                        put("idUser", idUser)
                        put("idUserOther",cursor_idUserOther.getString(cursor_idUserOther.getColumnIndex("idUser")))
                    }
                    db.insert("UserToUser",null,values_UserToUser)
                    listItem["idUser"] = cursor_idUserOther.getString(cursor_idUserOther.getColumnIndex("idUser"))
                    listItem["username"] = editText_userName.text.toString()
                    listItem["password"] = editText_passWord.text.toString()
                    listItems.add(listItem)
                    listItem = listItem_null
                    show_userItem(listItems)
                }else{
                    Toast.makeText(activity,"无该用户信息，用户之间关联失败", Toast.LENGTH_LONG).show()
                }
            }
            editText_userName.text = null
            editText_passWord.text = null
        }
        ListView.setOnItemClickListener { parent, view, position, id ->
            position_user = position
            editText_userName.text = Editable.Factory.getInstance().newEditable(listItems[position]["username"].toString())
            editText_passWord.text = Editable.Factory.getInstance().newEditable(listItems[position]["password"].toString())
        }
        button_delete.setOnClickListener {
            if (position_user != -1){
                //删除数据库中相关的用户关联信息
                db.delete("UserToUser","idUser=? and idUserOther=?", arrayOf(idUser,listItems[position_user]["idUser"].toString()))
                listItems.removeAt(position_user)
                show_userItem(listItems)
            }else{
                Toast.makeText(activity,"请先选择所需要进行删除的数据",Toast.LENGTH_LONG).show()
            }
        }
    }
    fun show_userItem(listItems:ArrayList<Map<String,Any>>){
        val simpleAdapter = SimpleAdapter(activity, listItems,R.layout.user_item, arrayOf("username"),
        intArrayOf(R.id.textView_userName))
        ListView.adapter = simpleAdapter
    }
}