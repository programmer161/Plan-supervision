package cn.edu.wmq_final_works.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class DashboardViewModel : ViewModel() {

    //定义当前的时间
    private var _times: MutableLiveData<Calendar> = MutableLiveData()

    //使外部可以访问到当前的时间，但是不能对其进行修改
    var times: LiveData<Calendar> = _times

    init {
        getTime()
    }
    //获取到当前的时间
    fun getTime(){
        _times.value = Calendar.getInstance()
    }
}