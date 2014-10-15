package com.woodywoodpecker.startmotion.imageslist;

import java.util.ArrayList;

import com.woodywoodpecker.startmotion.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ListImagesActivity extends Activity {
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

		Intent intent = new Intent(ListImagesActivity.this,
				DatabaseService.class);
		startService(intent);

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
			adapter.imageLoader.clearCache();
			adapter.notifyDataSetChanged();
		}
	};

	public void onItemClick(int mPosition) {
		String tempValues = stringList.get(mPosition);

		Toast.makeText(ListImagesActivity.this, "Image URL : " + tempValues,
				Toast.LENGTH_LONG).show();
	}

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
}