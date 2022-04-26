package com.pratham.a7minworkoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_b_m_i.*
import kotlinx.android.synthetic.main.activity_b_m_i.toolbar_bmi_activity
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        setSupportActionBar(toolbar_history_activity)
        val actionbar=supportActionBar
        if(actionbar!=null){
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.title="HISTORY"
        }
        toolbar_history_activity.setNavigationOnClickListener {
            onBackPressed()
        }
        getAllCompletedDates()
    }

    private fun getAllCompletedDates(){
        val dbHandler=SqliteOpenHelper(this,null)
      val allCompletedDatesList= dbHandler.getAllCompletedDatesList()
        for(i in allCompletedDatesList){
            if(allCompletedDatesList.size>0){
                tvHistory.visibility= View.VISIBLE
                rvHistory.visibility=View.VISIBLE
                tvNoDataAvailable.visibility=View.GONE
                rvHistory.layoutManager=LinearLayoutManager(this)
                val historyAdapter=HistoryAdapter(this,allCompletedDatesList)
                rvHistory.adapter=historyAdapter
            }else{
                tvHistory.visibility= View.GONE
                rvHistory.visibility=View.GONE
                tvNoDataAvailable.visibility=View.VISIBLE
            }
        }
    }
}