package magym.robobt;

//https://istarik.ru/blog/android/50.html

import static android.R.layout.simple_list_item_1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

@SuppressLint("MissingPermission")
public class MainActivity2017OldVersion extends Activity implements View.OnClickListener {
    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter bluetoothAdapter;
    private ArrayList<String> pairedDeviceArrayList;
    private ArrayAdapter<String> pairedDeviceAdapter;
    private UUID myUUID;
    private ThreadConnectBTdevice myThreadConnectBTdevice;
    private ThreadConnected myThreadConnected;
    private StringBuilder sb = new StringBuilder();
    private ListView listViewPairedDevice;

    private TextView textInfo, textView, textViewRGB;
    private RelativeLayout butPanel;

    private SeekBar seekBarR, seekBarG, seekBarB;
    private TextView textViewR, textViewG, textViewB;
    private Button info, back;
    private LinearLayout areaLay;

    private Button buttonA, buttonB, buttonC, buttonD;

    private SensorManager sensorManager;
    private Sensor sensorAccel;
    private Timer timer;
    private int[] valuesAccelGravity = new int[3];// Массив значений скорости моторов
    private int maxValue = 4, minValue = 1;// Максимальный и минимальны порог данных с акс (0 - 9.8)
    private TextView textViewX, textViewY, textViewLeftMotor, textViewRightMotor, temp;
    private SeekBar seekBarX, seekBarY, seekBarLeftMotor, seekBarRightMotor;
    private RelativeLayout buttonControlLay, accelerometrControlLay;
    private TableLayout musicLay;
    private Button buttonControl, accelerometrControl, music, musicBack;
    private Button button11, button12, button13, button14, button15, button16, button17, button18, button19, button21, button22, button23, button24, button25, button26, button27, button28, button29, button31, button32, button33, button34, button35, button36, button37, button38, button39, button41, button42, button43, button44, button45, button46, button47, button48, button49, button51, button52, button53, button54, button55, button56, button57, button58, button59, button61, button62, button63, button64, button65, button66, button67, button68, button69;
    private Button servoUp, servoDown, towerLeft, towerRight;

    private boolean control = true;// Тип управления

    ImageView imageView;

    int servo = 90;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Resources res = getResources();

        final String UUID_STRING_WELL_KNOWN_SPP = "00001101-0000-1000-8000-00805F9B34FB";

        listViewPairedDevice = (ListView) findViewById(R.id.pairedlist);
        butPanel = (RelativeLayout) findViewById(R.id.butPanel);

        textInfo = (TextView) findViewById(R.id.textInfo);
        textView = (TextView) findViewById(R.id.textView);
        textViewRGB = (TextView) findViewById(R.id.textViewRGB);
        buttonA = (Button) findViewById(R.id.buttonA);
        buttonB = (Button) findViewById(R.id.buttonB);
        buttonC = (Button) findViewById(R.id.buttonC);
        buttonD = (Button) findViewById(R.id.buttonD);

        seekBarR = (SeekBar) findViewById(R.id.seekBarR);
        seekBarG = (SeekBar) findViewById(R.id.seekBarG);
        seekBarB = (SeekBar) findViewById(R.id.seekBarB);
        seekBarR.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBarG.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBarB.setOnSeekBarChangeListener(seekBarChangeListener);
        textViewR = (TextView) findViewById(R.id.textViewR);
        textViewG = (TextView) findViewById(R.id.textViewG);
        textViewB = (TextView) findViewById(R.id.textViewB);
        temp = (TextView) findViewById(R.id.temp);

        info = (Button) findViewById(R.id.info);
        back = (Button) findViewById(R.id.back);
        info.setOnClickListener(onClickListener);
        back.setOnClickListener(onClickListener);
        areaLay = (LinearLayout) findViewById(R.id.areaLay);
        musicLay = (TableLayout) findViewById(R.id.musicLay);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        seekBarX = (SeekBar) findViewById(R.id.seekBarX);
        seekBarY = (SeekBar) findViewById(R.id.seekBarY);
        seekBarLeftMotor = (SeekBar) findViewById(R.id.seekBarLeftMotor);
        seekBarRightMotor = (SeekBar) findViewById(R.id.seekBarRightMotor);
        seekBarX.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBarY.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBarLeftMotor.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBarRightMotor.setOnSeekBarChangeListener(seekBarChangeListener);

