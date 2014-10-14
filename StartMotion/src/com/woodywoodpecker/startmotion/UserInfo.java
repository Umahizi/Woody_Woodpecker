package com.woodywoodpecker.startmotion;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserInfo extends Activity implements View.OnClickListener {
	EditText passOne, passTwo;
	View v;
	Button btnChange;
	String username;
	SQLiteDatabaseContentProvider mDd;
	UserDataPreference userData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info);
		passOne = (EditText) findViewById(R.id.editPass);
		passTwo = (EditText) findViewById(R.id.editPass2);
		btnChange = (Button) findViewById(R.id.button1);
		btnChange.setOnClickListener(this);
		mDd = new SQLiteDatabaseContentProvider(getApplicationContext());
		userData = new UserDataPreference(getApplicationContext());
		username = savedInstanceState.getString(this
				.getString(R.string.user_pass_data));
	}

	@Override
	public void onClick(View v) {
		if (R.id.button1 == v.getId()) {
			if (username != null) {
				// its time to change password
				if (passOne.getText().toString()
						.equals(passTwo.getText().toString())) {
					// change the currentPassword
					mDd.updateUserData(username, passOne.getText().toString());
					userData.rememeber(false);
				} else {
					// password missmatch
					Toast.makeText(this, "Passwords dont match !",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, "Invalid username!",
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}