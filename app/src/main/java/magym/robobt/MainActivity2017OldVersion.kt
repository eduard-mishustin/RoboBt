package magym.robobt

import android.R.layout
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.Timer
import java.util.TimerTask
import java.util.UUID
import kotlin.math.abs
import kotlin.math.max

// Версия приложения с 2017 года с оригинальными комментариями (автосмигрирована с Java)
@SuppressLint("MissingPermission")
class MainActivity2017OldVersion : ComponentActivity(), View.OnClickListener {
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var pairedDeviceArrayList: ArrayList<String>? = null
    private var pairedDeviceAdapter: ArrayAdapter<String>? = null
    private var myUUID: UUID? = null
    private var myThreadConnectBTdevice: ThreadConnectBTdevice? = null
    private var myThreadConnected: ThreadConnected? = null
    private val sb = StringBuilder()
    //    private var client: GoogleApiClient? = null
    private var listViewPairedDevice: ListView? = null

    private var textInfo: TextView? = null
    private var textView: TextView? = null
    private var textViewRGB: TextView? = null
    private var butPanel: RelativeLayout? = null

    private var seekBarR: SeekBar? = null
    private var seekBarG: SeekBar? = null
    private var seekBarB: SeekBar? = null
    private var textViewR: TextView? = null
    private var textViewG: TextView? = null
    private var textViewB: TextView? = null
    private var info: Button? = null
    private var back: Button? = null
    private var areaLay: LinearLayout? = null

    private var buttonA: Button? = null
    private var buttonB: Button? = null
    private var buttonC: Button? = null
    private var buttonD: Button? = null

    private var sensorManager: SensorManager? = null
    private var sensorAccel: Sensor? = null
    private var timer: Timer? = null
    private val valuesAccelGravity = IntArray(3) // Массив значений скорости моторов
    private val maxValue = 4
    private val minValue = 1 // Максимальный и минимальны порог данных с акс (0 - 9.8)
    private var textViewX: TextView? = null
    private var textViewY: TextView? = null
    private var textViewLeftMotor: TextView? = null
    private var textViewRightMotor: TextView? = null
    private var temp: TextView? = null
    private var seekBarX: SeekBar? = null
    private var seekBarY: SeekBar? = null
    private var seekBarLeftMotor: SeekBar? = null
    private var seekBarRightMotor: SeekBar? = null
    private var buttonControlLay: RelativeLayout? = null
    private var accelerometrControlLay: RelativeLayout? = null
    private var musicLay: TableLayout? = null
    private var buttonControl: Button? = null
    private var accelerometrControl: Button? = null
    private var music: Button? = null
    private var musicBack: Button? = null
    private var button11: Button? = null
    private var button12: Button? = null
    private var button13: Button? = null
    private var button14: Button? = null
    private var button15: Button? = null
    private var button16: Button? = null
    private var button17: Button? = null
    private var button18: Button? = null
    private var button19: Button? = null
    private var button21: Button? = null
    private var button22: Button? = null
    private var button23: Button? = null
    private var button24: Button? = null
    private var button25: Button? = null
    private var button26: Button? = null
    private var button27: Button? = null
    private var button28: Button? = null
    private var button29: Button? = null
    private var button31: Button? = null
    private var button32: Button? = null
    private var button33: Button? = null
    private var button34: Button? = null
    private var button35: Button? = null
    private var button36: Button? = null
    private var button37: Button? = null
    private var button38: Button? = null
    private var button39: Button? = null
    private var button41: Button? = null
    private var button42: Button? = null
    private var button43: Button? = null
    private var button44: Button? = null
    private var button45: Button? = null
    private var button46: Button? = null
    private var button47: Button? = null
    private var button48: Button? = null
    private var button49: Button? = null
    private var button51: Button? = null
    private var button52: Button? = null
    private var button53: Button? = null
    private var button54: Button? = null
    private var button55: Button? = null
    private var button56: Button? = null
    private var button57: Button? = null
    private var button58: Button? = null
    private var button59: Button? = null
    private var button61: Button? = null
    private var button62: Button? = null
    private var button63: Button? = null
    private var button64: Button? = null
    private var button65: Button? = null
    private var button66: Button? = null
    private var button67: Button? = null
    private var button68: Button? = null
    private var button69: Button? = null
    private var servoUp: Button? = null
    private var servoDown: Button? = null
    private var towerLeft: Button? = null
    private var towerRight: Button? = null

