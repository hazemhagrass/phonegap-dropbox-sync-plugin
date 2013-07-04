phonegap-dropbox-sync-plugin
============================

Dropbox Sync API for Android
----------------------------
This plugin is based on .ropbox sync API
The Sync API can be used on Android devices running API version
4 or higher.  You can build your application using the SDK for API
version 4 or higher, but building with the latest API version is
recommended.

Steps:
1- Setup Dropbox Syn Project
	https://www.dropbox.com/developers/sync/start/android#project-setup
2- Copy libs/ folder to your libs/ directory.
3- Copy com.badrit.dropbox folder to your src directory
4- Add this code to res/xml/config.xml
	<plugin name="Dropbox" value="com.badrit.dropbox.Dropbox" />
5- Update AndroidMainfest.xml to be like the plugin one, but make sure to 
   update it with your dropbox app key
	 <data android:scheme="db-APP_KEY" />
	 
Dropbox Sync API for IOS
----------------------------
	https://github.com/ccoenraets/phonegap-dropbox-sync

Note:
This plugin is inspired from Dropbox Sync plugin for IOS:
	https://github.com/ccoenraets/phonegap-dropbox-sync
	
Refrences:
https://www.dropbox.com/developers/sync
https://github.com/ccoenraets/phonegap-dropbox-sync
