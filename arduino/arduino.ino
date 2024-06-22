// https://alexgyver.ru/gyvermotor/
#include "GyverMotor.h"
// https://kit.alexgyver.ru/tutorials/bluetooth-jdy31/
#include <SoftwareSerial.h>

// (тип драйвера, пин, ШИМ пин, уровень драйвера)
GMotor motorTopLeft(DRIVER2WIRE, 2, 3, HIGH);
GMotor motorTopRight(DRIVER2WIRE, 4, 5, HIGH);
GMotor motorBottomLeft(DRIVER2WIRE, 8, 9, HIGH);
GMotor motorBottomRight(DRIVER2WIRE, 7, 6, HIGH);

SoftwareSerial bluetooth(11, 12);

void setup() {
    motorTopLeft.setDirection(REVERSE);
    motorTopRight.setDirection(NORMAL);
    motorBottomLeft.setDirection(REVERSE);
    motorBottomRight.setDirection(REVERSE);

    motorTopLeft.setMode(AUTO);
    motorTopRight.setMode(AUTO);
    motorBottomLeft.setMode(AUTO);
    motorBottomRight.setMode(AUTO);

    motorTopLeft.setMinDuty(100);
    motorTopRight.setMinDuty(100);
    motorBottomLeft.setMinDuty(100);
    motorBottomRight.setMinDuty(100);

    Serial.begin(4800);
    bluetooth.begin(9600);
}

void loop() {
    if (bluetooth.available() > 0) {
        char key = bluetooth.read();

        switch (key) {
            case 'm':
                int leftMotorSpeed = readValue('m');
                int rightMotorSpeed = readValue('m');

                motorTopLeft.setSpeed(leftMotorSpeed);
                motorTopRight.setSpeed(rightMotorSpeed);
                motorBottomLeft.setSpeed(leftMotorSpeed);
                motorBottomRight.setSpeed(rightMotorSpeed);
                break;
            default:
                break;
        }
    }
}

int readValue(char key) {
    String valueString = "";

    while (true) {
        char value = bluetooth.read();

        if (key == 'm' && (isDigit(value) || value == '-')) {
            valueString += value;
        } else if (value == 'z') {
            break;
        }
    }

    return valueString.toInt();
}