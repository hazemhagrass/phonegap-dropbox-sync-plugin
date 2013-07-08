Phonegap Dropbox Plugin
============================

Dropbox Sync API for Android
----------------------------
This plugin is based on dropbox sync API
The Sync API can be used on Android devices running API version
4 or higher.  You can build your application using the SDK for API
version 4 or higher, but building with the latest API version is
recommended.

**Steps:**

1. Setup Dropbox Sync Project from [here](https://www.dropbox.com/developers/sync/start/android#project-setup)

2. Copy *libs/* folder to your libs/ directory.

3. Copy *com.badrit.dropbox* folder to your *src* directory

4. Update *com.badrit.dropbox/Dropbox.java* **appKey** and **appMaster** with your app values.

5. Add this code to *res/xml/config.xml*
	<code><plugin name="Dropbox" value="com.badrit.dropbox.Dropbox" /></code>

6. Update *AndroidMainfest.xml* to be like the plugin one, but make sure to 
   update it with your dropbox **app key**
	 <code><data android:scheme="db-APP_KEY" /></code>

7. Copy *JS* files to your *www* folder.
	 
Dropbox Sync API for IOS
----------------------------
	
 [source code](https://github.com/ccoenraets/phonegap-dropbox-sync) 

**Note:**

This plugin is inspired from [Dropbox Sync plugin](https://github.com/ccoenraets/phonegap-dropbox-sync) for IOS
	
Refrences:

1. [https://www.dropbox.com/developers/sync](https://www.dropbox.com/developers/sync)

2. [https://github.com/ccoenraets/phonegap-dropbox-sync](https://github.com/ccoenraets/phonegap-dropbox-sync)