        textViewX = (TextView) findViewById(R.id.textViewX);
        textViewY = (TextView) findViewById(R.id.textViewY);
        textViewLeftMotor = (TextView) findViewById(R.id.textViewLeftMotor);
        textViewRightMotor = (TextView) findViewById(R.id.textViewRightMotor);

        buttonControlLay = (RelativeLayout) findViewById(R.id.buttonControlLay);
        accelerometrControlLay = (RelativeLayout) findViewById(R.id.accelerometrControlLay);
        buttonControl = (Button) findViewById(R.id.buttonControl);
        accelerometrControl = (Button) findViewById(R.id.accelerometrControl);
        buttonControl.setOnClickListener(onClickListener);
        accelerometrControl.setOnClickListener(onClickListener);
        music = (Button) findViewById(R.id.music);
        musicBack = (Button) findViewById(R.id.musicBack);
        music.setOnClickListener(onClickListener);
        musicBack.setOnClickListener(onClickListener);

        button11 = (Button) findViewById(R.id.button11);
        button11.setOnClickListener(onClickListener);
        button12 = (Button) findViewById(R.id.button12);
        button12.setOnClickListener(onClickListener);
        button13 = (Button) findViewById(R.id.button13);
        button13.setOnClickListener(onClickListener);
        button14 = (Button) findViewById(R.id.button14);
        button14.setOnClickListener(onClickListener);
        button15 = (Button) findViewById(R.id.button15);
        button15.setOnClickListener(onClickListener);
        button16 = (Button) findViewById(R.id.button16);
        button16.setOnClickListener(onClickListener);
        button17 = (Button) findViewById(R.id.button17);
        button17.setOnClickListener(onClickListener);
        button18 = (Button) findViewById(R.id.button18);
        button18.setOnClickListener(onClickListener);
        button19 = (Button) findViewById(R.id.button19);
        button19.setOnClickListener(onClickListener);

        button21 = (Button) findViewById(R.id.button21);
        button21.setOnClickListener(onClickListener);
        button22 = (Button) findViewById(R.id.button22);
        button22.setOnClickListener(onClickListener);
        button23 = (Button) findViewById(R.id.button23);
        button23.setOnClickListener(onClickListener);
        button24 = (Button) findViewById(R.id.button24);
        button24.setOnClickListener(onClickListener);
        button25 = (Button) findViewById(R.id.button25);
        button25.setOnClickListener(onClickListener);
        button26 = (Button) findViewById(R.id.button26);
        button26.setOnClickListener(onClickListener);
        button27 = (Button) findViewById(R.id.button17);
        button27.setOnClickListener(onClickListener);
        button28 = (Button) findViewById(R.id.button18);
        button28.setOnClickListener(onClickListener);
        button29 = (Button) findViewById(R.id.button19);
        button29.setOnClickListener(onClickListener);

        button31 = (Button) findViewById(R.id.button31);
        button31.setOnClickListener(onClickListener);
        button32 = (Button) findViewById(R.id.button32);
        button32.setOnClickListener(onClickListener);
        button33 = (Button) findViewById(R.id.button33);
        button33.setOnClickListener(onClickListener);
        button34 = (Button) findViewById(R.id.button34);
        button34.setOnClickListener(onClickListener);
        button35 = (Button) findViewById(R.id.button35);
        button35.setOnClickListener(onClickListener);
        button36 = (Button) findViewById(R.id.button36);
        button36.setOnClickListener(onClickListener);
        button37 = (Button) findViewById(R.id.button17);
        button37.setOnClickListener(onClickListener);
        button38 = (Button) findViewById(R.id.button18);
        button38.setOnClickListener(onClickListener);
        button39 = (Button) findViewById(R.id.button19);
        button39.setOnClickListener(onClickListener);

