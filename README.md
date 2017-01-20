# WiFiTracker
WifiTracker supports the basic features of wireless sensor system, networks using the mobile application processors and Location-awareness.
This android application includes Wi-Fi manager services, sensor data storage/transfer/processing such as SQLite database, cloud-based storage and Google map service.

Application features

1. Obtain the details of Wi-Fi signals at different locations. These details include SSID, RSSI, Device ID.

2. Records the timestamp with each data in order to correctly interpret the data.

3. Retrieves the last known location of an Android device, which is usually equivalent to the user's current location.

4. Requests and receives periodic location updates.

5. Stores signal strength and location data in SQLite database which will be locally available on the device, when there is no internet connection.

6. Sends that data through a webserver to cloud-based storage using the given web service whenever device has internet connection available.

7. Whenever there is an internet connection available, application can automatically synchronize data to web server. For this task, a Service which will run in background indefinitely and check for internet to send data for every 5 minutes.

8. Retrieves the complete data from server and displays the markers at different locations on google map, with different colors as per signal strength if internet connection is available.

9. When there is no internet connectivity, retrieves the data available on the device and displays the markers at different locations on google map.

Compatibility

1. Minimum SDK – API 21: Android 5.0 (Lollipop)

2. It is recommended to use the application while connected to a network in order to obtain accurate location information.

3. Compatible with android smartphones and tablet devices with Wi-Fi service.

4. Compatibility is not guaranteed for devices without GPS capabilities.
