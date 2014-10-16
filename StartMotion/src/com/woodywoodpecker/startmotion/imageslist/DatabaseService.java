package com.woodywoodpecker.startmotion.imageslist;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.telerik.everlive.sdk.core.EverliveApp;
import com.telerik.everlive.sdk.core.facades.special.DownloadFileAsStreamFacade;
import com.telerik.everlive.sdk.core.model.system.File;
import com.telerik.everlive.sdk.core.query.definition.FileField;
import com.telerik.everlive.sdk.core.result.RequestResult;

public class DatabaseService extends Service {
	public static final String DATA_PASSED = "DATAPASSED";
	public static final String MY_ACTION = "MY_ACTION";

	private static final String API_KEY = "jBbtYae7LxkPAump";

	private static EverliveApp app;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		app = new EverliveApp(API_KEY);
		app.workWith().authentication().login("telerik_test", "1234")
				.executeSync();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Check test = new Check();
		test.start();

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public String getDownloadLink(UUID fileId) {
		DownloadFileAsStreamFacade file = app.workWith().files()
				.download(fileId);
		String downloadPath = file.getDownloadPath();

		return downloadPath;
	}

	public static void UploadFile(String fileName, String contentType,
			InputStream inputStream) {
		FileField fileField = new FileField(fileName, contentType, inputStream);
		app.workWith().files().upload(fileField).executeSync();
	}

	private class Check extends Thread {
		@Override
		public void run() {
			super.run();

			while (true) {
				Intent intent = new Intent();
				intent.setAction(MY_ACTION);

				RequestResult<?> requestResult = app.workWith()
						.data(File.class).getAll().executeSync();

				if (requestResult.getSuccess()) {
					@SuppressWarnings("unchecked")
					ArrayList<File> allFiles = (ArrayList<File>) requestResult
							.getValue();

					for (File file : allFiles) {
						UUID currentUuid = file.getId();
						String downloadLink = getDownloadLink(currentUuid);
						intent.putExtra(DATA_PASSED, downloadLink);
						sendBroadcast(intent);
					}
				}
			}
		}
	}
}