        button41 = (Button) findViewById(R.id.button41);
        button41.setOnClickListener(onClickListener);
        button42 = (Button) findViewById(R.id.button42);
        button42.setOnClickListener(onClickListener);
        button43 = (Button) findViewById(R.id.button43);
        button43.setOnClickListener(onClickListener);
        button44 = (Button) findViewById(R.id.button44);
        button44.setOnClickListener(onClickListener);
        button45 = (Button) findViewById(R.id.button45);
        button45.setOnClickListener(onClickListener);
        button46 = (Button) findViewById(R.id.button46);
        button46.setOnClickListener(onClickListener);
        button47 = (Button) findViewById(R.id.button47);
        button47.setOnClickListener(onClickListener);
        button48 = (Button) findViewById(R.id.button48);
        button48.setOnClickListener(onClickListener);
        button49 = (Button) findViewById(R.id.button49);
        button49.setOnClickListener(onClickListener);

        button51 = (Button) findViewById(R.id.button51);
        button51.setOnClickListener(onClickListener);
        button52 = (Button) findViewById(R.id.button52);
        button52.setOnClickListener(onClickListener);
        button53 = (Button) findViewById(R.id.button53);
        button53.setOnClickListener(onClickListener);
        button54 = (Button) findViewById(R.id.button54);
        button54.setOnClickListener(onClickListener);
        button55 = (Button) findViewById(R.id.button55);
        button55.setOnClickListener(onClickListener);
        button56 = (Button) findViewById(R.id.button56);
        button56.setOnClickListener(onClickListener);
        button57 = (Button) findViewById(R.id.button57);
        button57.setOnClickListener(onClickListener);
        button58 = (Button) findViewById(R.id.button58);
        button58.setOnClickListener(onClickListener);
        button59 = (Button) findViewById(R.id.button59);
        button59.setOnClickListener(onClickListener);

        button61 = (Button) findViewById(R.id.button61);
        button61.setOnClickListener(onClickListener);
        button62 = (Button) findViewById(R.id.button62);
        button62.setOnClickListener(onClickListener);
        button63 = (Button) findViewById(R.id.button63);
        button63.setOnClickListener(onClickListener);
        button64 = (Button) findViewById(R.id.button64);
        button64.setOnClickListener(onClickListener);
        button65 = (Button) findViewById(R.id.button65);
        button65.setOnClickListener(onClickListener);
        button66 = (Button) findViewById(R.id.button66);
        button66.setOnClickListener(onClickListener);
        button67 = (Button) findViewById(R.id.button67);
        button67.setOnClickListener(onClickListener);
        button68 = (Button) findViewById(R.id.button68);
        button68.setOnClickListener(onClickListener);
        button69 = (Button) findViewById(R.id.button69);
        button69.setOnClickListener(onClickListener);

        servoUp = (Button) findViewById(R.id.servoUp);
        servoDown = (Button) findViewById(R.id.servoDown);
        towerLeft = (Button) findViewById(R.id.towerLeft);
        towerRight = (Button) findViewById(R.id.towerRight);

