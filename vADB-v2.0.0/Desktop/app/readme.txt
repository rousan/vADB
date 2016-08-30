**************************************************************************
Welcome to the vADB Tutorials.
Please read carefully the instructions below to setup.
It is compatible from HoneyComb 3.0 to Marshmallow 6.0 Android version.
It requirs minimum API level 11
***************************************************************************

1. First of all please read the full description from the android app of this project -> vADB-v2.0.0.apk.
2.To get logs you have to create a package named 'lib' in your current project, and then create a class named 'VLog' in that package, and then copy & paste the full texts of the file 'VLog.java' located in 'lib' folder to that 'VLog' class. Then replace 'package in.byter.vadb.lib' with 'package <your-package-name>.lib' in this VLog.java class. This 'VLog' class requirs minimum API level 11.
3. Replace 'import in.byter.vadb.BuildConfig' with 'import <your-package-name>.BuildConfig' in the VLog.java source file.
4. Add a permission in AndroidManifest.xml in your androis project ->
"<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />";
5. To disable or enable the VLog service you have to call VLog.Settings.setVlogEnabled(boolean enable) mathod from anywhere in your android project. If you disable this, then all VLog's logs methods(d, e, i, v, w) will be replaced by Log.d, Log.e, Log.i and so on. If you face any problem about logging then you can check this VLog.java class.
6. Now you are done!


*************************
Keep touchs with us at ariyankhan46@gmail.com,
Happy Coding...