    private var control = true // Тип управления

    //    var imageView: ImageView? = null

    var servo: Int = 90

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val res = resources

        val UUID_STRING_WELL_KNOWN_SPP = "00001101-0000-1000-8000-00805F9B34FB"

        listViewPairedDevice = findViewById<View>(R.id.pairedlist) as ListView
        butPanel = findViewById<View>(R.id.butPanel) as RelativeLayout

        textInfo = findViewById<View>(R.id.textInfo) as TextView
        textView = findViewById<View>(R.id.textView) as TextView
        textViewRGB = findViewById<View>(R.id.textViewRGB) as TextView
        buttonA = findViewById<View>(R.id.buttonA) as Button
        buttonB = findViewById<View>(R.id.buttonB) as Button
        buttonC = findViewById<View>(R.id.buttonC) as Button
        buttonD = findViewById<View>(R.id.buttonD) as Button

        seekBarR = findViewById<View>(R.id.seekBarR) as SeekBar
        seekBarG = findViewById<View>(R.id.seekBarG) as SeekBar
        seekBarB = findViewById<View>(R.id.seekBarB) as SeekBar
        seekBarR!!.setOnSeekBarChangeListener(seekBarChangeListener)
        seekBarG!!.setOnSeekBarChangeListener(seekBarChangeListener)
        seekBarB!!.setOnSeekBarChangeListener(seekBarChangeListener)
        textViewR = findViewById<View>(R.id.textViewR) as TextView
        textViewG = findViewById<View>(R.id.textViewG) as TextView
        textViewB = findViewById<View>(R.id.textViewB) as TextView
        temp = findViewById<View>(R.id.temp) as TextView

        info = findViewById<View>(R.id.info) as Button
        back = findViewById<View>(R.id.back) as Button
        info!!.setOnClickListener(onClickListener)
        back!!.setOnClickListener(onClickListener)
        areaLay = findViewById<View>(R.id.areaLay) as LinearLayout
        musicLay = findViewById<View>(R.id.musicLay) as TableLayout

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensorAccel = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        seekBarX = findViewById<View>(R.id.seekBarX) as SeekBar
        seekBarY = findViewById<View>(R.id.seekBarY) as SeekBar
        seekBarLeftMotor = findViewById<View>(R.id.seekBarLeftMotor) as SeekBar
        seekBarRightMotor = findViewById<View>(R.id.seekBarRightMotor) as SeekBar
        seekBarX!!.setOnSeekBarChangeListener(seekBarChangeListener)
        seekBarY!!.setOnSeekBarChangeListener(seekBarChangeListener)
        seekBarLeftMotor!!.setOnSeekBarChangeListener(seekBarChangeListener)
        seekBarRightMotor!!.setOnSeekBarChangeListener(seekBarChangeListener)

        textViewX = findViewById<View>(R.id.textViewX) as TextView
        textViewY = findViewById<View>(R.id.textViewY) as TextView
        textViewLeftMotor = findViewById<View>(R.id.textViewLeftMotor) as TextView
        textViewRightMotor = findViewById<View>(R.id.textViewRightMotor) as TextView

        buttonControlLay = findViewById<View>(R.id.buttonControlLay) as RelativeLayout
        accelerometrControlLay = findViewById<View>(R.id.accelerometrControlLay) as RelativeLayout
        buttonControl = findViewById<View>(R.id.buttonControl) as Button
        accelerometrControl = findViewById<View>(R.id.accelerometrControl) as Button
        buttonControl!!.setOnClickListener(onClickListener)
        accelerometrControl!!.setOnClickListener(onClickListener)
        music = findViewById<View>(R.id.music) as Button
        musicBack = findViewById<View>(R.id.musicBack) as Button
        music!!.setOnClickListener(onClickListener)
        musicBack!!.setOnClickListener(onClickListener)

        button11 = findViewById<View>(R.id.button11) as Button
        button11!!.setOnClickListener(onClickListener)
        button12 = findViewById<View>(R.id.button12) as Button
        button12!!.setOnClickListener(onClickListener)
        button13 = findViewById<View>(R.id.button13) as Button
        button13!!.setOnClickListener(onClickListener)
        button14 = findViewById<View>(R.id.button14) as Button
        button14!!.setOnClickListener(onClickListener)
        button15 = findViewById<View>(R.id.button15) as Button
        button15!!.setOnClickListener(onClickListener)
        button16 = findViewById<View>(R.id.button16) as Button
        button16!!.setOnClickListener(onClickListener)
        button17 = findViewById<View>(R.id.button17) as Button
        button17!!.setOnClickListener(onClickListener)
        button18 = findViewById<View>(R.id.button18) as Button
        button18!!.setOnClickListener(onClickListener)
        button19 = findViewById<View>(R.id.button19) as Button
        button19!!.setOnClickListener(onClickListener)

