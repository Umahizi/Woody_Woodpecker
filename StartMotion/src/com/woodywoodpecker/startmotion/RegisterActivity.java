package com.woodywoodpecker.startmotion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity implements View.OnClickListener {

	private EditText editUser, editPass, editPassConf, editEmail;
	private Button btnRegister;
	private SQLiteDatabaseContentProvider mDatabaseInstance;

	private void initElements() {
		editUser = (EditText) findViewById(R.id.editUsername);
		editPass = (EditText) findViewById(R.id.editPassword);
		editPassConf = (EditText) findViewById(R.id.editPasswordConf);
		editEmail = (EditText) findViewById(R.id.editEmail);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(this);
		mDatabaseInstance = new SQLiteDatabaseContentProvider(
				getApplicationContext());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_layout);
		initElements();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnRegister) {
			String pass = editPass.getText().toString();
			String confPass = editPassConf.getText().toString();

			if (pass.equals(confPass)) {
				boolean isRegistered = mDatabaseInstance.registerUser(editUser
						.getText().toString(), editPass.getText().toString(),
						editEmail.getText().toString());

				if (isRegistered) {
					Intent startScreen = new Intent(RegisterActivity.this,
							HomeActivity.class);
					startActivity(startScreen);
				} else {
					Toast.makeText(this, "Problem with registration",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, "No match passwords", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}
}