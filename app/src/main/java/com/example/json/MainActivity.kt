package com.example.json

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var tvDate : TextView
    lateinit var eUserInput : EditText
    lateinit var spinner : Spinner
    lateinit var bConvert : Button
    lateinit var tvResult : TextView

    private var curencyDetails: Currency? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvDate = findViewById<View>(R.id.tvDate) as TextView
        eUserInput = findViewById(R.id.eUserInput)
        spinner = findViewById(R.id.spinner)
        bConvert =findViewById(R.id.bConvert)
        tvResult = findViewById(R.id.tvResult)


        val cur = arrayListOf("inr", "usd", "aud", "sar", "cny", "jpy")

        var selected: Int = 0

        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, cur
            )
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    selected = position
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

        bConvert.setOnClickListener {

            var sel = eUserInput.text.toString()
            var currency: Double = sel.toDouble()

            getCurrency(onResult = {
                curencyDetails = it

                when (selected) {
                    0 -> disp(calc(curencyDetails?.eur?.inr?.toDouble(), currency));
                    1 -> disp(calc(curencyDetails?.eur?.usd?.toDouble(), currency));
                    2 -> disp(calc(curencyDetails?.eur?.aud?.toDouble(), currency));
                    3 -> disp(calc(curencyDetails?.eur?.sar?.toDouble(), currency));
                    4 -> disp(calc(curencyDetails?.eur?.cny?.toDouble(), currency));
                    5 -> disp(calc(curencyDetails?.eur?.jpy?.toDouble(), currency));
                }
            })
        }

    }

private fun disp(calc: Double) {

    tvResult.text = "result " + calc
}

private fun calc(i: Double?, sel: Double): Double {
    var s = 0.0
    if (i != null) {
        s = (i * sel)
    }
    return s
}

private fun getCurrency(onResult: (Currency?) -> Unit) {
    val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)

    if (apiInterface != null) {
        apiInterface.getCurrency()?.enqueue(object : Callback<Currency> {
            override fun onResponse(
                call: Call<Currency>,
                response: Response<Currency>
            ) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<Currency>, t: Throwable) {
                onResult(null)
                Toast.makeText(applicationContext, "" + t.message, Toast.LENGTH_SHORT).show();
            }

        })
    }
}

}