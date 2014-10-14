package com.woodywoodpecker.startmotion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class HomeActivity extends Activity implements View.OnClickListener {
	private EditText mUsernameField, mPasswordField;
	private Button mLoginButton;
	private SQLiteDatabaseContentProvider mDatabaseInstance;
	private Context mContext = this;
	private CheckBox rememberUserBox;
	private UserDataPreference mUserInfo;
	public static final String USER_NAME = "USERNAME";

	private void initElements() {
		// username
		mUsernameField = (EditText) findViewById(R.id.editPass);
		mPasswordField = (EditText) findViewById(R.id.editPass2);
		mLoginButton = (Button) findViewById(R.id.button1);
		mLoginButton.setOnClickListener(this);
		rememberUserBox = (CheckBox) findViewById(R.id.checkBox1);
		mDatabaseInstance = new SQLiteDatabaseContentProvider(mContext);
		mUserInfo = new UserDataPreference(mContext);
		
		if (mUserInfo.isLogged()) {
			//Intent intent = new Intent(HomeActivity.this, ItemActivity.class);
			//intent.putExtra("USERNAME", "Test");
			//startActivity(intent);
			Toast.makeText(mContext,
					"You are remembered already!!",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		initElements();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		if (id == R.id.action_settings) {
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// login button was clicked
		if (R.id.button1 == v.getId()) {
			if (!mDatabaseInstance.loginUser(mUsernameField.getText()
					.toString(), mPasswordField.getText().toString())) {
				Toast.makeText(mContext,
						"Please,press the register button to register !",
						Toast.LENGTH_SHORT).show();
			} else {
				if (rememberUserBox.isChecked()) {
					if (mUserInfo.rememeber(true)) {
						// open the other activity
						//Intent intent = new Intent(HomeActivity.this,
							//	ItemActivity.class);
						//intent.putExtra("USERNAME", "Test");
						//startActivity(intent);
						Toast.makeText(mContext, "New Activity(remember)",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					if (mUserInfo.rememeber(false)) {
						// open the other activity
						//Intent intent = new Intent(HomeActivity.this,
							//	ItemActivity.class);
						//intent.putExtra("USERNAME", "Test");
						//startActivity(intent);
						Toast.makeText(mContext, "New Activity",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		}
	}
}