        button21 = findViewById<View>(R.id.button21) as Button
        button21!!.setOnClickListener(onClickListener)
        button22 = findViewById<View>(R.id.button22) as Button
        button22!!.setOnClickListener(onClickListener)
        button23 = findViewById<View>(R.id.button23) as Button
        button23!!.setOnClickListener(onClickListener)
        button24 = findViewById<View>(R.id.button24) as Button
        button24!!.setOnClickListener(onClickListener)
        button25 = findViewById<View>(R.id.button25) as Button
        button25!!.setOnClickListener(onClickListener)
        button26 = findViewById<View>(R.id.button26) as Button
        button26!!.setOnClickListener(onClickListener)
        button27 = findViewById<View>(R.id.button17) as Button
        button27!!.setOnClickListener(onClickListener)
        button28 = findViewById<View>(R.id.button18) as Button
        button28!!.setOnClickListener(onClickListener)
        button29 = findViewById<View>(R.id.button19) as Button
        button29!!.setOnClickListener(onClickListener)

        button31 = findViewById<View>(R.id.button31) as Button
        button31!!.setOnClickListener(onClickListener)
        button32 = findViewById<View>(R.id.button32) as Button
        button32!!.setOnClickListener(onClickListener)
        button33 = findViewById<View>(R.id.button33) as Button
        button33!!.setOnClickListener(onClickListener)
        button34 = findViewById<View>(R.id.button34) as Button
        button34!!.setOnClickListener(onClickListener)
        button35 = findViewById<View>(R.id.button35) as Button
        button35!!.setOnClickListener(onClickListener)
        button36 = findViewById<View>(R.id.button36) as Button
        button36!!.setOnClickListener(onClickListener)
        button37 = findViewById<View>(R.id.button17) as Button
        button37!!.setOnClickListener(onClickListener)
        button38 = findViewById<View>(R.id.button18) as Button
        button38!!.setOnClickListener(onClickListener)
        button39 = findViewById<View>(R.id.button19) as Button
        button39!!.setOnClickListener(onClickListener)

        button41 = findViewById<View>(R.id.button41) as Button
        button41!!.setOnClickListener(onClickListener)
        button42 = findViewById<View>(R.id.button42) as Button
        button42!!.setOnClickListener(onClickListener)
        button43 = findViewById<View>(R.id.button43) as Button
        button43!!.setOnClickListener(onClickListener)
        button44 = findViewById<View>(R.id.button44) as Button
        button44!!.setOnClickListener(onClickListener)
        button45 = findViewById<View>(R.id.button45) as Button
        button45!!.setOnClickListener(onClickListener)
        button46 = findViewById<View>(R.id.button46) as Button
        button46!!.setOnClickListener(onClickListener)
        button47 = findViewById<View>(R.id.button47) as Button
        button47!!.setOnClickListener(onClickListener)
        button48 = findViewById<View>(R.id.button48) as Button
        button48!!.setOnClickListener(onClickListener)
        button49 = findViewById<View>(R.id.button49) as Button
        button49!!.setOnClickListener(onClickListener)

        button51 = findViewById<View>(R.id.button51) as Button
        button51!!.setOnClickListener(onClickListener)
        button52 = findViewById<View>(R.id.button52) as Button
        button52!!.setOnClickListener(onClickListener)
        button53 = findViewById<View>(R.id.button53) as Button
        button53!!.setOnClickListener(onClickListener)
        button54 = findViewById<View>(R.id.button54) as Button
        button54!!.setOnClickListener(onClickListener)
        button55 = findViewById<View>(R.id.button55) as Button
        button55!!.setOnClickListener(onClickListener)
        button56 = findViewById<View>(R.id.button56) as Button
        button56!!.setOnClickListener(onClickListener)
        button57 = findViewById<View>(R.id.button57) as Button
        button57!!.setOnClickListener(onClickListener)
        button58 = findViewById<View>(R.id.button58) as Button
        button58!!.setOnClickListener(onClickListener)
        button59 = findViewById<View>(R.id.button59) as Button
        button59!!.setOnClickListener(onClickListener)

