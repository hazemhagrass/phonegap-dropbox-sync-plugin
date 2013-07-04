/**
 * Copyright (c) 2013, Hazem Hagrass
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/**
 * @author hazem.hagrass@badrit.com (Hazem Hagrass)
 */

package com.badrit.dropbox;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

public class Dropbox extends CordovaPlugin {
	private static final String appKey = "b7ys1kj2hmpxlhg";
	private static final String appSecret = "wfe8cpkfj3xf4jo";
	private static final int REQUEST_LINK_TO_DBX = 0;

	private DbxAccountManager mDbxAcctMgr;
	private CallbackContext callback;

	@Override
	public boolean execute(String action, JSONArray args,
		CallbackContext callbackContext) {

		callback = callbackContext;

		Log.d("Dropbox", "Initializing dropbox plugin");
		mDbxAcctMgr = DbxAccountManager.getInstance(this.cordova.getActivity()
			.getApplicationContext(), appKey, appSecret);

		if (action.equals("checkLink")) {
			checkLink();
		} 
		else if (action.equals("link")) {
			link();
		} 
		else if (action.equals("unlink")) {
			unlink();
		} 
		else if (action.equals("addObserver")) {
			//TODO: Need to be implemented
			Log.d("Dropbox", "adding observer");
		} 
		else if (action.equals("readData")) {
			//TODO: Need to be implemented to handle images
			Log.d("Dropbox", "reading data");
		} 
		else if (action.equals("readString")) {
			try {
				readFile(args.getString(0) + "");
			} catch (JSONException e) {
				Log.d("Dropbox", "Exception: " + e.getMessage());
			}

		} 
		else if (action.equals("listFolder")) {
			try {
				listFolder(args.getString(0) + "");
			} catch (JSONException e) {
				Log.d("Dropbox", "Exception: " + e.getMessage());
			}
		}
		return true;
	}

	private void checkLink(){
		Log.d("Dropbox", "Check dropbox linking");
		if (!mDbxAcctMgr.hasLinkedAccount()) {
			sendPluginResult(PluginResult.Status.OK, "false");
			Log.d("Dropbox", "Not linked");
		} else {
			sendPluginResult(PluginResult.Status.OK, "true");
			Log.d("Dropbox", "Already linked");
			
		}
	}

	private void link(){
		Log.d("Dropbox", "Linking dropbox plugin");

		mDbxAcctMgr.startLink(this.cordova.getActivity(),
			REQUEST_LINK_TO_DBX);

		sendPluginResult(PluginResult.Status.OK, "true");
	}

	private void unlink(){
		Log.d("Dropbox", "Unlink dropbox");

		mDbxAcctMgr.unlink();

		sendPluginResult(PluginResult.Status.OK, "true");
	}

	private void readFile(String path){
		try {
			Log.d("Dropbox", "Reading string");
			DbxFileSystem dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr
				.getLinkedAccount());
			DbxPath dbxPath = new DbxPath(DbxPath.ROOT, path);

			if (dbxFs.isFile(dbxPath)) {
				Log.d("Dropbox", "Reading file: " + dbxPath);

				String resultData;
				DbxFile file = dbxFs.open(dbxPath);

				try {
					resultData = file.readString();
				} finally {
					file.close();
				}

				sendPluginResult(PluginResult.Status.OK, resultData);

			} else if (dbxFs.isFolder(dbxPath)) {
				Log.d("Dropbox", "This path: " + path +  " is a folder ");
			}
		} catch (IOException e) {
			Log.d("Dropbox", "IOException: " + e.getMessage());
		}
	}
	private void listFolder(String path){
		Log.d("Dropbox", "Listing folder, path: " + path);
		JSONArray result = new JSONArray();
		try {
			DbxFileSystem dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr
				.getLinkedAccount());
			DbxPath dbxPath = new DbxPath(DbxPath.ROOT, !path.equals("/") ? path : "");
			List<DbxFileInfo> infos = dbxFs.listFolder(dbxPath);
			for (DbxFileInfo info : infos) {
				try{
					JSONObject fileInfo = new JSONObject();
					fileInfo.put("path", info.path);
					fileInfo.put("isFolder", info.isFolder);
					result.put(fileInfo);
				}catch (JSONException e) {
					Log.d("Dropbox", "JSONException" + e.getMessage());
					throw new RuntimeException(e);
				}
			}
		} catch (IOException e) {
			Log.d("Dropbox", "IOException" + e.getMessage());
		}
		sendPluginListFolder(PluginResult.Status.OK, result);
	}
	private void sendPluginResult(PluginResult.Status status, String result){
		PluginResult res = new PluginResult(status,
			result);
		res.setKeepCallback(true);
		callback.sendPluginResult(res);
	}
	private void sendPluginListFolder(PluginResult.Status status, JSONArray result){
		PluginResult res = new PluginResult(status,
			result);
		res.setKeepCallback(true);
		callback.sendPluginResult(res);
	}
	// private void doDropboxTest() {
	// try {
	// final String TEST_DATA = "Hello Dropbox";
	// final String TEST_FILE_NAME = "hello_dropbox.txt";
	// DbxPath testPath = new DbxPath(DbxPath.ROOT, TEST_FILE_NAME);
	//
	// // Create DbxFileSystem for synchronized file access.
	// DbxFileSystem dbxFs =
	// DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());
	//
	// // Print the contents of the root folder. This will block until we can
	// // sync metadata the first time.
	// List<DbxFileInfo> infos = dbxFs.listFolder(DbxPath.ROOT);
	// mTestOutput.setText("\nContents of app folder:\n");
	// for (DbxFileInfo info : infos) {
	// mTestOutput.append("    " + info.path + ", " + info.modifiedTime + '\n');
	// }
	//
	// // Create a test file only if it doesn't already exist.
	// if (!dbxFs.exists(testPath)) {
	// DbxFile testFile = dbxFs.create(testPath);
	// try {
	// testFile.writeString(TEST_DATA);
	// } finally {
	// testFile.close();
	// }
	// mTestOutput.append("\nCreated new file '" + testPath + "'.\n");
	// }
	//
	// // Read and print the contents of test file. Since we're not making
	// // any attempt to wait for the latest version, this may print an
	// // older cached version. Use getSyncStatus() and/or a listener to
	// // check for a new version.
	// if (dbxFs.isFile(testPath)) {
	// String resultData;
	// DbxFile testFile = dbxFs.open(testPath);
	// try {
	// resultData = testFile.readString();
	// } finally {
	// testFile.close();
	// }
	// mTestOutput.append("\nRead file '" + testPath + "' and got data:\n    " +
	// resultData);
	// } else if (dbxFs.isFolder(testPath)) {
	// mTestOutput.append("'" + testPath.toString() + "' is a folder.\n");
	// }
	// } catch (IOException e) {
	// mTestOutput.setText("Dropbox test failed: " + e);
	// }
	// }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_LINK_TO_DBX) {
			if (resultCode == Activity.RESULT_OK) {
				// doDropboxTest();
			} else {
				Log.d("Dropbox", "Link to Dropbox failed or was cancelled.");
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
}
