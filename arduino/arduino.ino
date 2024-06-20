// https://github.com/GyverLibs/GyverMotor
#include "GyverMotor.h"

#include <SoftwareSerial.h>

// (тип драйвера, пин, ШИМ пин, уровень драйвера)
GMotor motorTopRight(DRIVER2WIRE, 4, 5, HIGH);
GMotor motorTopLeft(DRIVER2WIRE, 3, 2, HIGH);
GMotor motorBottomRight(DRIVER2WIRE, 6, 7, HIGH);
GMotor motorBottomLeft(DRIVER2WIRE, 9, 8, HIGH);

char incomingbyte;
unsigned long lastTime;

SoftwareSerial mySerial(11, 12);

void setup() {
    motorTopRight.setMode(AUTO);
    motorTopLeft.setMode(AUTO);
    motorBottomRight.setMode(AUTO);
    motorBottomLeft.setMode(AUTO);

    motorTopRight.setMinDuty(100);
    motorTopLeft.setMinDuty(100);
    motorBottomRight.setMinDuty(100);
    motorBottomLeft.setMinDuty(100);

    Serial.begin(4800);
    mySerial.begin(9600);
}

void loop() {
    if (mySerial.available() > 0) {
        incomingbyte = mySerial.read();

        switch (incomingbyte) {
            case 'm':// motor
                int leftMotorSpeed = readByte('m');
                int rightMotorSpeed = readByte('m');

                runMotor(leftMotorSpeed, motorTopLeft);
                runMotor(rightMotorSpeed, motorTopRight);
                runMotor(leftMotorSpeed, motorBottomLeft);
                runMotor(rightMotorSpeed, motorBottomRight);
                break;
            default:
                break;
        }
    }
}

// Чтение строки
int readByte(char set) {
    String bytesStr = "";
    while (1 == 1) {
        incomingbyte = mySerial.read();
        if ((set == 'm' && (incomingbyte == '0' || incomingbyte == '1' || incomingbyte == '2' || incomingbyte == '3' ||
                            incomingbyte == '4' || incomingbyte == '5' || incomingbyte == '6' || incomingbyte == '7' ||
                            incomingbyte == '8' || incomingbyte == '9' || incomingbyte == '-')) ||
            (set == 't' && (incomingbyte == '0' || incomingbyte == '1' || incomingbyte == '2' || incomingbyte == '3' ||
                            incomingbyte == '4' || incomingbyte == '5' || incomingbyte == '6' || incomingbyte == '7' ||
                            incomingbyte == '8' || incomingbyte == '9')))
            bytesStr += incomingbyte;
        else if (incomingbyte == 'z') break;
    }
    return bytesStr.toInt();
}

// Управлените моторами
void runMotor(int motor, GMotor AF_DC) {
    AF_DC.setSpeed(motor);
}