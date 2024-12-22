# Project: Robo Car

This project is a remote-controlled robotic car equipped with various control mechanisms and capabilities, including video streaming, environmental data monitoring, and interactive remote management.

---

## Features

### Control Mechanisms
1. **Mobile App**: Control the car via a mobile application with on-screen buttons.
2. **Accelerometer Control**: Utilize your phone's accelerometer to steer the car.
3. **Gamepad Control**: Use a gamepad for precise control.
4. **Remote Control**: Operate the car remotely from another device.

### Dual Mode Application
- **Host Mode**: Acts as the main control interface.
- **Remote Mode**: Provides a secondary control interface through web sockets, communicating with a Kotlin backend for seamless interaction.

### Video Streaming
- View real-time video from the car's ESP32-based camera module.

### Environmental Monitoring
- Read and display temperature and humidity data from a weather sensor mounted on the car.

---

## Technical Details

### Hardware Components
- **Arduino Module**: Central processing unit for car operations.
- **Bluetooth Module**: Facilitates communication with the mobile application.
- **Motor Modules**: Drives the car’s wheels.
- **ESP32 Module**: Handles video streaming and camera operations.
- **Weather Sensor**: Collects temperature and humidity data.

### Software Features
- **Mobile Application**: Offers multiple control modes and displays real-time data.
- **Web Socket Communication**: Enables remote operation between host and remote devices.
- **Kotlin Backend**: Manages interactions between devices and supports seamless data exchange.

---

## Media

<table>
    <tr>
        <td>
            <img src="https://github.com/eduard-mishustin/RoboBt/blob/main/media/app.jpeg" width="256"/>
        </td>
        <td>
            <img src="https://github.com/eduard-mishustin/RoboBt/blob/main/media/car_1.jpeg" width="256"/>
            <img src="https://github.com/eduard-mishustin/RoboBt/blob/main/media/car_2.jpeg" width="256"/>
        </td>
    </tr>
    <tr>
        <td>
            Mobile application
        </td>
        <td>
            Car
        </td>
    </tr>
</table>
