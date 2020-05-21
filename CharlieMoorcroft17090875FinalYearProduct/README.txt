SmartHomeControlApplication

Required Software and kit
-----------------------------
Android Studio
Eclipse
Phidget 22 Api
A phidget RFID reader
A phidget Servo Motor
TomCat 9
-----------------------------
General explanation
-----------------------------
This is a application used to control smart home devices, currently it can only control devices
which use the phidget 22 api. 
-----------------------------
Notes
-----------------------------
The app will not work on Eduroam not the MMU free wifi
The app will only run on android studio 3.5 and above
There may be build path issues with the JRE library, I have tested on multiple machines,
the issue only happens sometimes I can not guarantee it is 100% fixed.
-----------------------------
Setup			    
-----------------------------
1) Unzip the file contents
2) Open the SmartHomeBackEnd folder in eclipse
3) Run the deviceDAO class as a server 
4) Boot the FinalProjectCodeandApp folder using Android studio
5) Plug in your Phidget devices 
6) Follow the instructions in the app info screen to add devices to the database
7) Activate the necessary classes for motors this is deviceSubscriber, for the RFID reader this is the lockPublisher class
8) The app should work as intended
---------------------------------------------------
Current Functionality and possible future features
---------------------------------------------------

Current Functionality

The application can add new devices to the database
The Application can also generate dynamic UI components based off of new data added to the database
The application can also detect when a RFID reader has been detected
The application can controlly only control servo motors
The backend does have a basic framework in place which could be expanded to control lights and switchly

Intended future features

I would like to program a activate light callback class
Finish the details view in the application for better control over the devices
Program a switch callback class