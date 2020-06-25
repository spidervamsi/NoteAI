# ADB(Android Device Bridge)
We are using ADB to test the App development because the Android AVD on laptop is really slow.

## ADB Usage in OPPO A3S

Go to "About Phone"-> click on "Version" 7 times for developer mode.  
Go to "Additional Settings"-> "Developer Options"-> toggle "USB debugging"

## Usage

```bash
#Connected devices( you only need device ID if you have multiple devices)
adb devices

#Uninstall
adb -s 98718635 uninstall com.example.noteai

#install from apk on computer
adb install app-debug.apk

#re-install from apk on computer
adb install -r app-debug.apk

#to get info about running app
adb shell dumpsys window windows | grep Focus

#to open an app
adb shell am start -c api.android.intent.LAUNCHER -a api.android.category.MAIN -n com.example.noteai/com.example.noteai.MainActivity

#print logs (-d = destroy once done)
adb logcat -d

#logs to a file
adb logcat > noteAI-logs.txt

#clear logs
adb logcat -c

#to search among logs
adb logcat | grep "NoteAITest"

#to get into Mobile OS
adb shell

#send files from laptop to mobile
adb push random.txt /sdcard/Android

#get files from mobile to laptop
adb pull /sdcard/Android/random.txt

#force stop a mobile application (-am : android manager)
adb shell am force-stop com.example.noteai

#wipe all the data (-pm : package manager)
adb shell pm clear com.example.noteai

#to find a specific string
adb shell dumpsys | grep "DUMP OF SERVICE"
```

