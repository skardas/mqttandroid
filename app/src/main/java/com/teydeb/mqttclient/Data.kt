package com.teydeb.mqttclient

import java.util.*

class Data(var clientId: String, var sensorId: String, var value: String) {
    var date:Date = Calendar.getInstance().time
    override fun toString():String {
        return " -$sensorId : $value"
    }
}
