package cn.edu.wmq_final_works.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var listItems = ArrayList<Map<String,Any>>()
        var listItem = HashMap<String,Any>()
        val dbHelper = MyDatabaseHelper(activity!!,"PlanStore.db",3)
        val db = dbHelper.writableDatabase
        linearLayout5.getBackground().setAlpha(80)
        //当一进入到相关的界面后则从数据库中读取该相关用户的信息
        val cursor_idUserOther_show = db.query("UserToUser",null,"idUser=?", arrayOf(idUser),null,null,null)
        if (cursor_idUserOther_show.moveToFirst()){
            val list = ArrayList<Map<String,Any>>()
            if (listItems.size > 0){
                listItems = list
            }
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
        button_search.setOnClickListener {
            val list1 = ArrayList<Map<String,Any>>()
            if (listItems.size > 0){
                listItems = list1
            }
            if (editText_userName.text.toString() == ""){
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
            }else{
                val idUserOther = db.query("UserToUser",null,"idUser=?", arrayOf(idUser),null,null,null)
                var flag: Int = 0
                if (idUserOther.moveToFirst()){
                    do {
                        var idUser_other = idUserOther.getString(idUserOther.getColumnIndex("idUserOther"))
                        var username = db.query("user",null,"idUser=? and username=?",
                            arrayOf(idUser_other,editText_userName.text.toString()),null,null,null)
                        if (username.moveToFirst()){
                            flag = 1
                            val listItem_null = HashMap<String,Any>()
                            listItem["idUser"] = idUser_other
                            listItem["username"] = username.getString(username.getColumnIndex("username"))
                            listItems.add(listItem)
                            listItem = listItem_null
                        }
                    }while (idUserOther.moveToNext())
                }
                if (flag == 1){
                    show_userItem(listItems)
                }else{
                    Toast.makeText(activity,"无相关数据",Toast.LENGTH_LONG).show()
                }
            }
        }
        ListView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(activity,UserPlanItem::class.java).apply {
                putExtra("idUser",listItems[position]["idUser"].toString())
                putExtra("username",listItems[position]["username"].toString())
            }
            startActivity(intent)
        }
    }
    fun show_userItem(listItems:ArrayList<Map<String,Any>>){
        val simpleAdapter = SimpleAdapter(activity, listItems,R.layout.user_item, arrayOf("username"),
            intArrayOf(R.id.textView_userName))
        ListView.adapter = simpleAdapter
    }
}