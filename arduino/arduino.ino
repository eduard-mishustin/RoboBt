#include <AFMotor.h>
#include <dht11.h>
dht11 DHT;
#include <Servo.h>
#include "Wire.h"
#include "I2Cdev.h"
#include "MPU6050.h"

AF_DCMotor motor2(2);
AF_DCMotor motor3(3);
AF_DCMotor motor4(4);
Servo servo;

char incomingbyte;
unsigned long lastTime;
int leftMotor = 0, rightMotor = 0;

int count = 0;
bool flag = true;

MPU6050 accelgyro;
int16_t ax, ay, az, startAy = 0;
int16_t gx, gy, gz;
double filter_ay;
double pred_ay;

// переменные для калмана
float varVolt = 65;  // среднее отклонение (ищем в excel)
float varProcess = 0.01; // скорость реакции на изменение (подбирается вручную)
float Pc = 0.0;
float G = 0.0;
float P = 1.0;
float Xp = 0.0;
float Zp = 0.0;
float Xe = 0.0;
// переменные для калмана

void setup(){
    pinMode(13, OUTPUT);
    Serial.begin(38400);
    motor2.setSpeed(1);
    servo.attach(9);
    servo.write(90);
    Wire.begin();
    accelgyro.initialize();

    /*digitalWrite(13, HIGH);
    delay(1000);
    accelgyro.getMotion6(&ax, &ay, &az, &gx, &gy, &gz);
    startAy = filter(ay);
    digitalWrite(13, LOW);*/
}

void loop(){
    if(Serial.available() > 0){
        incomingbyte = Serial.read();
        //Serial.println(incomingbyte);
        switch (incomingbyte){
            // Ручное управление моторами
            case 'A':
                motor4.setSpeed(255);
                motor4.run(FORWARD);
                break;
            case 'B':
                motor3.setSpeed(255);
                motor3.run(FORWARD);
                break;
            case 'C':
                motor4.setSpeed(255);
                motor4.run(BACKWARD);
                break;
            case 'D':
                motor3.setSpeed(255);
                motor3.run(BACKWARD);
                break;
            case 'a':
            case 'c':
                motor4.run(RELEASE);
                break;
            case 'b':
            case 'd':
                motor3.run(RELEASE);
                break;

            case 'm':// motor
                runMotor(readByte('m'), motor3);
                runMotor(readByte('m'), motor4);
                break;

            case 't':// tone
                tone(13, readByte('t'), 300);
                break;

            case 's':// servo
                servo.write(readByte('t'));
                break;

            case 'Q':// tower
                if(millis() - lastTime > 50){
                    lastTime = millis();
                    motor2.run(BACKWARD);
                    delay(3);
                    motor2.run(RELEASE);
                }
                break;
            case 'E':
                if(millis() - lastTime > 50){
                    lastTime = millis();
                    motor2.run(FORWARD);
                    delay(3);
                    motor2.run(RELEASE);
                }
                break;
            case 'q':
            case 'e':
                motor2.run(RELEASE);
                break;

            default:
                break;

        }
    } else acc();
}

// Чтение строки
int readByte(char set){
    String bytesStr = "";
    while(1 == 1){
        incomingbyte = Serial.read();
        if ((set == 'm' && (incomingbyte == '0' || incomingbyte == '1' || incomingbyte == '2' || incomingbyte == '3' || incomingbyte == '4' || incomingbyte == '5' || incomingbyte == '6' || incomingbyte == '7' || incomingbyte == '8' || incomingbyte == '9' || incomingbyte == '-')) ||
            (set == 't' && (incomingbyte == '0' || incomingbyte == '1' || incomingbyte == '2' || incomingbyte == '3' || incomingbyte == '4' || incomingbyte == '5' || incomingbyte == '6' || incomingbyte == '7' || incomingbyte == '8' || incomingbyte == '9')))
            bytesStr += incomingbyte;
        else if(incomingbyte == 'z') break;
    }
    return bytesStr.toInt();
}

// Управлените моторами
void runMotor(int motor, AF_DCMotor AF_DC){
    if (motor == 0){
        AF_DC.run(RELEASE);
    }else if (motor > 0){
        AF_DC.setSpeed(motor);
        AF_DC.run(FORWARD);
    }else if (motor < 0){
        AF_DC.setSpeed(-motor);
        AF_DC.run(BACKWARD);
    }
}

// Отправка температуры
void sendTemp(){
    if(millis() - lastTime > 2000){
        lastTime = millis();

        int chk = DHT.read(3);
        String str = "Temperature: ";
        str.concat(String(DHT.temperature, DEC));
        str.concat("   Humidity: ");
        str.concat(String(DHT.humidity, DEC));
        Serial.println(str);
    }
}

void acc(){
    /*if(millis() - lastTime > 1) {
      lastTime = millis();*/

    //Получение значений акселерометра и гироскопа сразу
    accelgyro.getMotion6(&ax, &ay, &az, &gx, &gy, &gz);
    filter_ay = filter(ay);
    //Вывод всего
    /*Serial.print("ax:\t"); Serial.print(ax); Serial.print("\n");*/
    /*Serial.print("ay:\t"); Serial.print(ay); Serial.print("\n");*/
    /*Serial.print("az:\t"); Serial.print(az); Serial.print("\n");
    Serial.print("gx:\t"); Serial.print(gx); Serial.print("\n");
    Serial.print("gy:\t"); Serial.print(gy); Serial.print("\n");
    Serial.print("gz:\t"); Serial.println(gz);*/

    //if (ay < 2000)
    if (filter_ay > 800 && filter_ay < 1200 && filter_ay > pred_ay) {

        if (flag){
            count++;
        }

        flag = false;
        digitalWrite(13, HIGH);

    }
    else {
        digitalWrite(13, LOW);
        flag = true;
    }
    pred_ay = filter_ay;
    //}

    // Отправка данных в SerialPortPlotter
    Serial.print("$");
    Serial.print(ay);
    Serial.print(" ");
    Serial.print((int)filter_ay);
    Serial.println(";");
}

//Фильтр Калмана
float filter(float val) {
    Pc = P + varProcess;
    G = Pc/(Pc + varVolt);
    P = (1-G)*Pc;
    Xp = Xe;
    Zp = Xp;
    Xe = G*(val-Zp)+Xp;
    return(Xe);
}
