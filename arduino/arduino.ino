// https://github.com/GyverLibs/GyverMotor
#include "GyverMotor.h"

#include <SoftwareSerial.h>

// (тип драйвера, пин, ШИМ пин, уровень драйвера)
GMotor motorR(DRIVER2WIRE, 4, 5, HIGH);
GMotor motorL(DRIVER2WIRE, 2, 3, HIGH);

char incomingbyte;
unsigned long lastTime;

SoftwareSerial mySerial(11, 12);

void setup() {
    Serial.println("setup");
    motorR.setMode(AUTO);
    motorL.setMode(AUTO);
    motorR.setMinDuty(120);
    motorL.setMinDuty(120);
    Serial.begin(4800);
    mySerial.begin(9600);
}

void loop() {
    if (mySerial.available() > 0) {
        incomingbyte = mySerial.read();
        Serial.println(incomingbyte);
        switch (incomingbyte) {
            case 'm':// motor
                runMotor(readByte('m'), motorL);
                runMotor(readByte('m'), motorR);
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