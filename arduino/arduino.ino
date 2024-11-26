// https://alexgyver.ru/gyvermotor
#include "GyverMotor.h"
// https://kit.alexgyver.ru/tutorials/htu21d
#include <GyverHTU21D.h>
// https://kit.alexgyver.ru/tutorials/bluetooth-jdy31
#include <SoftwareSerial.h>

// (тип драйвера, пин, ШИМ пин, уровень драйвера)
GMotor motorTopLeft(DRIVER2WIRE, 7, 6, HIGH);
GMotor motorTopRight(DRIVER2WIRE, 2, 3, HIGH);
GMotor motorBottomLeft(DRIVER2WIRE, 8, 9, HIGH);
GMotor motorBottomRight(DRIVER2WIRE, 4, 5, HIGH);

GyverHTU21D weather;

SoftwareSerial bluetooth(11, 12);

void setup() {
    motorTopLeft.setDirection(NORMAL);
    motorTopRight.setDirection(NORMAL);
    motorBottomLeft.setDirection(REVERSE);
    motorBottomRight.setDirection(NORMAL);

    motorTopLeft.setMode(AUTO);
    motorTopRight.setMode(AUTO);
    motorBottomLeft.setMode(AUTO);
    motorBottomRight.setMode(AUTO);

    motorTopLeft.setMinDuty(100);
    motorTopRight.setMinDuty(100);
    motorBottomLeft.setMinDuty(100);
    motorBottomRight.setMinDuty(100);

    Serial.begin(9600);
    bluetooth.begin(9600);

    //weather.begin();
}

void loop() {
    if (bluetooth.available() > 0) {
        char key = bluetooth.read();
        Serial.print(key);

        switch (key) {
            case 'm':
                int leftMotorSpeed = readValue();
                int rightMotorSpeed = readValue();

                motorTopLeft.setSpeed(leftMotorSpeed);
                motorTopRight.setSpeed(rightMotorSpeed);
                motorBottomLeft.setSpeed(leftMotorSpeed);
                motorBottomRight.setSpeed(rightMotorSpeed);
                break;
            default:
                break;
        }

        Serial.println();
    }

    //sendWeather();
}

int readValue() {
    String valueString = "";

    while (true) {
        char value = bluetooth.read();

        if (isDigit(value) || value == '-') {
            valueString += value;
        } else if (value == ';') {
            break;
        }
    }

    Serial.print(valueString);
    Serial.print(";");
    return valueString.toInt();
}

void sendWeather() {
    if (weather.readTick(15000)) {
        String data = "";

        data.concat(weather.getTemperature());
        data.concat(";");
        data.concat(weather.getHumidity());

        if (bluetooth.available() == 0) {
            bluetooth.print(data);
        }
    }
}