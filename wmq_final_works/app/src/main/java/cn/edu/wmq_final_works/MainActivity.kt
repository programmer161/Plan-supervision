package cn.edu.wmq_final_works

import android.content.ContentValues
import android.os.Bundle
import android.text.TextUtils.replace
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import cn.edu.wmq_final_works.ui.dashboard.DashboardFragment
import cn.edu.wmq_final_works.ui.dashboard.PlanHomeFragment
import cn.edu.wmq_final_works.ui.home.HomeFragment
import cn.edu.wmq_final_works.ui.notifications.NotificationsFragment

var flag_state: Int = 0
//当用户登录进来后定义用户的idUser为全局变量

class MainActivity : AppCompatActivity() {

    val homeFragment = HomeFragment.newInstance()

    val planHomeFragment = PlanHomeFragment.newInstance()

    val notificationFragment = NotificationsFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val intent = getIntent()
//        idUser = intent.getStringExtra("idUser").toString()
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val dbHelper = MyDatabaseHelper(this,"PlanStore.db",3)
        val db = dbHelper.writableDatabase
        if (flag_state == 0){
            navView.selectedItemId = R.id.navigation_dashboard
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout_box,planHomeFragment)
                .commit()
            val values_state = ContentValues().apply{
                put("id","1")
                put("state","-1")
            }
            db.insert("plan4",null,values_state)
        }else{
            var state: String = ""
            val cursor_state = db.query("plan4",null,null,null,null,null,null)
            if (cursor_state.moveToFirst()){
                state = cursor_state.getString(cursor_state.getColumnIndex("state"))
            }
            if (state == "-1"){
                navView.selectedItemId = R.id.navigation_home
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout_box,homeFragment)
                    .commit()
            }else if (state == "0"){
                navView.selectedItemId = R.id.navigation_dashboard
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout_box,planHomeFragment)
                    .commit()
            }else{
                navView.selectedItemId = R.id.navigation_notifications
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout_box,notificationFragment)
                    .commit()
            }
        }
        flag_state = 1
        //1. 先判断数据库里面是否有该字段
        //没有 不管
        //有 再判断当前字段是哪个状态 status (1 主界面 2 计划 3 通知)
        //判断完之后根据status 替换当前的fragment()先设置颜色
        //bottomNav.selectedItemId = R.id.navigation_home
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.frameLayout_box,homeFragment)
//            .commit()

        //最后将status置为-1  ！！！！！

//        val navController = findNavController(R.id.nav_host_fragment)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)

//        setOnNavigationItemReselectedListener
        navView.setOnNavigationItemSelectedListener{
            when(it.itemId) {
                R.id.navigation_home ->
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout_box,homeFragment)
                        .commit()
                R.id.navigation_dashboard ->
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout_box,planHomeFragment)
                        .commit()
                R.id.navigation_notifications ->
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout_box,notificationFragment)
                        .commit()
            }
            true
        }

    }
}
