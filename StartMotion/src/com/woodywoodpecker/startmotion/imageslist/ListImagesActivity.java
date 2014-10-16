package com.woodywoodpecker.startmotion.imageslist;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.woodywoodpecker.startmotion.AnimatedGifEncoder;
import com.woodywoodpecker.startmotion.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("SdCardPath")
public class ListImagesActivity extends Activity {
	private static final String SD_CARD = "/sdcard/Pictures/test31.gif";

	private ProgressDialog simpleWaitDialog;

	ListView list;
	LazyImageLoadAdapter adapter;
	ArrayList<String> stringList;
	MyReceiver myReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_images);
		stringList = new ArrayList<String>();
		// stringList
		// .add("http://androidexample.com/media/webservice/LazyListView_images/image0.png");
		// stringList
		// .add("http://androidexample.com/media/webservice/LazyListView_images/image1.png");

		list = (ListView) findViewById(R.id.listImages);

		// Create custom adapter for listview
		adapter = new LazyImageLoadAdapter(this, stringList);

		// Set adapter to listview
		list.setAdapter(adapter);

		myReceiver = new MyReceiver();

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(DatabaseService.MY_ACTION);
		registerReceiver(myReceiver, intentFilter);

		Button b = (Button) findViewById(R.id.cacheButton);
		b.setOnClickListener(listener);
	}

	@Override
	public void onDestroy() {
		// Remove adapter refference from list
		list.setAdapter(null);
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		unregisterReceiver(myReceiver);
		super.onStop();
	}

	public OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {

			// Refresh cache directory downloaded images
			// adapter.imageLoader.clearCache();
			// adapter.notifyDataSetChanged();
			// Bitmap[] bitmaps = new Bitmap[stringList.size()];
			// String[] stringArray = new String[stringList.size()];
			// stringArray = stringList.toArray(stringArray);
			// new BitmapLoader().execute(stringArray);
			new GifUploader().execute(stringList.toArray());
		}
	};

	public void onItemClick(int mPosition) {
		String tempValues = stringList.get(mPosition);

		Toast.makeText(ListImagesActivity.this, "Image URL : " + tempValues,
				Toast.LENGTH_LONG).show();
	}

	/*
	 * public void createGif(){ //Bitmap[] bitmaps = getBitmaps(); new
	 * SaveGifTask().execute(getBitmaps()); }
	 */

	private class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {

			String datapassed = arg1
					.getStringExtra(DatabaseService.DATA_PASSED);

			if (!stringList.contains(datapassed)) {
				stringList.add(datapassed);

				adapter.notifyDataSetChanged();
				// Toast.makeText(
				// MainActivity.this,
				// "Triggered by Service!\n" + "Data passed: "
				// + String.valueOf(datapassed), Toast.LENGTH_LONG)
				// .show();
			}
		}
	}

	private class GifUploader extends AsyncTask<Object, Void, Void> {
		@Override
		protected Void doInBackground(Object... param) {
			uploadImage(param);
			return null;
		}

		@Override
		protected void onPreExecute() {
			Log.i("Async-Example", "onPreExecute Called");
			simpleWaitDialog = ProgressDialog.show(ListImagesActivity.this,
					"Wait", "Creating GIF");
		}

		@Override
		protected void onPostExecute(Void result) {
			Log.i("Async-Example", "onPostExecute Called");
			simpleWaitDialog.dismiss();
			Toast.makeText(ListImagesActivity.this, "GIF added at " + SD_CARD,
					Toast.LENGTH_LONG).show();
		}

		private void uploadImage(Object[] urls) {
			try {
				ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();

				for (Object url : urls) {
					URL firstUrl = new URL(url.toString());
					HttpURLConnection connection = (HttpURLConnection) firstUrl
							.openConnection();
					connection.setDoInput(true);
					connection.connect();
					InputStream input = connection.getInputStream();
					Bitmap firstBitmap = BitmapFactory.decodeStream(input);
					Bitmap realFirstBitmap = Bitmap.createScaledBitmap(
							firstBitmap, 200, 200, false);
					bitmaps.add(realFirstBitmap);
				}

				File outputFile = new File(SD_CARD);
				FileOutputStream fos = new FileOutputStream(outputFile);

				if (fos != null) {
					AnimatedGifEncoder gifEncoder = new AnimatedGifEncoder();
					gifEncoder.start(fos);

					for (Bitmap current : bitmaps) {
						gifEncoder.addFrame(current);
					}

					gifEncoder.finish();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}