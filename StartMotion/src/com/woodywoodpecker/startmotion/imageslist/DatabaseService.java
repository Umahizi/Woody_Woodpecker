package com.woodywoodpecker.startmotion.imageslist;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
//import java.awt.image.BufferedImage;
//import javax.imageio.ImageIO;

import com.telerik.everlive.sdk.core.EverliveApp;
import com.telerik.everlive.sdk.core.facades.special.DownloadFileAsStreamFacade;
import com.telerik.everlive.sdk.core.query.definition.FileField;
import com.telerik.everlive.sdk.core.result.RequestResult;

public class DatabaseService extends Service {
	public static final String DATA_PASSED = "DATAPASSED";
	final static String MY_ACTION = "MY_ACTION";
	EverliveApp app;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		app = new EverliveApp("jBbtYae7LxkPAump");
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

	// private void addItems() throws IOException {
	// // create books
	// // for (int start = 0; start < 8; start++) {
	// String base64 =
	// encodeFileToBase64Binary("/storage/emulated/0/Bluetooth/20141010_223603.jpg");
	// // UUID filaAsUUID = decode(base64);
	// // File dido = new File("");
	//
	// // Base64.encode(FieldUtils.readFileToByteArray(file));
	//
	// UUID uuid = UUID.fromString(base64);
	// Gifs testBook = new Gifs();
	// testBook.setName("Are be moooi");
	// testBook.setData(uuid);
	// // app.fi
	// app.workWith().data(Gifs.class).create(testBook).executeAsync();
	// // }
	// }

	public String getDownloadLink(UUID fileId) {
		DownloadFileAsStreamFacade file = app.workWith().files()
				.download(fileId);
		String downloadPath = file.getDownloadPath();

		return downloadPath;
	}

	public void UploadFile(String fileName, String contentType,
			InputStream inputStream) {
		FileField fileField = new FileField(fileName, contentType, inputStream);

		RequestResult<?> result = app.workWith().files().upload(fileField)
				.executeSync();

		if (result.getSuccess()) {
			ArrayList<FileObject> items = (ArrayList<FileObject>) result
					.getValue();
			for (FileObject item : items) {
				Log.i("MainActivity", item.getDownloadURI());
			}
		}

		int a = 5;
	}

	private class Check extends Thread {
		@Override
		public void run() {
			super.run();
			// try {
			// addItems();
			// } catch (IOException e) {
			// Log.i("Problem", "chetenee");
			// }

			try {
				InputStream is = new FileInputStream(
						"/storage/emulated/0/Bluetooth/20141010_223603.jpg");
				UploadFile("Futbol", "image/jpeg", is);
				is.close();
			} catch (IOException e) {
				Log.i("Problem", "reading");
			}

			while (true) {
				// try {
				// Thread.sleep(5000);
				Intent intent = new Intent();
				intent.setAction(MY_ACTION);
				RequestResult<?> allItems = app.workWith().data(Gifs.class)
						.getAll().executeSync();
				if (allItems.getSuccess()) {
					ArrayList<?> boooks = (ArrayList<?>) allItems.getValue();

					for (Object book : boooks) {
						Gifs test = (Gifs) book;
						String downloadLink = getDownloadLink(test.getData());
						intent.putExtra(DATA_PASSED, downloadLink);
						sendBroadcast(intent);
					}
				}

				// intent.putExtra(DATA_PASSED, "123");

				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
			}
		}
	}
}