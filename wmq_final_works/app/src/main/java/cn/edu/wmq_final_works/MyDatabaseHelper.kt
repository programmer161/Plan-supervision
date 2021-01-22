package cn.edu.wmq_final_works

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

//创建数据库和相关的表
class MyDatabaseHelper(val context: Context,name: String, version: Int) :
        SQLiteOpenHelper(context, name, null, version){
    //存储计划的最基本信息
    private val createPlan1 = "create table plan1("+
            "id1 text primary key,"+
            "type text,"+
            "startTime text,"+
            "state text,"+
            "endTime text)"
    //存储计划的具体事项内容
    private val createPlan2 = "create table plan2("+
            "id2 text primary key,"+
            "item text,"+
            "time text,"+
            "progress text)"
    //存储计划基本信息与具体计划事项的相互关联信息
    private val createPlan = "create table plan3("+
            "id integer primary key autoincrement,"+
            "id1 text,"+
            "id2 text)"
    //存储相关的界面状态信息
    private val createPlan3 = "create table plan4("+
            "id text,"+
            "state text)"
    //保存用户的基本信息
    private val createUser = "create table user("+
            "idUser text primary key,"+
            "username text,"+
            "password text)"
    //存储用户与各计划之间的相互关系
    private val createUserPlan = "create table userPlan("+
            "id integer primary key autoincrement,"+
            "idUser text,"+
            "idPlan text)"
    //存储用户与各用户之间的相互监督关系
    private val createUserToUser = "create table UserToUser("+
            "id integer primary key autoincrement,"+
            "idUser text,"+
            "idUserOther text)"
    override fun onCreate(db: SQLiteDatabase){
        db.execSQL(createPlan)
        db.execSQL(createPlan1)
        db.execSQL(createPlan2)
        db.execSQL(createPlan3)
        db.execSQL(createUser)
        db.execSQL(createUserPlan)
        db.execSQL(createUserToUser)
        Toast.makeText(context,"create succeeded",Toast.LENGTH_SHORT).show()
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int){
        db.execSQL("drop table if exists plan1")
        db.execSQL("drop table if exists plan2")
        db.execSQL("drop table if exists plan3")
        db.execSQL("drop table if exists plan4")
        db.execSQL("drop table if exists user")
        db.execSQL("drop table if exists userPlan")
        db.execSQL("drop table if exists UserToUser")
        onCreate(db)
    }
}