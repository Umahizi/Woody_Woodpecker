package com.woodywoodpecker.startmotion.imageslist;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.woodywoodpecker.startmotion.AnimatedGifEncoder;
import com.woodywoodpecker.startmotion.R;

@SuppressLint("SdCardPath")
public class ListImagesActivity extends Activity {
	private static final String SD_CARD = "/sdcard/Pictures/test31.gif";

	private ProgressDialog mSimpleWaitDialog;

	private ListView mList;
	private LazyImageLoadAdapter mAdapter;
	private ArrayList<String> mStringList;
	private MyReceiver mMyReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_images);
		mStringList = new ArrayList<String>();

		mList = (ListView) findViewById(R.id.listImages);

		// Create custom adapter for listview
		mAdapter = new LazyImageLoadAdapter(this, mStringList);

		// Set adapter to listview
		mList.setAdapter(mAdapter);

		mMyReceiver = new MyReceiver();

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(DatabaseService.MY_ACTION);
		registerReceiver(mMyReceiver, intentFilter);

		Button b = (Button) findViewById(R.id.cacheButton);
		b.setOnClickListener(listener);
	}

	@Override
	public void onDestroy() {
		// Remove adapter refference from list
		mList.setAdapter(null);
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		unregisterReceiver(mMyReceiver);
		super.onStop();
	}

	public OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			new GifUploader().execute(mStringList.toArray());
		}
	};

	public void onItemClick(int mPosition) {
		String currentUrl = mStringList.get(mPosition);

		Toast.makeText(ListImagesActivity.this, "Image URL : " + currentUrl,
				Toast.LENGTH_LONG).show();
	}

	private class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {

			String datapassed = arg1
					.getStringExtra(DatabaseService.DATA_PASSED);

			if (!mStringList.contains(datapassed)) {
				mStringList.add(datapassed);
				mAdapter.notifyDataSetChanged();
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
			mSimpleWaitDialog = ProgressDialog.show(ListImagesActivity.this,
					"Wait", "Creating GIF");
		}

		@Override
		protected void onPostExecute(Void result) {
			mSimpleWaitDialog.dismiss();
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