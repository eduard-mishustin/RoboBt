package magym.robobt

import android.R.layout
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import java.io.IOException
import java.io.OutputStream
import java.util.Timer
import java.util.TimerTask
import java.util.UUID
import kotlin.math.abs
import kotlin.math.max

@SuppressLint("MissingPermission")
class MainActivity2017OldVersion : ComponentActivity(), View.OnClickListener {

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var pairedDeviceArrayList: ArrayList<String>? = null
    private var pairedDeviceAdapter: ArrayAdapter<String>? = null
    private var myUUID: UUID? = null
    private var myThreadConnectBTdevice: ThreadConnectBTdevice? = null
    private var myThreadConnected: ThreadConnected? = null

    private var listViewPairedDevice: ListView? = null

    private var textInfo: TextView? = null

    private var sensorManager: SensorManager? = null
    private var sensorAccel: Sensor? = null
    private var timer: Timer? = null
    private val valuesAccelGravity = IntArray(3) // Массив значений скорости моторов
    private val maxValue = 9
    private val minValue = 1 // Максимальный и минимальны порог данных с акс (0 - 9.8)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val UUID_STRING_WELL_KNOWN_SPP = "00001101-0000-1000-8000-00805F9B34FB"

        listViewPairedDevice = findViewById<View>(R.id.pairedlist) as ListView

