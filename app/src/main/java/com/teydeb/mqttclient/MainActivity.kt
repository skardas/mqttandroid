package com.teydeb.mqttclient


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(),  UIUpdaterInterface  {

    var mqttManager:MQTTmanager? = null
    var nextId = 0

    // Interface methods
    override fun resetUIWithConnection(status: Boolean) {

        ipAddressField.isEnabled  = !status
        topicField.isEnabled      = !status
        connectBtn.isEnabled      = !status
        disConnectBtn.isEnabled      = status

        // Update the status label.
        if (status){
            updateStatusViewWith("Connected")

        }else{
            updateStatusViewWith("Disconnected")
        }
    }

    override fun updateStatusViewWith(status: String) {
        statusLabl.text = "$status-tester$nextId"
    }

    var count:Float = 3f
    override fun update(message: String) {
        //update line chart

        count++
        val data = Gson().fromJson(message,Data::class.java)
        //dataArrayList.add(data)

        if(count > 50)
        {
            entryArrayList.removeAt(0)
        }
        entryArrayList.add(Entry(count, data.value.toFloat()))

        runOnUiThread (Runnable {
            val dataSet = LineDataSet(entryArrayList,"Sicaklik Verileri")
            val data = LineData(dataSet)
            lineChart.data = data
            lineChart.invalidate()
            lineChart.notifyDataSetChanged()
        })

    }
    var dataArrayList = ArrayList<Data>()
    var entryArrayList = ArrayList<Entry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enable send button and message textfield only after connection
        resetUIWithConnection(false)
        lineChart.setPinchZoom(true)
        lineChart.setTouchEnabled(true)

        entryArrayList.add(Entry(1f,2f))
        entryArrayList.add(Entry(2f,1f))
        entryArrayList.add(Entry(3f,3f))
        val dataSet = LineDataSet(entryArrayList,"Sicaklik Verileri")
        val data = LineData(dataSet)
        lineChart.data = data

    }
    fun disconnect(view: View) {
        resetUIWithConnection(false)
        mqttManager?.disconnect()
    }
    fun connect(view: View){

        if (!(ipAddressField.text.isNullOrEmpty() && topicField.text.isNullOrEmpty())) {
            var host = "tcp://" + ipAddressField.text.toString()
            var topic = topicField.text.toString()
            nextId = java.util.Random().nextInt(100) + 800

            var connectionParams = MQTTConnectionParams("$nextId",host,topic,"tester$nextId","tester$nextId")
            mqttManager = MQTTmanager(connectionParams,applicationContext,this)
            mqttManager?.connect()
        }else{
            updateStatusViewWith("Please enter all valid fields")
        }

    }


}