        buttonA.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        if (myThreadConnected != null) {
                            byte[] bytesToSend = "A".getBytes();
                            myThreadConnected.write(bytesToSend);
                        }
                        textView.setText("Нажали кнопку A");
                        break;
                    case MotionEvent.ACTION_MOVE: // движение
                        break;
                    case MotionEvent.ACTION_UP: // отпускание
                    case MotionEvent.ACTION_CANCEL:
                        if (myThreadConnected != null) {
                            byte[] bytesToSend = "a".getBytes();
                            myThreadConnected.write(bytesToSend);
                        }
                        textView.setText("Отпустили кнопку А");
                        break;
                }
                return false;
            }
        });

        buttonB.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (myThreadConnected != null) {
                            byte[] bytesToSend = "B".getBytes();
                            myThreadConnected.write(bytesToSend);
                        }
                        textView.setText("Нажали кнопку B");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (myThreadConnected != null) {
                            byte[] bytesToSend = "b".getBytes();
                            myThreadConnected.write(bytesToSend);
                        }
                        textView.setText("Отпустили кнопку B");
                        break;
                }
                return false;
            }
        });

        buttonC.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (myThreadConnected != null) {
                            byte[] bytesToSend = "C".getBytes();
                            myThreadConnected.write(bytesToSend);
                        }
                        textView.setText("Нажали кнопку C");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (myThreadConnected != null) {
                            byte[] bytesToSend = "c".getBytes();
                            myThreadConnected.write(bytesToSend);
                        }
                        textView.setText("Отпустили кнопку C");
                        break;
                }
                return false;
            }
        });

        buttonD.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (myThreadConnected != null) {
                            byte[] bytesToSend = "D".getBytes();
                            myThreadConnected.write(bytesToSend);
                        }
                        textView.setText("Нажали кнопку D");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (myThreadConnected != null) {
                            byte[] bytesToSend = "d".getBytes();
                            myThreadConnected.write(bytesToSend);
                        }
                        textView.setText("Отпустили кнопку D");
                        break;
                }
                return false;
            }
        });

        towerLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        break;
                    case MotionEvent.ACTION_MOVE: // движение
                        if (myThreadConnected != null) {
                            byte[] bytesToSend = "Q".getBytes();
                            myThreadConnected.write(bytesToSend);
                        }
                        break;
                    case MotionEvent.ACTION_UP: // отпускание
                    case MotionEvent.ACTION_CANCEL:
                        if (myThreadConnected != null) {
                            byte[] bytesToSend = "q".getBytes();
                            myThreadConnected.write(bytesToSend);
                        }
                        break;
                }
                return false;
            }
        });

        towerRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        break;
                    case MotionEvent.ACTION_MOVE: // движение
                        if (myThreadConnected != null) {
                            byte[] bytesToSend = "E".getBytes();
                            myThreadConnected.write(bytesToSend);
                        }
                        break;
                    case MotionEvent.ACTION_UP: // отпускание
                    case MotionEvent.ACTION_CANCEL:
                        if (myThreadConnected != null) {
                            byte[] bytesToSend = "e".getBytes();
                            myThreadConnected.write(bytesToSend);
                        }
                        break;
                }
                return false;
            }
        });

        servoUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        break;
                    case MotionEvent.ACTION_MOVE: // движение
                        if (servo < 180)
                            servo++;
                        String servoData = String.valueOf("s" + servo + "z");
                        //servoUp.setText(servoData);
                        if (myThreadConnected != null) {
                            byte[] bytesToSend = servoData.getBytes();
                            myThreadConnected.write(bytesToSend);
                        }
                        break;
                    case MotionEvent.ACTION_UP: // отпускание
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }
                return false;
            }
        });

        servoDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        break;
                    case MotionEvent.ACTION_MOVE: // движение
                        if (servo > 0)
                            servo--;
                        String servoData = String.valueOf("s" + servo + "z");
                        //servoUp.setText(servoData);
                        if (myThreadConnected != null) {
                            byte[] bytesToSend = servoData.getBytes();
                            myThreadConnected.write(bytesToSend);
                        }
                        break;
                    case MotionEvent.ACTION_UP: // отпускание
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }
                return false;
            }
        });

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            Toast.makeText(this, "BLUETOOTH NOT support", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        myUUID = UUID.fromString(UUID_STRING_WELL_KNOWN_SPP);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported on this hardware platform", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        String stInfo = bluetoothAdapter.getName() + " " + bluetoothAdapter.getAddress();
        textInfo.setText(String.format("Это устройство: %s", stInfo));
    }

    protected View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.info:
                    info.setVisibility(View.INVISIBLE);
                    areaLay.setVisibility(View.VISIBLE);
                    accelerometrControlLay.setVisibility(View.INVISIBLE);
                    buttonControlLay.setVisibility(View.INVISIBLE);
                    break;
                case R.id.back:
                    areaLay.setVisibility(View.INVISIBLE);
                    info.setVisibility(View.VISIBLE);
                    if (control)
                        buttonControlLay.setVisibility(View.VISIBLE);
                    else
                        accelerometrControlLay.setVisibility(View.VISIBLE);
                    break;
                case R.id.buttonControl:
                    accelerometrControlLay.setVisibility(View.INVISIBLE);
                    buttonControlLay.setVisibility(View.VISIBLE);
                    control = true;
                    break;
                case R.id.accelerometrControl:
                    areaLay.setVisibility(View.INVISIBLE);
                    buttonControlLay.setVisibility(View.INVISIBLE);
                    accelerometrControlLay.setVisibility(View.VISIBLE);
                    control = false;
                    break;
                case R.id.music:
                    info.setVisibility(View.INVISIBLE);
                    accelerometrControlLay.setVisibility(View.INVISIBLE);
                    buttonControlLay.setVisibility(View.INVISIBLE);
                    music.setVisibility(View.INVISIBLE);
                    musicBack.setVisibility(View.VISIBLE);
                    musicLay.setVisibility(View.VISIBLE);
                    break;
                case R.id.musicBack:
                    areaLay.setVisibility(View.INVISIBLE);
                    info.setVisibility(View.VISIBLE);
                    buttonControlLay.setVisibility(View.VISIBLE);
                    music.setVisibility(View.VISIBLE);
                    musicBack.setVisibility(View.INVISIBLE);
                    musicLay.setVisibility(View.INVISIBLE);
                    break;

                case R.id.button11:
                    myThreadConnected.write("t329z".getBytes());
                    break;
                case R.id.button12:
                    myThreadConnected.write("t349z".getBytes());
                    break;
                case R.id.button13:
                    myThreadConnected.write("t370z".getBytes());
                    break;
                case R.id.button14:
                    myThreadConnected.write("t392z".getBytes());
                    break;
                case R.id.button15:
                    myThreadConnected.write("t415z".getBytes());
                    break;
                case R.id.button16:
                    myThreadConnected.write("t440z".getBytes());
                    break;
                case R.id.button17:
                    myThreadConnected.write("t446z".getBytes());
                    break;
                case R.id.button18:
                    myThreadConnected.write("t494z".getBytes());
                    break;
                case R.id.button19:
                    myThreadConnected.write("t523z".getBytes());
                    break;

                case R.id.button21:
                    myThreadConnected.write("t247z".getBytes());
                    break;
                case R.id.button22:
                    myThreadConnected.write("t262z".getBytes());
                    break;
                case R.id.button23:
                    myThreadConnected.write("t277z".getBytes());
                    break;
                case R.id.button24:
                    myThreadConnected.write("t294z".getBytes());
                    break;
                case R.id.button25:
                    myThreadConnected.write("t311z".getBytes());
                    break;
                case R.id.button26:
                    myThreadConnected.write("t329z".getBytes());
                    break;
                case R.id.button27:
                    myThreadConnected.write("t349z".getBytes());
                    break;
                case R.id.button28:
                    myThreadConnected.write("t370z".getBytes());
                    break;
                case R.id.button29:
                    myThreadConnected.write("t392z".getBytes());
                    break;

                case R.id.button31:
                    myThreadConnected.write("t196z".getBytes());
                    break;
                case R.id.button32:
                    myThreadConnected.write("t208z".getBytes());
                    break;
                case R.id.button33:
                    myThreadConnected.write("t220z".getBytes());
                    break;
                case R.id.button34:
                    myThreadConnected.write("t233z".getBytes());
                    break;
                case R.id.button35:
                    myThreadConnected.write("t247z".getBytes());
                    break;
                case R.id.button36:
                    myThreadConnected.write("t262z".getBytes());
                    break;
                case R.id.button37:
                    myThreadConnected.write("t277z".getBytes());
                    break;
                case R.id.button38:
                    myThreadConnected.write("t294z".getBytes());
                    break;
                case R.id.button39:
                    myThreadConnected.write("t311z".getBytes());
                    break;

                case R.id.button41:
                    myThreadConnected.write("t147z".getBytes());
                    break;
                case R.id.button42:
                    myThreadConnected.write("t156z".getBytes());
                    break;
                case R.id.button43:
                    myThreadConnected.write("t165z".getBytes());
                    break;
                case R.id.button44:
                    myThreadConnected.write("t175z".getBytes());
                    break;
                case R.id.button45:
                    myThreadConnected.write("t185z".getBytes());
                    break;
                case R.id.button46:
                    myThreadConnected.write("t196z".getBytes());
                    break;
                case R.id.button47:
                    myThreadConnected.write("t208z".getBytes());
                    break;
                case R.id.button48:
                    myThreadConnected.write("t220z".getBytes());
                    break;
                case R.id.button49:
                    myThreadConnected.write("t233z".getBytes());
                    break;

                case R.id.button51:
                    myThreadConnected.write("t110z".getBytes());
                    break;
                case R.id.button52:
                    myThreadConnected.write("t117z".getBytes());
                    break;
                case R.id.button53:
                    myThreadConnected.write("t123z".getBytes());
                    break;
                case R.id.button54:
                    myThreadConnected.write("t131z".getBytes());
                    break;
                case R.id.button55:
                    myThreadConnected.write("t139z".getBytes());
                    break;
                case R.id.button56:
                    myThreadConnected.write("t147z".getBytes());
                    break;
                case R.id.button57:
                    myThreadConnected.write("t156z".getBytes());
                    break;
                case R.id.button58:
                    myThreadConnected.write("t165z".getBytes());
                    break;
                case R.id.button59:
                    myThreadConnected.write("t175z".getBytes());
                    break;

                case R.id.button61:
                    myThreadConnected.write("t82z".getBytes());
                    break;
                case R.id.button62:
                    myThreadConnected.write("t87z".getBytes());
                    break;
                case R.id.button63:
                    myThreadConnected.write("t92z".getBytes());
                    break;
                case R.id.button64:
                    myThreadConnected.write("t98z".getBytes());
                    break;
                case R.id.button65:
                    myThreadConnected.write("t104z".getBytes());
                    break;
                case R.id.button66:
                    myThreadConnected.write("t110z".getBytes());
                    break;
                case R.id.button67:
                    myThreadConnected.write("t117z".getBytes());
                    break;
                case R.id.button68:
                    myThreadConnected.write("t123z".getBytes());
                    break;
                case R.id.button69:
                    myThreadConnected.write("t131z".getBytes());
                    break;
            }
        }
    };

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener =
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    int myColor = Color.rgb(seekBarR.getProgress(), seekBarG.getProgress(), seekBarB.getProgress());
                    textView.setTextColor(myColor);
                    textViewRGB.setTextColor(myColor);

                    textViewR.setText("Красный " + seekBarR.getProgress());
                    textViewG.setText("Зелёный " + seekBarG.getProgress());
                    textViewB.setText("Синий " + seekBarB.getProgress());

                    // Формируем данные типа -1-1-1- or -11-11-11- or -111-111-111- or ...
                    String rgbData = String.valueOf("-" + seekBarR.getProgress() + "-" + seekBarG.getProgress() + "-" + seekBarB.getProgress() + "-");

                    if (myThreadConnected != null) {
                        byte[] bytesToSend = rgbData.getBytes();
                        myThreadConnected.write(bytesToSend);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            };

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(listener, sensorAccel, SensorManager.SENSOR_DELAY_NORMAL);

        // Вывод данных в потоке и с задержкой
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!control) {// Только если управление акселерометром активировано
                            seekBarX.setProgress(valuesAccelGravity[0] + 255);
                            seekBarY.setProgress(valuesAccelGravity[1] + 255);
                            textViewX.setText("X: " + valuesAccelGravity[0]);
                            textViewY.setText("Y: " + valuesAccelGravity[1]);

                            int xAxis = valuesAccelGravity[0], yAxis = valuesAccelGravity[1];
                            int leftMotor = 0, rightMotor = 0;

                            if (xAxis > 0) {// Если вправо
                                if (yAxis >= 0) {// 1 четверть
                                    leftMotor = Math.max(Math.abs(xAxis), Math.abs(yAxis));
                                    rightMotor = yAxis - xAxis;
                                } else {// 4 четверть
                                    leftMotor = yAxis + xAxis;
                                    rightMotor = -Math.max(Math.abs(xAxis), Math.abs(yAxis));
                                }
                            } else if (xAxis < 0) {// Если влево
                                if (yAxis >= 0) {// 2 четверть
                                    leftMotor = yAxis + xAxis;
                                    rightMotor = Math.max(Math.abs(xAxis), Math.abs(yAxis));
                                } else {// 3 четверть
                                    leftMotor = -Math.max(Math.abs(xAxis), Math.abs(yAxis));
                                    rightMotor = yAxis - xAxis;
                                }
                            } else if (xAxis == 0) {// Едем по прямой
                                leftMotor = yAxis;
                                rightMotor = yAxis;
                            }

                            seekBarLeftMotor.setProgress(leftMotor + 255);
                            seekBarRightMotor.setProgress(rightMotor + 255);
                            textViewLeftMotor.setText("leftMotor: " + leftMotor);
                            textViewRightMotor.setText("rightMotor: " + rightMotor);
                            setGradient(leftMotor, rightMotor);

                            String motorData = String.valueOf("m" + leftMotor + "z" + rightMotor + "z");

                            if (myThreadConnected != null) {
                                byte[] bytesToSend = motorData.getBytes();
                                myThreadConnected.write(bytesToSend);
                            }
                        }
                    }
                });
            }
        };
        timer.schedule(task, 0, 100);//Частота обновления
    }

    private void setGradient(int left, int right) {
        int leftColor, rightColor;

        if (left > 0)
            leftColor = Color.rgb(255, 255 - left, 255 - left);
        else leftColor = Color.rgb(255 + left, 255 + left, 255);
        if (right > 0)
            rightColor = Color.rgb(255, 255 - right, 255 - right);
        else rightColor = Color.rgb(255 + right, 255 + right, 255);

        GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{rightColor, leftColor});
        gradient.setShape(GradientDrawable.RECTANGLE);
        gradient.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        imageView.setImageDrawable(gradient);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listener);
        timer.cancel();
    }

    protected SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (!control) {
                switch (event.sensor.getType()) {
                    case Sensor.TYPE_ACCELEROMETER:
                        for (int i = 0; i < 3; i++) {
                            // Задаём верхний порог
                            if (event.values[i] > maxValue)
                                event.values[i] = maxValue;
                            if (event.values[i] < -maxValue)
                                event.values[i] = -maxValue;

                            // Задаём нижний порог
                            if (event.values[i] > -minValue && event.values[i] < minValue)
                                event.values[i] = 0;
                            else if (event.values[i] >= minValue)
                                event.values[i] = -convert(event.values[i], minValue, maxValue, 150, 255);
                            else
                                event.values[i] = -convert(event.values[i], -minValue, -maxValue, -150, -255);

                            valuesAccelGravity[i] = (int) event.values[i];
                            //valuesAccelGravity[i] = (int) (0.4 * event.values[i] + 0.6 * valuesAccelGravity[i]);// Постепенное нарастание значения
                        }
                        break;
                }
            }
        }
    };

    // Преобразование числа
    private int convert(double value, double From1, double From2, double To1, double To2) {
        return (int) ((value - From1) / (From2 - From1) * (To2 - To1) + To1);
    }

    @Override
    protected void onStart() { // Запрос на включение Bluetooth
        super.onStart();

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        setup();
    }

    private void setup() { // Создание списка сопряжённых Bluetooth-устройств

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) { // Если есть сопряжённые устройства

            pairedDeviceArrayList = new ArrayList<>();

            for (BluetoothDevice device : pairedDevices) { // Добавляем сопряжённые устройства - Имя + MAC-адресс
                pairedDeviceArrayList.add(device.getName() + "\n" + device.getAddress());
            }

            pairedDeviceAdapter = new ArrayAdapter<>(this, simple_list_item_1, pairedDeviceArrayList);
            listViewPairedDevice.setAdapter(pairedDeviceAdapter);

            listViewPairedDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() { // Клик по нужному устройству

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    listViewPairedDevice.setVisibility(View.GONE); // После клика скрываем список

                    String itemValue = (String) listViewPairedDevice.getItemAtPosition(position);
                    String MAC = itemValue.substring(itemValue.length() - 17); // Вычленяем MAC-адрес

                    BluetoothDevice device2 = bluetoothAdapter.getRemoteDevice(MAC);

                    myThreadConnectBTdevice = new ThreadConnectBTdevice(device2);
                    myThreadConnectBTdevice.start();  // Запускаем поток для подключения Bluetooth
                }
            });
        }
    }

    @Override
    protected void onDestroy() { // Закрытие приложения
        super.onDestroy();
        if (myThreadConnectBTdevice != null) myThreadConnectBTdevice.cancel();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) { // Если разрешили включить Bluetooth, тогда void setup()

            if (resultCode == Activity.RESULT_OK) {
                setup();
            } else { // Если не разрешили, тогда закрываем приложение

                Toast.makeText(this, "BlueTooth не включён", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    private class ThreadConnectBTdevice extends Thread { // Поток для коннекта с Bluetooth

        private BluetoothSocket bluetoothSocket = null;

        private ThreadConnectBTdevice(BluetoothDevice device) {

            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(myUUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void run() { // Коннект

            boolean success = false;

            try {
                bluetoothSocket.connect();
                success = true;
            } catch (IOException e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(MainActivity2017OldVersion.this, "Нет коннекта, проверьте Bluetooth-устройство с которым хотите соединиться!", Toast.LENGTH_LONG).show();
                        listViewPairedDevice.setVisibility(View.VISIBLE);
                    }
                });

                try {
                    bluetoothSocket.close();
                } catch (IOException e1) {

                    e1.printStackTrace();
                }
            }

            if (success) {  // Если законнектились, тогда открываем панель с кнопками и запускаем поток приёма и отправки данных

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        butPanel.setVisibility(View.VISIBLE); // открываем панель с кнопками
                    }
                });

                myThreadConnected = new ThreadConnected(bluetoothSocket);
                myThreadConnected.start(); // запуск потока приёма и отправки данных
            }
        }


        public void cancel() {

            Toast.makeText(getApplicationContext(), "Close - BluetoothSocket", Toast.LENGTH_LONG).show();

            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    } // END ThreadConnectBTdevice:


    private class ThreadConnected extends Thread {    // Поток - приём и отправка данных

        private final InputStream connectedInputStream;
        private final OutputStream connectedOutputStream;

        private String sbprint;

        public ThreadConnected(BluetoothSocket socket) {

            InputStream in = null;
            OutputStream out = null;

            try {
                in = socket.getInputStream();
                out = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            connectedInputStream = in;
            connectedOutputStream = out;
        }


        @Override
        public void run() { // Приём данных

            while (true) {
                try {
                    byte[] buffer = new byte[1];
                    int bytes = connectedInputStream.read(buffer);
                    String strIncom = new String(buffer, 0, bytes);
                    sb.append(strIncom); // собираем символы в строку
                    int endOfLineIndex = sb.indexOf("\r\n"); // определяем конец строки

                    if (endOfLineIndex > 0) {

                        sbprint = sb.substring(0, endOfLineIndex);
                        sb.delete(0, sb.length());

                        runOnUiThread(new Runnable() { // Вывод данных

                            @Override
                            public void run() {
                                temp.setText(sbprint);
                                /*switch (sbprint) {

                                    case "D10 ON":
                                        d10.setText(sbprint);
                                        break;

                                    default:
                                        break;
                                }*/
                            }
                        });
                    }
                } catch (IOException e) {
                    break;
                }
            }
        }


        public void write(byte[] buffer) {
            try {
                connectedOutputStream.write(buffer);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}