        textInfo = findViewById<View>(R.id.textInfo) as TextView
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensorAccel = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            Toast.makeText(this, "BLUETOOTH NOT support", Toast.LENGTH_LONG).show()
            finish()
            return
        }
        myUUID = UUID.fromString(UUID_STRING_WELL_KNOWN_SPP)
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported on this hardware platform", Toast.LENGTH_LONG).show()
            finish()
            return
        }
        val stInfo = bluetoothAdapter!!.name + " " + bluetoothAdapter!!.address
        textInfo!!.text = String.format("Это устройство: %s", stInfo)
    }

    override fun onClick(v: View) {
    }

    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(listener, sensorAccel, SensorManager.SENSOR_DELAY_NORMAL)

        // Вывод данных в потоке и с задержкой
        timer = Timer()
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    val xAxis = valuesAccelGravity[0]
                    val yAxis = valuesAccelGravity[1]
                    var leftMotor = 0
                    var rightMotor = 0

                    if (xAxis > 0) { // Если вправо
                        if (yAxis >= 0) { // 1 четверть
                            leftMotor = max(abs(xAxis.toDouble()), abs(yAxis.toDouble()))
                                .toInt()
                            rightMotor = yAxis - xAxis
                        } else { // 4 четверть
                            leftMotor = yAxis + xAxis
                            rightMotor = (-max(abs(xAxis.toDouble()), abs(yAxis.toDouble()))).toInt()
                        }
                    } else if (xAxis < 0) { // Если влево
                        if (yAxis >= 0) { // 2 четверть
                            leftMotor = yAxis + xAxis
                            rightMotor = max(abs(xAxis.toDouble()), abs(yAxis.toDouble()))
                                .toInt()
                        } else { // 3 четверть
                            leftMotor = (-max(abs(xAxis.toDouble()), abs(yAxis.toDouble()))).toInt()
                            rightMotor = yAxis - xAxis
                        }
                    } else if (xAxis == 0) { // Едем по прямой
                        leftMotor = yAxis
                        rightMotor = yAxis
                    }

                    val motorData = ("m" + leftMotor + "z" + rightMotor + "z")

                    if (myThreadConnected != null) {
                        println("Send data: $motorData")
                        val bytesToSend = motorData.toByteArray()
                        myThreadConnected!!.write(bytesToSend)
                    }
                }
            }
        }
        timer!!.schedule(task, 0, 100) //Частота обновления
    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(listener)
        timer!!.cancel()
    }

    private var listener: SensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        }

        override fun onSensorChanged(event: SensorEvent) {
            when (event.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    var i = 0
                    while (i < 3) {
                        // Задаём верхний порог
                        if (event.values[i] > maxValue) event.values[i] = maxValue.toFloat()
                        if (event.values[i] < -maxValue) event.values[i] = -maxValue.toFloat()

                        // Задаём нижний порог
                        if (event.values[i] > -minValue && event.values[i] < minValue) event.values[i] = 0f
                        else if (event.values[i] >= minValue) event.values[i] = -convert(
                            event.values[i].toDouble(),
                            minValue.toDouble(),
                            maxValue.toDouble(),
                            0.0,
                            255.0
                        ).toFloat()
                        else event.values[i] = -convert(
                            event.values[i].toDouble(),
                            -minValue.toDouble(),
                            -maxValue.toDouble(),
                            0.0,
                            -255.0
                        ).toFloat()

                        valuesAccelGravity[i] = event.values[i].toInt()
                        i++
                    }
                }
            }
        }
    }

    // Преобразование числа
    private fun convert(value: Double, From1: Double, From2: Double, To1: Double, To2: Double): Int {
        return ((value - From1) / (From2 - From1) * (To2 - To1) + To1).toInt()
    }

    override fun onStart() { // Запрос на включение Bluetooth
        super.onStart()

        if (!bluetoothAdapter!!.isEnabled) {
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT)
        }

        setup()
    }

    private fun setup() { // Создание списка сопряжённых Bluetooth-устройств
        val pairedDevices = bluetoothAdapter!!.bondedDevices

        if (pairedDevices.size > 0) { // Если есть сопряжённые устройства

            pairedDeviceArrayList = ArrayList()

            for (device: BluetoothDevice in pairedDevices) { // Добавляем сопряжённые устройства - Имя + MAC-адресс
                pairedDeviceArrayList!!.add(device.name + "\n" + device.address)
            }

            pairedDeviceAdapter = ArrayAdapter(this, layout.simple_list_item_1, pairedDeviceArrayList!!)
            listViewPairedDevice!!.adapter = pairedDeviceAdapter

            listViewPairedDevice!!.onItemClickListener = OnItemClickListener { parent, view, position, id ->
                // Клик по нужному устройству
                listViewPairedDevice!!.visibility = View.GONE // После клика скрываем список

                val itemValue: String = listViewPairedDevice!!.getItemAtPosition(position) as String
                val MAC: String = itemValue.substring(itemValue.length - 17) // Вычленяем MAC-адрес

                val device2: BluetoothDevice = bluetoothAdapter!!.getRemoteDevice(MAC)

                myThreadConnectBTdevice = ThreadConnectBTdevice(device2)
                myThreadConnectBTdevice!!.start() // Запускаем поток для подключения Bluetooth
            }
        }
    }

    override fun onDestroy() { // Закрытие приложения
        super.onDestroy()
        if (myThreadConnectBTdevice != null) myThreadConnectBTdevice!!.cancel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_ENABLE_BT) { // Если разрешили включить Bluetooth, тогда void setup()

            if (resultCode == RESULT_OK) {
                setup()
            } else { // Если не разрешили, тогда закрываем приложение

                Toast.makeText(this, "BlueTooth не включён", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private inner class ThreadConnectBTdevice(device: BluetoothDevice) : Thread() {
        // Поток для коннекта с Bluetooth
        private var bluetoothSocket: BluetoothSocket? = null

        init {
            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(myUUID)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        override fun run() { // Коннект
            var success = false
            val bluetoothSocket = bluetoothSocket!!

            try {
                bluetoothSocket.connect()
                success = true
            } catch (e: IOException) {
                e.printStackTrace()

                runOnUiThread {
                    Toast.makeText(
                        this@MainActivity2017OldVersion,
                        "Нет коннекта, проверьте Bluetooth-устройство с которым хотите соединиться!",
                        Toast.LENGTH_LONG
                    ).show()
                    listViewPairedDevice!!.visibility = View.VISIBLE
                }

                try {
                    bluetoothSocket.close()
                } catch (e1: IOException) {
                    e1.printStackTrace()
                }
            }

            if (success) {  // Если законнектились, тогда открываем панель с кнопками и запускаем поток приёма и отправки данных
                myThreadConnected = ThreadConnected(bluetoothSocket)
                myThreadConnected!!.start() // запуск потока приёма и отправки данных
            }
        }

        fun cancel() {
            Toast.makeText(applicationContext, "Close - BluetoothSocket", Toast.LENGTH_LONG).show()

            try {
                bluetoothSocket!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private inner class ThreadConnected(socket: BluetoothSocket) : Thread() {
        // Поток - приём и отправка данных
        private val connectedOutputStream: OutputStream?

        init {
            var out: OutputStream? = null

            try {
                out = socket.outputStream
            } catch (e: IOException) {
                e.printStackTrace()
            }

            connectedOutputStream = out
        }

        override fun run() { // Приём данных
        }

        fun write(buffer: ByteArray?) {
            try {
                connectedOutputStream!!.write(buffer)
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
        }
    }

    companion object {
        private val REQUEST_ENABLE_BT = 1
    }
}