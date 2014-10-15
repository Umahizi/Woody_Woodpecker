package com.woodywoodpecker.startmotion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity implements View.OnClickListener {
	private static final int MIN_INPUT_LENGTH = 4;

	private EditText editUser, editPass, editPassConf, editEmail;
	private TextView wrongInput;
	private Button btnRegister;
	private SQLiteDatabaseContentProvider mDatabaseInstance;

	private void initElements() {
		editUser = (EditText) findViewById(R.id.editUsername);
		editPass = (EditText) findViewById(R.id.editPassword);
		editPassConf = (EditText) findViewById(R.id.editPasswordConf);
		editEmail = (EditText) findViewById(R.id.editEmail);
		wrongInput = (TextView) findViewById(R.id.wrongInput);
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
			wrongInput.setText("");
			boolean isInputValid = ValidateInput();

			if (isInputValid) {
				String username = editUser.getText().toString();
				String pass = editPass.getText().toString();
				String email = editEmail.getText().toString();

				boolean isRegistered = mDatabaseInstance.registerUser(username,
						pass, email);

				if (isRegistered) {
					Intent startScreen = new Intent(RegisterActivity.this,
							HomeActivity.class);
					startActivity(startScreen);
				} else {
					wrongInput.setText("Problem occured during registration!");
				}
			}
		}
	}

	private boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
		}
	}

	private boolean ValidateInput() {
		boolean result = false;
		String username = editUser.getText().toString();
		String pass = editPass.getText().toString();
		String confPass = editPassConf.getText().toString();
		String email = editEmail.getText().toString();

		if (username.length() >= MIN_INPUT_LENGTH) {
			if (!mDatabaseInstance.existUsername(username)) {
				if (pass.equals(confPass)) {
					if (pass.length() >= MIN_INPUT_LENGTH) {
						if (isValidEmail(email)) {
							if (!mDatabaseInstance.existEmail(email)) {
								result = true;
							} else {
								wrongInput.setText("Email already exists!");
							}
						} else {
							wrongInput.setText("Invalid email!");
						}
					} else {
						wrongInput.setText("Password should be at least "
								+ MIN_INPUT_LENGTH + " chars");
					}
				} else {
					wrongInput.setText("Different Passwords");
				}
			} else {
				wrongInput.setText("Username already exists!");
			}
		} else {
			wrongInput.setText("Username should be at least "
					+ MIN_INPUT_LENGTH + " chars");
		}

		return result;
	}
}