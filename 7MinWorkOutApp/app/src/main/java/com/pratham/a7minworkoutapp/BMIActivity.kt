package com.pratham.a7minworkoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_b_m_i.*
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {
    val METRIC_UNITS_VIEW="METRIC_UNIT_VIEW"
    val US_UNITS_VIEW="US_UNIT_VIEW"
    var currentVisibleView:String=METRIC_UNITS_VIEW
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b_m_i)
        setSupportActionBar(toolbar_bmi_activity)
        val actionbar=supportActionBar
        if(actionbar!=null){
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.title="CALCULATE BMI"
        }
        toolbar_bmi_activity.setNavigationOnClickListener {
            onBackPressed()
         }

        btnCalculateUnits.setOnClickListener {
            if(currentVisibleView.equals(METRIC_UNITS_VIEW)) {
                if (validateMetricUnits()) {
                    val heightValue: Float = etMetricUnitHeight.text.toString().toFloat() / 100
                    val weightValue: Float = etMetricUnitWeight.text.toString().toFloat()
                    val bmi = weightValue / (heightValue * heightValue)
                    displayBMIResult(bmi)
                } else {
                    Toast.makeText(this@BMIActivity, "Please enter valid values", Toast.LENGTH_SHORT).show()
                }
            }else{
                        if(validateUSUnits()){
                            val usUnitheightValueFeet:String=etUsUnitHeightFeet.text.toString()
                            val usUnitheightValueInch:String=etUsUnitHeightInch.text.toString()
                            val UsUnitweightValue:Float=etUsUnitWeight.text.toString().toFloat()
                            val finalHeight=usUnitheightValueInch.toFloat()+usUnitheightValueFeet.toFloat()*12
                            val bmi=703*(UsUnitweightValue/(finalHeight*finalHeight))
                            displayBMIResult(bmi)
                        }else{
                            Toast.makeText(this@BMIActivity,"Please enter valid values",Toast.LENGTH_SHORT).show()
                        }
                    }
            }


        makeVisibleMetricUnitsView()
        rgUnits.setOnCheckedChangeListener { group, i ->
            if(i==R.id.rbMetricUnits){
                makeVisibleMetricUnitsView()
            }
            else{
                makeVisibleUSUnitsView()
            }
        }
    }
    private fun makeVisibleUSUnitsView(){
        currentVisibleView=US_UNITS_VIEW
        tilMetricUnitWeight.visibility=View.GONE
        tilMetricUnitHeight.visibility=View.GONE
        etUsUnitHeightFeet.text!!.clear()
        etUsUnitHeightInch.text!!.clear()
        etUsUnitWeight.text!!.clear()
        tilUsUnitWeight.visibility=View.VISIBLE
        llUsUnitsHeight.visibility=View.VISIBLE
        llDisplayBMIResult.visibility=View.INVISIBLE
    }
    private fun makeVisibleMetricUnitsView(){
        currentVisibleView=METRIC_UNITS_VIEW
        tilMetricUnitWeight.visibility=View.VISIBLE
        tilMetricUnitHeight.visibility=View.VISIBLE
        etMetricUnitHeight.text!!.clear()
        etMetricUnitWeight.text!!.clear()
        tilUsUnitWeight.visibility=View.GONE
        llUsUnitsHeight.visibility=View.GONE
        llDisplayBMIResult.visibility=View.INVISIBLE
    }
    private fun displayBMIResult(bmi:Float){
        val bmiLabel:String
        val bmiDescription:String
        if(bmi.compareTo(15f)<=0){
            bmiLabel="Very severely underweight"
            bmiDescription="Oops!You really need to take better care of your health!Eat more!Eat healthy!"
        }else if(bmi.compareTo(15f)>0&&bmi.compareTo(16f)<=0){
            bmiLabel="Severely underweight"
            bmiDescription="Oops!You really need to take care of your health!Eat more!Eat healthy!"
        }else if(bmi.compareTo(16f)>0&&bmi.compareTo(18.5f)<=0){
            bmiLabel="Underweight"
            bmiDescription="Oops!A little more care your health will be surely beneficial!Eat more!Eat healthy!"
        }else if(bmi.compareTo(18.5f)>0&&bmi.compareTo(25f)<=0){
            bmiLabel="Normal"
            bmiDescription="Congratulations!You are in a good shape!"
        }else if(bmi.compareTo(25f)>0&&bmi.compareTo(30f)<=0){
            bmiLabel="Overweight"
            bmiDescription="Oops!You need to take better care of your health!Start working out to get in a good shape."
        }else if(bmi.compareTo(30f)>0&&bmi.compareTo(35f)<=0){
            bmiLabel="Obese class| (Moderately obese)"
            bmiDescription="Oops!You really need to take care of your health!Start working out to get in a good shape"
        }else if(bmi.compareTo(35f)>0&&bmi.compareTo(40f)<=0){
            bmiLabel="Obese class ||(Severely obese)"
            bmiDescription="OMG!You are in a very dangerous condition!Act now!"
        }else {
            bmiLabel="Obese class |||( Very Severely obese)"
            bmiDescription="OMG!You are in a very serious condition!Act now!Shift to a healthier lifestyle."
        }
        llDisplayBMIResult.visibility=View.VISIBLE

        val bmiValue=BigDecimal(bmi.toDouble()).setScale(2,RoundingMode.HALF_EVEN).toString()
        tvBMIValue.text=bmiValue
        BMIType.text=bmiLabel
        BMIDescription.text=bmiDescription
    }
   private fun validateMetricUnits():Boolean{
       var isValid=true
       if(etMetricUnitWeight.text.toString().isEmpty()){
           isValid=false
       }
        else if(etMetricUnitHeight.text.toString().isEmpty()){
           isValid=false
       }
      return isValid
   }
    private fun validateUSUnits():Boolean{
        var isValid=true
        if(etUsUnitWeight.text.toString().isEmpty()){
            isValid=false
        }
        else if(etUsUnitHeightFeet.text.toString().isEmpty()){
            isValid=false
        }
        else if(etUsUnitHeightInch.text.toString().isEmpty()){
            isValid=false
        }

        return isValid
    }

}