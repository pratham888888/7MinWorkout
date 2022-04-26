package com.pratham.a7minworkoutapp

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.android.synthetic.main.dialog_custom_back_confirmation.*
import java.util.*
import kotlin.collections.ArrayList


class ExerciseActivity : AppCompatActivity(),TextToSpeech.OnInitListener{
    private var restTimer: CountDownTimer? = null
    private var once=true
    private var restProgress = 0
    private var ExerciseTimeDuration:Long=30
    private var RestTimeDuration:Long=10
    private var ExerciseTimer:CountDownTimer?=null
    private var ExerciseProgress=0
    private var ExerciseList: ArrayList<ExerciseModel>? = null
    private var CurrentExercisePosition= -1
    private var tts:TextToSpeech?=null
    private var tts2:TextToSpeech?=null
    private var player:MediaPlayer?=null
    private var exerciseAdapter:ExerciseStatusAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)
        setSupportActionBar(toolbar_exercise_activity)
        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
        }

        toolbar_exercise_activity.setNavigationOnClickListener {
            customDialogForBackButton()
        }
        tts= TextToSpeech(this,this)
        tts2= TextToSpeech(this,this)
        ExerciseList=Constants.defaultExerciseList()
        setUpRestView()
        setupExerciseStatusRecyclerView()

    }

    override fun onDestroy() {

        super.onDestroy()
        if (restTimer != null) {
            restTimer!!.cancel()
            restProgress = 0
        }
        if (ExerciseTimer != null) {
            ExerciseTimer!!.cancel()
            ExerciseProgress = 0
        }
        if(tts!=null){
            tts!!.stop()
            tts!!.shutdown()
        }
        if(tts2!=null){
            tts2!!.stop()
            tts2!!.shutdown()
        }
        if(player!=null){
            player!!.stop()
        }
    }

    private fun setRestProgressBar() {
        progressBar.progress = restProgress
        restTimer = object : CountDownTimer(RestTimeDuration*1000, 1000)
        {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                progressBar.progress = 10 - restProgress
                tvTimer.text = (10 - restProgress).toString()
                if(CurrentExercisePosition==-1&&once){
                    SpeakOut("Get ready for jumping jacks")
                    once=false
                }

            }

            override fun onFinish() {
                CurrentExercisePosition++
                ExerciseList!![CurrentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()
                setUpExerciseView()

            }
        }.start()
    }

        private fun setExerciseProgressBar() {
            progressBar2.progress = ExerciseProgress
            ExerciseTimer = object : CountDownTimer(ExerciseTimeDuration*1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    ExerciseProgress++
                    progressBar2.progress = 30 -ExerciseProgress
                    tvTimer2.text = (30 - ExerciseProgress).toString()

                }

                override fun onFinish() {
                  if(CurrentExercisePosition<ExerciseList!!.size-1){
                      ExerciseList!![CurrentExercisePosition].setIsSelected(false)
                      ExerciseList!![CurrentExercisePosition].setIsCompleted(true)
                      exerciseAdapter!!.notifyDataSetChanged()
                      setUpRestView()
                  }
                    else{
                       finish()
                      val intent= Intent(this@ExerciseActivity,FinishActivity::class.java)
                      startActivity(intent)
                  }
                }
            }.start()

        }

    private fun setUpRestView() {

        llRestView.visibility=View.VISIBLE
        llExerciseView.visibility=View.GONE
        if (restTimer != null) {
            restTimer!!.cancel()
            restProgress = 0
        }
        SpeakOut("exercise done.Now get ready for"+ExerciseList!![CurrentExercisePosition+1].getName())
        setRestProgressBar()
        tvnextexercisename.text=ExerciseList!![CurrentExercisePosition+1].getName()
    }
    private fun setUpExerciseView() {
        try {
            player=MediaPlayer.create(applicationContext,R.raw.clocksound)
            player!!.isLooping=false
            player!!.start()
        }catch (e:Exception){
            e.printStackTrace()
        }

        llRestView.visibility=View.GONE
        llExerciseView.visibility=View.VISIBLE
        if (ExerciseTimer != null) {
            ExerciseTimer!!.cancel()
            ExerciseProgress = 0
        }
        SpeakOut("start"+ExerciseList!![CurrentExercisePosition].getName())
        setExerciseProgressBar()
        ivImage.setImageResource(ExerciseList!![CurrentExercisePosition].getImage())
        tvExerciseName.text=ExerciseList!![CurrentExercisePosition].getName()
    }

    override fun onInit(status: Int) {
        if(status==TextToSpeech.SUCCESS){
            val result=tts!!.setLanguage(Locale.US)
            if(result==TextToSpeech.LANG_MISSING_DATA||result==TextToSpeech.LANG_NOT_SUPPORTED){
            Log.e("TTS","the language is not supported")
            }
        }
        else{
            Log.e("TTS","initialisation failed")
        }
    }
   private fun SpeakOut(text:String){
       tts!!.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
   }

 private fun setupExerciseStatusRecyclerView(){
     rvExerciseStatus.layoutManager=LinearLayoutManager(this,
             LinearLayoutManager.HORIZONTAL,false)
     exerciseAdapter= ExerciseStatusAdapter(ExerciseList!!,this)
     rvExerciseStatus.adapter=exerciseAdapter
 }
    private fun customDialogForBackButton(){
        val customDialog=Dialog(this)
        customDialog.setContentView(R.layout.dialog_custom_back_confirmation)
        customDialog.tvYes.setOnClickListener {
             finish()
            customDialog.dismiss()
        }
        customDialog.tvNo.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }
}