        button61 = findViewById<View>(R.id.button61) as Button
        button61!!.setOnClickListener(onClickListener)
        button62 = findViewById<View>(R.id.button62) as Button
        button62!!.setOnClickListener(onClickListener)
        button63 = findViewById<View>(R.id.button63) as Button
        button63!!.setOnClickListener(onClickListener)
        button64 = findViewById<View>(R.id.button64) as Button
        button64!!.setOnClickListener(onClickListener)
        button65 = findViewById<View>(R.id.button65) as Button
        button65!!.setOnClickListener(onClickListener)
        button66 = findViewById<View>(R.id.button66) as Button
        button66!!.setOnClickListener(onClickListener)
        button67 = findViewById<View>(R.id.button67) as Button
        button67!!.setOnClickListener(onClickListener)
        button68 = findViewById<View>(R.id.button68) as Button
        button68!!.setOnClickListener(onClickListener)
        button69 = findViewById<View>(R.id.button69) as Button
        button69!!.setOnClickListener(onClickListener)

        servoUp = findViewById<View>(R.id.servoUp) as Button
        servoDown = findViewById<View>(R.id.servoDown) as Button
        towerLeft = findViewById<View>(R.id.towerLeft) as Button
        towerRight = findViewById<View>(R.id.towerRight) as Button

        //        imageView = findViewById<View>(R.id.imageView) as ImageView

