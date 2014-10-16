package com.woodywoodpecker.startmotion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends Activity implements View.OnClickListener {
	private static final int MIN_INPUT_LENGTH = 4;

	private EditText mEditUser;
	private EditText mEditPass;
	private EditText mEditPassConf;
	private EditText mEditEmail;
	private TextView mWrongInput;
	private Button mBtnRegister;
	private SQLiteDatabaseContentProvider mDatabaseInstance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_layout);
		initElements();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnRegister) {
			mWrongInput.setText("");
			boolean isInputValid = ValidateInput();

			if (isInputValid) {
				String username = mEditUser.getText().toString();
				String pass = mEditPass.getText().toString();
				String email = mEditEmail.getText().toString();

				boolean isRegistered = mDatabaseInstance.registerUser(username,
						pass, email);

				if (isRegistered) {
					Intent startScreen = new Intent(RegisterActivity.this,
							HomeActivity.class);
					startActivity(startScreen);
				} else {
					mWrongInput.setText("Problem occured during registration!");
				}
			}
		}
	}

	private void initElements() {
		mEditUser = (EditText) findViewById(R.id.editUsername);
		mEditPass = (EditText) findViewById(R.id.editPassword);
		mEditPassConf = (EditText) findViewById(R.id.editPasswordConf);
		mEditEmail = (EditText) findViewById(R.id.editEmail);
		mWrongInput = (TextView) findViewById(R.id.wrongInput);
		mBtnRegister = (Button) findViewById(R.id.btnRegister);
		mBtnRegister.setOnClickListener(this);
		mDatabaseInstance = new SQLiteDatabaseContentProvider(
				getApplicationContext());
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
		String username = mEditUser.getText().toString();
		String pass = mEditPass.getText().toString();
		String confPass = mEditPassConf.getText().toString();
		String email = mEditEmail.getText().toString();

		if (username.length() >= MIN_INPUT_LENGTH) {
			if (!mDatabaseInstance.existUsername(username)) {
				if (pass.equals(confPass)) {
					if (pass.length() >= MIN_INPUT_LENGTH) {
						if (isValidEmail(email)) {
							if (!mDatabaseInstance.existEmail(email)) {
								result = true;
							} else {
								mWrongInput.setText("Email already exists!");
							}
						} else {
							mWrongInput.setText("Invalid email!");
						}
					} else {
						mWrongInput.setText("Password should be at least "
								+ MIN_INPUT_LENGTH + " chars");
					}
				} else {
					mWrongInput.setText("Different Passwords");
				}
			} else {
				mWrongInput.setText("Username already exists!");
			}
		} else {
			mWrongInput.setText("Username should be at least "
					+ MIN_INPUT_LENGTH + " chars");
		}

		return result;
	}
}