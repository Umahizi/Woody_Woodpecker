package com.woodywoodpecker.startmotion;

import com.telerik.everlive.sdk.core.login.GoogleLoginMethod;

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
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity implements View.OnClickListener {
	private EditText mUsernameField, mPasswordField;
	private TextView mInvalidUser;
	private Button mLoginButton, mRegButton;
	private SQLiteDatabaseContentProvider mDatabaseInstance;
	private Context mContext = this;
	private CheckBox rememberUserBox;
	private UserDataPreference mUserInfo;
	public static final String USER_NAME = "USERNAME";

	private void initElements() {
		// username
		mUsernameField = (EditText) findViewById(R.id.editPass);
		mPasswordField = (EditText) findViewById(R.id.editPass2);
		mInvalidUser = (TextView) findViewById(R.id.invalidUser);
		mLoginButton = (Button) findViewById(R.id.btnLogin);
		mRegButton = (Button) findViewById(R.id.btnRegister);
		mLoginButton.setOnClickListener(this);
		mRegButton.setOnClickListener(this);
		rememberUserBox = (CheckBox) findViewById(R.id.checkBox1);
		mDatabaseInstance = new SQLiteDatabaseContentProvider(mContext);
		mUserInfo = new UserDataPreference(mContext);

		if (mUserInfo.isLogged()) {
			// Intent intent = new Intent(HomeActivity.this,
			// ItemActivity.class);
			// intent.putExtra("USERNAME", "Test");
			// startActivity(intent);
			Intent intent = new Intent(HomeActivity.this,
					PhotoIntentActivity.class);
			startActivity(intent);
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
		if (R.id.btnLogin == v.getId()) {
			if (!mDatabaseInstance.loginUser(mUsernameField.getText()
					.toString(), mPasswordField.getText().toString())) {
				mInvalidUser.setVisibility(View.VISIBLE);
			} else {
				boolean state = rememberUserBox.isChecked();
				mUserInfo.rememeber(state);

				Intent intent = new Intent(HomeActivity.this,
						PhotoIntentActivity.class);
				startActivity(intent);
			}

		} else if (R.id.btnRegister == v.getId()) {
			Intent screenRegister = new Intent(HomeActivity.this,
					RegisterActivity.class);
			startActivity(screenRegister);
		}
	}
}