        buttonA!!.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (myThreadConnected != null) {
                        val bytesToSend = "A".toByteArray()
                        myThreadConnected!!.write(bytesToSend)
                    }
                    textView!!.text = "Нажали кнопку A"
                }

                MotionEvent.ACTION_MOVE -> {}

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if (myThreadConnected != null) {
                        val bytesToSend = "a".toByteArray()
                        myThreadConnected!!.write(bytesToSend)
                    }
                    textView!!.text = "Отпустили кнопку А"
                }
            }
            false
        }

        buttonB!!.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (myThreadConnected != null) {
                        val bytesToSend = "B".toByteArray()
                        myThreadConnected!!.write(bytesToSend)
                    }
                    textView!!.text = "Нажали кнопку B"
                }

                MotionEvent.ACTION_MOVE -> {}

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if (myThreadConnected != null) {
                        val bytesToSend = "b".toByteArray()
                        myThreadConnected!!.write(bytesToSend)
                    }
                    textView!!.text = "Отпустили кнопку B"
                }
            }
            false
        }

        buttonC!!.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (myThreadConnected != null) {
                        val bytesToSend = "C".toByteArray()
                        myThreadConnected!!.write(bytesToSend)
                    }
                    textView!!.text = "Нажали кнопку C"
                }

                MotionEvent.ACTION_MOVE -> {}

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if (myThreadConnected != null) {
                        val bytesToSend = "c".toByteArray()
                        myThreadConnected!!.write(bytesToSend)
                    }
                    textView!!.text = "Отпустили кнопку C"
                }
            }
            false
        }

        buttonD!!.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (myThreadConnected != null) {
                        val bytesToSend = "D".toByteArray()
                        myThreadConnected!!.write(bytesToSend)
                    }
                    textView!!.text = "Нажали кнопку D"
                }

                MotionEvent.ACTION_MOVE -> {}

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if (myThreadConnected != null) {
                        val bytesToSend = "d".toByteArray()
                        myThreadConnected!!.write(bytesToSend)
                    }
                    textView!!.text = "Отпустили кнопку D"
                }
            }
            false
        }

        towerLeft!!.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {}

                MotionEvent.ACTION_MOVE -> if (myThreadConnected != null) {
                    val bytesToSend = "Q".toByteArray()
                    myThreadConnected!!.write(bytesToSend)
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> if (myThreadConnected != null) {
                    val bytesToSend = "q".toByteArray()
                    myThreadConnected!!.write(bytesToSend)
                }
            }
            false
        }

        towerRight!!.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {}

                MotionEvent.ACTION_MOVE -> if (myThreadConnected != null) {
                    val bytesToSend = "E".toByteArray()
                    myThreadConnected!!.write(bytesToSend)
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> if (myThreadConnected != null) {
                    val bytesToSend = "e".toByteArray()
                    myThreadConnected!!.write(bytesToSend)
                }
            }
            false
        }

        servoUp!!.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {}

                MotionEvent.ACTION_MOVE -> {
                    if (servo < 180) servo++
                    val servoData = ("s" + servo + "z").toString()
                    //servoUp.setText(servoData);
                    if (myThreadConnected != null) {
                        val bytesToSend = servoData.toByteArray()
                        myThreadConnected!!.write(bytesToSend)
                    }
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {}
            }
            false
        }

        servoDown!!.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {}

                MotionEvent.ACTION_MOVE -> {
                    if (servo > 0) servo--
                    val servoData = ("s" + servo + "z").toString()
                    //servoUp.setText(servoData);
                    if (myThreadConnected != null) {
                        val bytesToSend = servoData.toByteArray()
                        myThreadConnected!!.write(bytesToSend)
                    }
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {}
            }
            false
        }

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
        //        client = Builder(this).addApi(AppIndex.API).build()
    }

    protected var onClickListener: View.OnClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.info -> {
                info!!.visibility = View.INVISIBLE
                areaLay!!.visibility = View.VISIBLE
                accelerometrControlLay!!.visibility = View.INVISIBLE
                buttonControlLay!!.visibility = View.INVISIBLE
            }

            R.id.back -> {
                areaLay!!.visibility = View.INVISIBLE
                info!!.visibility = View.VISIBLE
                if (control) buttonControlLay!!.visibility = View.VISIBLE
                else accelerometrControlLay!!.visibility = View.VISIBLE
            }

            R.id.buttonControl -> {
                accelerometrControlLay!!.visibility = View.INVISIBLE
                buttonControlLay!!.visibility = View.VISIBLE
                control = true
            }

            R.id.accelerometrControl -> {
                areaLay!!.visibility = View.INVISIBLE
                buttonControlLay!!.visibility = View.INVISIBLE
                accelerometrControlLay!!.visibility = View.VISIBLE
                control = false
            }

            R.id.music -> {
                info!!.visibility = View.INVISIBLE
                accelerometrControlLay!!.visibility = View.INVISIBLE
                buttonControlLay!!.visibility = View.INVISIBLE
                music!!.visibility = View.INVISIBLE
                musicBack!!.visibility = View.VISIBLE
                musicLay!!.visibility = View.VISIBLE
            }

            R.id.musicBack -> {
                areaLay!!.visibility = View.INVISIBLE
                info!!.visibility = View.VISIBLE
                buttonControlLay!!.visibility = View.VISIBLE
                music!!.visibility = View.VISIBLE
                musicBack!!.visibility = View.INVISIBLE
                musicLay!!.visibility = View.INVISIBLE
            }

            R.id.button11 -> myThreadConnected!!.write("t329z".toByteArray())
            R.id.button12 -> myThreadConnected!!.write("t349z".toByteArray())
            R.id.button13 -> myThreadConnected!!.write("t370z".toByteArray())
            R.id.button14 -> myThreadConnected!!.write("t392z".toByteArray())
            R.id.button15 -> myThreadConnected!!.write("t415z".toByteArray())
            R.id.button16 -> myThreadConnected!!.write("t440z".toByteArray())
            R.id.button17 -> myThreadConnected!!.write("t446z".toByteArray())
            R.id.button18 -> myThreadConnected!!.write("t494z".toByteArray())
            R.id.button19 -> myThreadConnected!!.write("t523z".toByteArray())
            R.id.button21 -> myThreadConnected!!.write("t247z".toByteArray())
            R.id.button22 -> myThreadConnected!!.write("t262z".toByteArray())
            R.id.button23 -> myThreadConnected!!.write("t277z".toByteArray())
            R.id.button24 -> myThreadConnected!!.write("t294z".toByteArray())
            R.id.button25 -> myThreadConnected!!.write("t311z".toByteArray())
            R.id.button26 -> myThreadConnected!!.write("t329z".toByteArray())
            R.id.button27 -> myThreadConnected!!.write("t349z".toByteArray())
            R.id.button28 -> myThreadConnected!!.write("t370z".toByteArray())
            R.id.button29 -> myThreadConnected!!.write("t392z".toByteArray())
            R.id.button31 -> myThreadConnected!!.write("t196z".toByteArray())
            R.id.button32 -> myThreadConnected!!.write("t208z".toByteArray())
            R.id.button33 -> myThreadConnected!!.write("t220z".toByteArray())
            R.id.button34 -> myThreadConnected!!.write("t233z".toByteArray())
            R.id.button35 -> myThreadConnected!!.write("t247z".toByteArray())
            R.id.button36 -> myThreadConnected!!.write("t262z".toByteArray())
            R.id.button37 -> myThreadConnected!!.write("t277z".toByteArray())
            R.id.button38 -> myThreadConnected!!.write("t294z".toByteArray())
            R.id.button39 -> myThreadConnected!!.write("t311z".toByteArray())
            R.id.button41 -> myThreadConnected!!.write("t147z".toByteArray())
            R.id.button42 -> myThreadConnected!!.write("t156z".toByteArray())
            R.id.button43 -> myThreadConnected!!.write("t165z".toByteArray())
            R.id.button44 -> myThreadConnected!!.write("t175z".toByteArray())
            R.id.button45 -> myThreadConnected!!.write("t185z".toByteArray())
            R.id.button46 -> myThreadConnected!!.write("t196z".toByteArray())
            R.id.button47 -> myThreadConnected!!.write("t208z".toByteArray())
            R.id.button48 -> myThreadConnected!!.write("t220z".toByteArray())
            R.id.button49 -> myThreadConnected!!.write("t233z".toByteArray())
            R.id.button51 -> myThreadConnected!!.write("t110z".toByteArray())
            R.id.button52 -> myThreadConnected!!.write("t117z".toByteArray())
            R.id.button53 -> myThreadConnected!!.write("t123z".toByteArray())
            R.id.button54 -> myThreadConnected!!.write("t131z".toByteArray())
            R.id.button55 -> myThreadConnected!!.write("t139z".toByteArray())
            R.id.button56 -> myThreadConnected!!.write("t147z".toByteArray())
            R.id.button57 -> myThreadConnected!!.write("t156z".toByteArray())
            R.id.button58 -> myThreadConnected!!.write("t165z".toByteArray())
            R.id.button59 -> myThreadConnected!!.write("t175z".toByteArray())
            R.id.button61 -> myThreadConnected!!.write("t82z".toByteArray())
            R.id.button62 -> myThreadConnected!!.write("t87z".toByteArray())
            R.id.button63 -> myThreadConnected!!.write("t92z".toByteArray())
            R.id.button64 -> myThreadConnected!!.write("t98z".toByteArray())
            R.id.button65 -> myThreadConnected!!.write("t104z".toByteArray())
            R.id.button66 -> myThreadConnected!!.write("t110z".toByteArray())
            R.id.button67 -> myThreadConnected!!.write("t117z".toByteArray())
            R.id.button68 -> myThreadConnected!!.write("t123z".toByteArray())
            R.id.button69 -> myThreadConnected!!.write("t131z".toByteArray())
        }
    }

    private val seekBarChangeListener: OnSeekBarChangeListener = object : OnSeekBarChangeListener {
        override fun onProgressChanged(
            seekBar: SeekBar, progress: Int,
            fromUser: Boolean
        ) {
            val myColor = Color.rgb(seekBarR!!.progress, seekBarG!!.progress, seekBarB!!.progress)
            textView!!.setTextColor(myColor)
            textViewRGB!!.setTextColor(myColor)

            textViewR!!.text = "Красный " + seekBarR!!.progress
            textViewG!!.text = "Зелёный " + seekBarG!!.progress
            textViewB!!.text = "Синий " + seekBarB!!.progress

            // Формируем данные типа -1-1-1- or -11-11-11- or -111-111-111- or ...
            val rgbData =
                ("-" + seekBarR!!.progress + "-" + seekBarG!!.progress + "-" + seekBarB!!.progress + "-").toString()

            if (myThreadConnected != null) {
                val bytesToSend = rgbData.toByteArray()
                myThreadConnected!!.write(bytesToSend)
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {
        }
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
                    if (!control) { // Только если управление акселерометром активировано
                        seekBarX!!.progress = valuesAccelGravity.get(0) + 255
                        seekBarY!!.progress = valuesAccelGravity.get(1) + 255
                        textViewX!!.text = "X: " + valuesAccelGravity.get(0)
                        textViewY!!.text = "Y: " + valuesAccelGravity.get(1)

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

                        seekBarLeftMotor!!.progress = leftMotor + 255
                        seekBarRightMotor!!.progress = rightMotor + 255
                        textViewLeftMotor!!.text = "leftMotor: $leftMotor"
                        textViewRightMotor!!.text = "rightMotor: $rightMotor"
                        setGradient(leftMotor, rightMotor)

                        val motorData = ("m" + leftMotor + "z" + rightMotor + "z").toString()

                        if (myThreadConnected != null) {
                            val bytesToSend = motorData.toByteArray()
                            myThreadConnected!!.write(bytesToSend)
                        }
                    }
                }
            }
        }
        timer!!.schedule(task, 0, 100) //Частота обновления
    }

    private fun setGradient(left: Int, right: Int) {
        val leftColor: Int
        val rightColor: Int

        if (left > 0) leftColor = Color.rgb(255, 255 - left, 255 - left)
        else leftColor = Color.rgb(255 + left, 255 + left, 255)
        if (right > 0) rightColor = Color.rgb(255, 255 - right, 255 - right)
        else rightColor = Color.rgb(255 + right, 255 + right, 255)

        val gradient = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(rightColor, leftColor)
        )
        gradient.shape = GradientDrawable.RECTANGLE
        gradient.gradientType = GradientDrawable.LINEAR_GRADIENT
        //        imageView!!.setImageDrawable(gradient)
    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(listener)
        timer!!.cancel()
    }

    protected var listener: SensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        }

        override fun onSensorChanged(event: SensorEvent) {
            if (!control) {
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
                                150.0,
                                255.0
                            ).toFloat()
                            else event.values[i] = -convert(
                                event.values[i].toDouble(),
                                -minValue.toDouble(),
                                -maxValue.toDouble(),
                                -150.0,
                                -255.0
                            ).toFloat()

                            valuesAccelGravity[i] = event.values[i].toInt()
                            i++
                        }
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

            listViewPairedDevice!!.onItemClickListener = object : OnItemClickListener {
                // Клик по нужному устройству
                override fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                    listViewPairedDevice!!.visibility = View.GONE // После клика скрываем список

                    val itemValue: String = listViewPairedDevice!!.getItemAtPosition(position) as String
                    val MAC: String = itemValue.substring(itemValue.length - 17) // Вычленяем MAC-адрес

                    val device2: BluetoothDevice = bluetoothAdapter!!.getRemoteDevice(MAC)

                    myThreadConnectBTdevice = ThreadConnectBTdevice(device2)
                    myThreadConnectBTdevice!!.start() // Запускаем поток для подключения Bluetooth
                }
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

            try {
                bluetoothSocket!!.connect()
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
                    bluetoothSocket!!.close()
                } catch (e1: IOException) {
                    e1.printStackTrace()
                }
            }

            if (success) {  // Если законнектились, тогда открываем панель с кнопками и запускаем поток приёма и отправки данных

                runOnUiThread {
                    butPanel!!.visibility = View.VISIBLE // открываем панель с кнопками
                }

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
    } // END ThreadConnectBTdevice:

    private inner class ThreadConnected(socket: BluetoothSocket?) : Thread() {
        // Поток - приём и отправка данных
        private val connectedInputStream: InputStream?
        private val connectedOutputStream: OutputStream?

        private var sbprint: String? = null

        init {
            var `in`: InputStream? = null
            var out: OutputStream? = null

            try {
                `in` = socket!!.inputStream
                out = socket.outputStream
            } catch (e: IOException) {
                e.printStackTrace()
            }

            connectedInputStream = `in`
            connectedOutputStream = out
        }

        override fun run() { // Приём данных

            while (true) {
                try {
                    val buffer = ByteArray(1)
                    val bytes = connectedInputStream!!.read(buffer)
                    val strIncom = String(buffer, 0, bytes)
                    sb.append(strIncom) // собираем символы в строку
                    val endOfLineIndex = sb.indexOf("\r\n") // определяем конец строки

                    if (endOfLineIndex > 0) {
                        sbprint = sb.substring(0, endOfLineIndex)
                        sb.delete(0, sb.length)

                        runOnUiThread(Runnable

                        // Вывод данных
                        {
                            temp!!.text = sbprint
                            /*switch (sbprint) {

                                    case "D10 ON":
                                        d10.setText(sbprint);
                                        break;

                                    default:
                                        break;
                                }*/
                        })
                    }
                } catch (e: IOException) {
                    break
                }
            }
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